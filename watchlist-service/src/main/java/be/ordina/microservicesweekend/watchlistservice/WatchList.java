package be.ordina.microservicesweekend.watchlistservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class WatchList {

	@Id
	@GeneratedValue
	private Long id;
	private String movieId;
	private String reason;
}
