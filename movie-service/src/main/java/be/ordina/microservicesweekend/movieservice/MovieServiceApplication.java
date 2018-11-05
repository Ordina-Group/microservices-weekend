package be.ordina.microservicesweekend.movieservice;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancerExchangeFilterFunction;
import org.springframework.cloud.netflix.hystrix.HystrixCommands;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/movie")
@EnableCircuitBreaker
@EnableDiscoveryClient
@SpringBootApplication
public class MovieServiceApplication {

	private static final String MOVIE_DB_URL =
		"https://api.themoviedb.org/3/find/%s?external_source=imdb_id&language=en-US&api_key=3c5dc7cf33e70ad798ec4337a7a14605";

	private static final String RECOMMENDATION_SERVICE_URL = "http://recommendation-service/recommendation/";

	private final WebClient movieClient;
	private final WebClient recommendationClient;

	public static void main(String[] args) {
		SpringApplication.run(MovieServiceApplication.class, args);
	}

	public MovieServiceApplication(LoadBalancerExchangeFilterFunction lbFunction) {
		this.movieClient = WebClient.builder().build();
		this.recommendationClient = WebClient.builder().filter(lbFunction).build();
	}

	@RequestMapping("/{movieId}")
	public Mono<Movie> findMovie(@PathVariable("movieId") final String movieId) {
		Flux<Recommendation> recommendations = HystrixCommands.from(this.recommendationClient.get()
			.uri(RECOMMENDATION_SERVICE_URL + String.format("%s", movieId))
			.retrieve()
			.bodyToFlux(Recommendation.class))
			.fallback(Flux.just(new Recommendation("Fallback description")))
			.commandName("find-recommendations")
			.toFlux();

		Mono<Movie> movieMono = HystrixCommands.from(movieClient.get()
			.uri(String.format(MOVIE_DB_URL, movieId))
			.retrieve()
			.bodyToMono(Movies.class)
			.map(movies -> movies.getMovies().get(0)))
			.fallback(Mono.just(new Movie("Fallback Movie", "Fallback Overview", 0.0, Lists.newArrayList())))
			.commandName("tmdb-find-movie")
			.toMono();

		return movieMono.flatMap(movie -> recommendations.map(movie::addRecommendation).last());
	}
}

@Data class Movies {
	@JsonProperty("movie_results")
	private List<Movie> movies;
}

@Data
@NoArgsConstructor
@AllArgsConstructor class Movie {
	@JsonSetter("original_title")
	private String title;

	private String overview;

	@JsonSetter("vote_average")
	private double rating;

	private List<Recommendation> recommendations = new ArrayList<>();

	Movie addRecommendation(Recommendation recommendation) {
		this.recommendations.add(recommendation);

		return this;
	}
}

@Data
@NoArgsConstructor
@AllArgsConstructor class Recommendation {
	private String description;
}