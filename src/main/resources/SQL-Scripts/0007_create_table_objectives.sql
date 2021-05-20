CREATE TABLE objective (
	id BIGINT AUTO_INCREMENT NOT NULL
	, objective_type  VARCHAR(30) NOT NULL
	, description VARCHAR(255) NOT NULL
	, CONSTRAINT PK_lobby PRIMARY KEY (id)
);
