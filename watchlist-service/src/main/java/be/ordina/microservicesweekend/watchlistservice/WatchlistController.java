package be.ordina.microservicesweekend.watchlistservice;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/watchlist")
public class WatchlistController {

	private final WatchlistService watchlistService;

	public WatchlistController(WatchlistService watchlistService) {
		this.watchlistService = watchlistService;
	}

	public List<WatchList> getWatchlist(final String movieId) {
		return this.watchlistService.findWatchlist(movieId);
	}
}
