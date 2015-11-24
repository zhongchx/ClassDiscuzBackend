INSERT INTO user(email,password,name,college,major,focus) VALUES('zhongchx@andrew.cmu.edu','12345678','Zhongchuan Xu','CIT','ECE','0');
INSERT INTO user(email,password,name,college,major,focus) VALUES('','','Andrew','CIT','ECE','0');
INSERT INTO user(email,password,name,college,major,focus) VALUES('','','Penny','CIT','ECE','0');

INSERT INTO course(num,instructor,name,time) VALUES('18641','BOB','JAVA','TR 12:30-13:30');
INSERT INTO course(num,instructor,name,time) VALUES('18746','abc','Storage System','TR 16:30-17:50');
INSERT INTO course(num,instructor,name,time) VALUES('18655','asdad','Service Oriented Computing','1234');
INSERT INTO course(num,instructor,name,time) VALUES('18601','vxczxczc','Inovation and blah blah','4321');

INSERT INTO registration(c_id,s_id) VALUES('1','1');
INSERT INTO registration(c_id,s_id) VALUES('2','1');

DELETE FROM registration WHERE c_id='1' AND s_id='1';

CREATE DATABASE classdiscuz;

CREATE TABLE user (
    id INT NOT NULL AUTO_INCREMENT,
    email varchar(64) NOT NULL,
    password varchar(32) NOT NULL,
    name varchar(64) NOT NULL,
    avatar MEDIUMBLOB,
    college varchar(128),
    major varchar(128),
    focus INT NOT NULL,
    PRIMARY KEY (id)
); 

CREATE TABLE course (
    id INT NOT NULL AUTO_INCREMENT,
    num varchar(64) NOT NULL,
    instructor varchar(64) NOT NULL,
    name varchar(128) NOT NULL,
    time varchar(128) NOT NULL,
    location varchar(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE registration (
    id INT NOT NULL AUTO_INCREMENT,
    c_id INT NOT NULL,
    s_id INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (c_id) REFERENCES course(id),
    FOREIGN KEY (s_id) REFERENCES user(id)
);

LOAD DATA INFILE 'courses.tsv'  INTO TABLE course FIELDS TERMINATED BY '\t' ENCLOSED BY '\'' LINES TERMINATED BY ')\n' STARTING BY '(' (num,instructor,name,time,location);