CREATE TABLE
	application_configuration
(
	name VARCHAR(255) NOT NULL, 
	version INTEGER NOT NULL DEFAULT 0, 
	value MEDIUMTEXT, 
	primary key (name)
) ENGINE=InnoDB;

CREATE TABLE 
	booking 
(
	id BIGINT NOT NULL auto_increment, 
	booking_type VARCHAR(255) NOT NULL, 
	version INTEGER NOT NULL DEFAULT 0, 
	active TINYINT(1),
	period_id BIGINT, 
	date DATETIME,
	create_date DATETIME,
	update_date DATETIME,
	teacher_id BIGINT, 
	pupil_id BIGINT, 
	notification_sent TINYINT(1),
	pupil_notification_sent TINYINT(1), 
	paid TINYINT(1), 
	primary key (id)
) ENGINE=InnoDB;

CREATE TABLE 
	period 
(
	id BIGINT NOT NULL auto_increment,
	version INTEGER NOT NULL DEFAULT 0, 
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
	status VARCHAR(10) NOT NULL,
	primary key (id)
) ENGINE=InnoDB;

CREATE TABLE 
	person 
(
	id BIGINT NOT NULL auto_increment, 
	person_type VARCHAR(255) NOT NULL, 
	version INTEGER NOT NULL DEFAULT 0, 
	name VARCHAR(100), 
	username VARCHAR(50) NOT NULL UNIQUE, 
	password VARCHAR(40), 
	email VARCHAR(250), 
	phone_number VARCHAR(20), 
	locale VARCHAR(20), 
	theme VARCHAR(40), 
	active TINYINT(1), 
	currency VARCHAR(10),
	teacher_id BIGINT, 
	notes MEDIUMTEXT,
	primary key (id)
) ENGINE=InnoDB;

CREATE TABLE 
	teacher_attribute 
(
	id BIGINT NOT NULL auto_increment, 
	attribute VARCHAR(255) NOT NULL, 
	version INTEGER NOT NULL DEFAULT 0, 
	teacher_id BIGINT NOT NULL, 
	value TEXT NOT NULL, 
	primary key (id)
) ENGINE=InnoDB;

CREATE TABLE
	message 
(
	id bigint NOT NULL auto_increment, 
	message_type VARCHAR(255) NOT NULL, 
	version INTEGER NOT NULL DEFAULT 0, 
	create_date DATETIME,
	subject VARCHAR(255), 
	body TEXT, 
	sender BIGINT, 
	recipient BIGINT, 
	state VARCHAR(255), 
	processing_date DATETIME,
	mail_type VARCHAR(255),
	primary key (id)
) ENGINE=InnoDB;

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

ALTER TABLE
	message 
		ADD INDEX FK38EB0007E0E4002A (sender), 
		ADD CONSTRAINT FK38EB0007E0E4002A FOREIGN KEY (sender) REFERENCES person (id),
		ADD INDEX IDX_MESSAGE_RECIPIENT (recipient), 
		ADD CONSTRAINT FK_MESSAGE_RECIPIENT_PERSON FOREIGN KEY (recipient) REFERENCES person (id);

INSERT INTO application_configuration (name, value) VALUES ('VERSION', '1.59');
