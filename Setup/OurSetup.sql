/*
 *  File name:  setup.sql
 *  Function:   to create the intial database schema for the CMPUT 391 project,
 *              Fall, 2014
 *  Author:     Prof. Li-Yan Yuan
 */
 
DROP SEQUENCE pic_id_seq;
DROP SEQUENCE group_id_seq;
DROP TABLE hitcounts;
DROP TABLE hits;
DROP TABLE imagetypes;
DROP TABLE images;
DROP TABLE group_lists;
DROP TABLE groups;
DROP TABLE persons;
DROP TABLE users;

COMMIT;

CREATE TABLE users (
   user_name varchar(24),
   password  varchar(24),
   date_registered date,
   primary key(user_name)
);

CREATE TABLE persons (
   user_name  varchar(24),
   first_name varchar(24),
   last_name  varchar(24),
   address    varchar(128),
   email      varchar(128),
   phone      char(10),
   PRIMARY KEY(user_name),
   UNIQUE (email),
   FOREIGN KEY (user_name) REFERENCES users
);


CREATE TABLE groups (
   group_id   int,
   user_name  varchar(24),
   group_name varchar(24),
   date_created date,
   PRIMARY KEY (group_id),
   UNIQUE (user_name, group_name),
   FOREIGN KEY(user_name) REFERENCES users
);

INSERT INTO groups values(1,null,'public', sysdate);
INSERT INTO groups values(2,null,'private',sysdate);

CREATE TABLE group_lists (
   group_id    int,
   friend_id   varchar(24),
   date_added  date,
   notice      varchar(1024),
   PRIMARY KEY(group_id, friend_id),
   FOREIGN KEY(group_id) REFERENCES groups,
   FOREIGN KEY(friend_id) REFERENCES users
);

CREATE TABLE images (
   photo_id    int,
   owner_name  varchar(24),
   permitted   int,
   subject     varchar(128),
   place       varchar(128),
   timing      date,
   description varchar(2048),
   thumbnail   blob,
   photo       blob,
   PRIMARY KEY(photo_id),
   FOREIGN KEY(owner_name) REFERENCES users,
   FOREIGN KEY(permitted) REFERENCES groups
);

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

COMMIT;
