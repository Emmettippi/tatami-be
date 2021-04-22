CREATE TABLE user_user(
	id BIGINT IDENTITY(1,1) NOT NULL
	, user_1_id BIGINT NOT NULL
	, user_2_id BIGINT NOT NULL
	, relationship VARCHAR(30) NOT NULL
	, CONSTRAINT PK_user_user PRIMARY KEY (id)
	, CONSTRAINT FK_user_1_id (user_1_id) REFERENCES user_info(id)
	, CONSTRAINT FK_user_2_id (user_2_id) REFERENCES user_info(id)
);
