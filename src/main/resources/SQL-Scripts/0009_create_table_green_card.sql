CREATE TABLE green_card(
	id BIGINT AUTO_INCREMENT NOT NULL
	, green_card_type VARCHAR(30) NOT NULL
	, green_card_subtype VARCHAR(30) NOT NULL
	, cost BIGINT NOT NULL
	, description VARCHAR(255) NOT NULL
	, CONSTRAINT PK_lobby PRIMARY KEY (id)
);
