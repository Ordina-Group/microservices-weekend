package be.ordina.microservicesweekend.recommendationservice;

import lombok.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;

@RestController
@RequestMapping("/recommendation")
@EnableEurekaClient
@SpringBootApplication
public class RecommendationServiceApplication implements CommandLineRunner {

	private final RecommendationService recommendationService;

	public RecommendationServiceApplication(
		RecommendationService recommendationService) {
		this.recommendationService = recommendationService;
	}

	public static void main(String[] args) {
		SpringApplication.run(RecommendationServiceApplication.class, args);
	}

	@GetMapping("/{movieId}")
	public List<Recommendation> getRecommendations(@PathVariable final String movieId) {
		return this.recommendationService.getRecommendations(movieId);
	}

	@Override public void run(String... args) {
		this.recommendationService.createRecommendation("tt3778644", "Would not recommend");
		this.recommendationService.createRecommendation("tt3778644", "Sucks!");
		this.recommendationService.createRecommendation("tt3778644", "It Kicks Ass!");
		this.recommendationService.createRecommendation("tt3778644", "0/10 I fell asleep");
	}
}

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity class Recommendation {

	@Id
	@GeneratedValue
	private Long id;

	private String movieId;
	private String description;
}

@Service class RecommendationService {

	private final RecommendationRepository recommendationRepository;

	RecommendationService(
		RecommendationRepository recommendationRepository) {
		this.recommendationRepository = recommendationRepository;
	}

	List<Recommendation> getRecommendations(String movieId) {
		return this.recommendationRepository.findAllByMovieId(movieId);
	}

	void createRecommendation(String movieId, String description) {
		this.recommendationRepository.save(Recommendation.builder()
			.movieId(movieId)
			.description(description)
			.build());
	}
}

@Repository interface RecommendationRepository extends JpaRepository<Recommendation, Long> {

	List<Recommendation> findAllByMovieId(String movieId);
}

