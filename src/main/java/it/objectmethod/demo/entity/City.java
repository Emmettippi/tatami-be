package it.objectmethod.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity // Indica che questa classe e' un Entita' JPA
@Table(name = "city") // Indichiamo il nome della tabella a DB a cui fa riferimento questa entita
public class City {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	// Valore generato e assegnato dal DB
	@Id // Indica che e' la chiave primaria
	private Long id;

	@Column(name = "Name") // Colonna relativa a DB
	private String name;

	@Column(name = "Population")
	private Integer population;

	@Column(name = "CountryCode")
	private String codiceNazione;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPopulation() {
		return population;
	}

	public void setPopulation(Integer population) {
		this.population = population;
	}

	public String getCodiceNazione() {
		return codiceNazione;
	}

	public void setCodiceNazione(String codiceNazione) {
		this.codiceNazione = codiceNazione;
	}

}
