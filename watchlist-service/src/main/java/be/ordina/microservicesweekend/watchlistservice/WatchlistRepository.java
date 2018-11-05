package be.ordina.microservicesweekend.watchlistservice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WatchlistRepository extends JpaRepository<WatchList, Long> {

	List<WatchList> findAllByMovieId(String movieId);
}
