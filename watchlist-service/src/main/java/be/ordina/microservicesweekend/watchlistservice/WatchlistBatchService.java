package be.ordina.microservicesweekend.watchlistservice;

import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WatchlistBatchService {

	private final WatchlistRepository watchlistRepository;

	public WatchlistBatchService(
		WatchlistRepository watchlistRepository) {
		this.watchlistRepository = watchlistRepository;
	}

	@Scheduled(cron = "* * * * * MON-FRI")
	@SchedulerLock(name = "scheduledTaskName")
	public void createWatchlistBatch() {
		log.info("Creating a new watchlist");
		this.watchlistRepository.save(WatchList.builder().movieId("bla").reason("reason").build());
	}
}
