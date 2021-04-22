CREATE TABLE lobby (
	id BIGINT AUTO_INCREMENT NOT NULL
	, user_1_id BIGINT NULL
	, user_2_id BIGINT NULL
	, user_3_id BIGINT NULL
	, user_4_id BIGINT NULL
	, last_in_lobby_1 DATETIME NULL
	, last_in_lobby_2 DATETIME NULL
	, last_in_lobby_3 DATETIME NULL
	, last_in_lobby_4 DATETIME NULL
	, CONSTRAINT PK_lobby PRIMARY KEY (id)
	, FOREIGN KEY FK_lobby_user_1_id (user_1_id) REFERENCES user_info(id)
	, FOREIGN KEY FK_lobby_user_2_id (user_2_id) REFERENCES user_info(id)
	, FOREIGN KEY FK_lobby_user_3_id (user_3_id) REFERENCES user_info(id)
	, FOREIGN KEY FK_lobby_user_4_id (user_4_id) REFERENCES user_info(id)
);
