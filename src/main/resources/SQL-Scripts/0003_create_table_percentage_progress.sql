CREATE TABLE percentage_progress(
	id BIGINT AUTO_INCREMENT NOT NULL
	, operation VARCHAR(30) NOT NULL
	, related_to BIGINT NOT NULL
	, locked BIT NULL
	, locked_by BIGINT NULL
	, created_at DATETIME NOT NULL
	, last_updated DATETIME NOT NULL
	, progression FLOAT NOT NULL
	, progression_status VARCHAR(30) NOT NULL
	, CONSTRAINT PK_percentage PRIMARY KEY (id)
	, FOREIGN KEY FK_perc_related_to (related_to) REFERENCES user_info(id)
	, FOREIGN KEY FK_perc_locked_by (locked_by) REFERENCES user_info(id)
);
