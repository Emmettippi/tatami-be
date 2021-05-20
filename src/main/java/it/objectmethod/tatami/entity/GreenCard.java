package it.objectmethod.tatami.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import it.objectmethod.tatami.entity.enums.GreenCardSubtype;
import it.objectmethod.tatami.entity.enums.GreenCardType;
import lombok.Data;

@Data
@Entity
@Table(name = "green_card")
public class GreenCard {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "green_card_type")
	@Enumerated(EnumType.STRING)
	private GreenCardType greenCardType;

	@Column(name = "green_card_subtype")
	@Enumerated(EnumType.STRING)
	private GreenCardSubtype greenCardSubtype;

	public void setGreenCardSubtype(GreenCardSubtype greenCardSubtype) {
		this.greenCardSubtype = greenCardSubtype;
		if (greenCardSubtype != null) {
			this.greenCardType = greenCardSubtype.greenCardType();
		}
	}

	@Column(name = "cost")
	private Long cost;

	@Column(name = "description")
	private String description;
}
