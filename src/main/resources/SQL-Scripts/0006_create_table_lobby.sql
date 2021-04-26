CREATE TABLE lobby (
	id BIGINT AUTO_INCREMENT NOT NULL
	, user_1_id BIGINT NULL
	, user_2_id BIGINT NULL
	, user_3_id BIGINT NULL
	, user_4_id BIGINT NULL
	, last_in_lobby_1 BIGINT NULL
	, last_in_lobby_2 BIGINT NULL
	, last_in_lobby_3 BIGINT NULL
	, last_in_lobby_4 BIGINT NULL
	, lobby_type VARCHAR(30) NOT NULL
	, lobby_name VARCHAR(255) NULL
	, game_id BIGINT NULL
	, CONSTRAINT PK_lobby PRIMARY KEY (id)
);
