CREATE TABLE percentage_errors(
	id BIGINT AUTO_INCREMENT NOT NULL
	, message_type VARCHAR(30) NOT NULL
	, message_type_id BIGINT NOT NULL
	, message_key VARCHAR(255) NOT NULL
	, extra_notes VARCHAR(4000) NULL
	, extra_param_1 VARCHAR(255) NULL
	, extra_param_2 VARCHAR(255) NULL
	, extra_param_3 VARCHAR(255) NULL
	, extra_param_4 VARCHAR(255) NULL
	, percentage_id BIGINT NOT NULL
	, CONSTRAINT PK_percentage_errors PRIMARY KEY (id)
	, FOREIGN KEY FK_perc_error_perc_id (percentage_id) REFERENCES percentage_progress(id)
		ON DELETE CASCADE ON UPDATE CASCADE
);
