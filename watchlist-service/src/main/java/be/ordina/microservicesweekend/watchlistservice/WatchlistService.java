package be.ordina.microservicesweekend.watchlistservice;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class WatchlistService {

	private final WatchlistRepository watchlistRepository;

	public WatchlistService(WatchlistRepository watchlistRepository) {
		this.watchlistRepository = watchlistRepository;
	}

	@Transactional
	public List<WatchList> findWatchlist(String movieId) {
		return this.watchlistRepository.findAllByMovieId(movieId);
	}
}
