package be.ordina.microservicesweekend.watchlistservice;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WatchlistService {

	private final WatchlistRepository watchlistRepository;

	public WatchlistService(WatchlistRepository watchlistRepository) {
		this.watchlistRepository = watchlistRepository;
	}

	public List<WatchList> findWatchlist(String movieId) {
		return this.watchlistRepository.findAllByMovieId(movieId);
	}
}
