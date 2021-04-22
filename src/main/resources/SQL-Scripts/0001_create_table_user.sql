CREATE TABLE user_info(
	id BIGINT AUTO_INCREMENT NOT NULL
	, username VARCHAR(64) NOT NULL
	, password VARCHAR(32) NOT NULL
	, nickname VARCHAR(64) NOT NULL
	, email VARCHAR(512) NOT NULL
	, user_status VARCHAR(30) NOT NULL
	, last_online DATETIME NULL
	, profile_image BLOB NULL
	, CONSTRAINT PK_user PRIMARY KEY (id)
	, CONSTRAINT UK_username UNIQUE (username)
	, CONSTRAINT UK_email UNIQUE (email)
);
