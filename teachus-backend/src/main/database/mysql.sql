CREATE TABLE
	application_configuration
(
	name VARCHAR(255) NOT NULL, 
	version INTEGER NOT NULL, 
	value MEDIUMTEXT, 
	primary key (name)
) type=InnoDB;

CREATE TABLE 
	booking 
(
	id BIGINT NOT NULL auto_increment, 
	booking_type VARCHAR(255) NOT NULL, 
	version INTEGER NOT NULL, 
	active TINYINT,
	period_id BIGINT, 
	date DATETIME,
	create_date DATETIME,
	update_date DATETIME,
	teacher_id BIGINT, 
	pupil_id BIGINT, 
	notification_sent TINYINT, 
	paid TINYINT, 
	primary key (id)
) type=InnoDB;

CREATE TABLE 
	period 
(
	id BIGINT NOT NULL auto_increment,
	version INTEGER NOT NULL, 
	name VARCHAR(100), 
	begin_date DATE, 
	end_date DATE, 
	start_time TIME, 
	end_time TIME, 
	week_days VARCHAR(255), 
	teacher_id BIGINT, 
	location VARCHAR(100), 
	price DOUBLE PRECISION,
	lesson_duration INTEGER NOT NULL, 
	interval_between_lesson_start INTEGER NOT NULL,
	repeat_every_week INTEGER NOT NULL,
	active TINYINT DEFAULT TRUE,
	primary key (id)
) type=InnoDB;

CREATE TABLE 
	person 
(
	id BIGINT NOT NULL auto_increment, 
	person_type VARCHAR(255) NOT NULL, 
	version INTEGER NOT NULL, 
	name VARCHAR(100), 
	username VARCHAR(50) NOT NULL UNIQUE, 
	password VARCHAR(40), 
	email VARCHAR(250), 
	phone_number VARCHAR(20), 
	locale VARCHAR(20), 
	theme VARCHAR(40), 
	active TINYINT, 
	currency VARCHAR(10),
	teacher_id BIGINT, 
	primary key (id)
) type=InnoDB;

CREATE TABLE 
	teacher_attribute 
(
	id BIGINT NOT NULL auto_increment, 
	attribute VARCHAR(255) NOT NULL, 
	version INTEGER NOT NULL, 
	teacher_id BIGINT NOT NULL, 
	value TEXT NOT NULL, 
	primary key (id)
) type=InnoDB;

ALTER TABLE 
	booking 
		ADD INDEX FK3DB08598007F534 (pupil_id), 
		ADD CONSTRAINT FK3DB08598007F534 FOREIGN KEY (pupil_id) REFERENCES person (id);
		
ALTER TABLE 
	booking 
		ADD INDEX FK3DB08591CC24034 (teacher_id), 
		ADD CONSTRAINT FK3DB08591CC24034 FOREIGN KEY (teacher_id) REFERENCES person (id);
		
ALTER TABLE 
	booking 
		ADD INDEX FK3DB0859FEE2C820 (period_id), 
		ADD CONSTRAINT FK3DB0859FEE2C820 FOREIGN KEY (period_id) REFERENCES period (id);
		
ALTER TABLE 
	period 
		ADD INDEX FKC4E375C11CC24034 (teacher_id), 
		ADD CONSTRAINT FKC4E375C11CC24034 FOREIGN KEY (teacher_id) REFERENCES person (id);
		
ALTER TABLE 
	person 
		ADD INDEX FKC4E39B551CC24034 (teacher_id), 
		ADD CONSTRAINT FKC4E39B551CC24034 FOREIGN KEY (teacher_id) REFERENCES person (id);
		
ALTER TABLE 
	teacher_attribute 
		ADD INDEX FKD49E7E7F1CC24034 (teacher_id), 
		ADD CONSTRAINT FKD49E7E7F1CC24034 FOREIGN KEY (teacher_id) REFERENCES person (id);
