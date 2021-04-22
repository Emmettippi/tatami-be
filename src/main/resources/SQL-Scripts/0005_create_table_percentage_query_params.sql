CREATE TABLE percentage_query_params(
	id BIGINT AUTO_INCREMENT NOT NULL
	, str_param_1 VARCHAR(255) NULL
	, str_param_2 VARCHAR(255) NULL
	, str_param_3 VARCHAR(255) NULL
	, str_param_4 VARCHAR(255) NULL
	, str_param_5 VARCHAR(255) NULL
	, decimal_param_1 DOUBLE NULL
	, decimal_param_2 DOUBLE NULL
	, decimal_param_3 DOUBLE NULL
	, decimal_param_4 DOUBLE NULL
	, decimal_param_5 DOUBLE NULL
	, integer_param_1 BIGINT NULL
	, integer_param_2 BIGINT NULL
	, integer_param_3 BIGINT NULL
	, integer_param_4 BIGINT NULL
	, integer_param_5 BIGINT NULL
	, percentage_id BIGINT NOT NULL
	, CONSTRAINT PK_query_params PRIMARY KEY (id)
	, FOREIGN KEY FK_query_params_perc_id (percentage_id) REFERENCES percentage_progress(id)
		ON DELETE CASCADE ON UPDATE CASCADE
);
