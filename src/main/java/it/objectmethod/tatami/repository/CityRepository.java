package it.objectmethod.tatami.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import it.objectmethod.tatami.entity.City;

@Repository
public interface CityRepository extends JpaRepository<City, Long> { 
	public City findByName(String name);

	public List<City> findByCodiceNazione(String codice);

	@Query(value = "select x from City x where x.codiceNazione = ?1")
	public List<City> findCitiesByCodiceNazione(String codNaz);

	@Query(value = "select x from City x where x.population > ?1 and x.population < ?2")
	public List<City> findCitiesByPopulation(Integer minPop, Integer maxPop);
}
