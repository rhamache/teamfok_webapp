DROP SEQUENCE pic_id_seq;
DROP SEQUENCE group_id_seq;
DROP TABLE hitcounts;
DROP TABLE hits;
DROP TABLE imagetypes;

CREATE SEQUENCE pic_id_seq
	START WITH 1
	INCREMENT BY 1
	NOCACHE
	NOCYCLE;
	
CREATE SEQUENCE group_id_seq
	START WITH 3
	INCREMENT BY 1
	NOCACHE
	NOCYCLE
	MINVALUE 3;
	
CREATE TABLE hitcounts (
	photo_id int,
	uniq_hits int,
	PRIMARY KEY (photo_id),
	FOREIGN KEY (photo_id) REFERENCES images
);

CREATE TABLE hits (
	user_name varchar(24),
	photo_id int,
	PRIMARY KEY (user_name, photo_id),
	FOREIGN KEY (photo_id) REFERENCES images,
	FOREIGN KEY (user_name) REFERENCES users
);

CREATE TABLE imagetypes (
	photo_id int,
	type varchar(20) NOT NULL,
	PRIMARY KEY(photo_id),
	FOREIGN KEY(photo_id) REFERENCES images
);

INSERT INTO users
VALUES('admin', '92668751', sysdate);
	
