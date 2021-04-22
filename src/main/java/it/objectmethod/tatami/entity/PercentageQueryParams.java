package it.objectmethod.tatami.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "percentage_query_params")
public class PercentageQueryParams {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "str_param_1")
	private String strParam1;

	@Column(name = "str_param_2")
	private String strParam2;

	@Column(name = "str_param_3")
	private String strParam3;

	@Column(name = "str_param_4")
	private String strParam4;

	@Column(name = "str_param_5")
	private String strParam5;

	@Column(name = "decimal_param_1")
	private Double decimalParam1;

	@Column(name = "decimal_param_2")
	private Double decimalParam2;

	@Column(name = "decimal_param_3")
	private Double decimalParam3;

	@Column(name = "decimal_param_4")
	private Double decimalParam4;

	@Column(name = "decimal_param_5")
	private Double decimalParam5;

	@Column(name = "integer_param_1")
	private Long integerParam1;

	@Column(name = "integer_param_2")
	private Long integerParam2;

	@Column(name = "integer_param_3")
	private Long integerParam3;

	@Column(name = "integer_param_4")
	private Long integerParam4;

	@Column(name = "integer_param_5")
	private Long integerParam5;

	@ManyToOne
	@JoinColumn(nullable = false, name = "percentage_id")
	private Percentage percentage;
}
