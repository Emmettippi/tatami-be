package it.objectmethod.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import it.objectmethod.demo.entity.City;

@Repository // @Component + indica che e' una repository jpa
public interface CityRepository extends JpaRepository<City, Long> { // City e' il riferimento alla entity gestita da
																	// questa repo, Long e' il tipo dell'ID

	public City findByName(String name); // Fare riferimento al link di Spring JPA

	public List<City> findByCodiceNazione(String codice);

	// Attenzione, JPA Query fa riferimento all'entity, quindi notare il 'c' e il
	// codiceNazione
	@Query(value = "select x from City x where x.codiceNazione = ?1")
	public List<City> findCitiesByCodiceNazione(String codNaz);

	@Query(value = "select x from City x where x.population > ?1 and x.population < ?2")
	public List<City> findCitiesByPopulation(Integer minPop, Integer maxPop);

}
