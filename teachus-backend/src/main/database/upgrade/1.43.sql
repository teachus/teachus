-- Fix incorrect boolean types
ALTER TABLE
	booking 
		CHANGE active active TINYINT(1),
		CHANGE notification_sent notification_sent TINYINT(1),
		CHANGE paid paid TINYINT(1);

ALTER TABLE
	person 
		CHANGE active active TINYINT(1);

-- Create message tables
CREATE TABLE
	message 
(
	id bigint NOT NULL auto_increment, 
	message_type VARCHAR(255) NOT NULL, 
	version INTEGER NOT NULL, 
	create_date DATETIME,
	subject VARCHAR(255), 
	body TEXT, 
	sender BIGINT, 
	sent TINYINT(1), 
	sent_date DATETIME,
	primary key (id)
) type=InnoDB;

CREATE TABLE
	message_recipient 
(
	message_id BIGINT NOT NULL, 
	person_id BIGINT NOT NULL, 
	primary key (message_id, person_id)
) type=InnoDB;

ALTER TABLE
	message 
		ADD INDEX FK38EB0007E0E4002A (sender), 
		ADD CONSTRAINT FK38EB0007E0E4002A FOREIGN KEY (sender) REFERENCES person (id);

ALTER TABLE 
	message_recipient 
		ADD INDEX FK398E4FE149BE803A (person_id), 
		ADD CONSTRAINT FK398E4FE149BE803A FOREIGN KEY (person_id) REFERENCES person (id);

ALTER TABLE 
	message_recipient 
		ADD INDEX FK398E4FE1CAFD62F8 (message_id), 
		ADD CONSTRAINT FK398E4FE1CAFD62F8 FOREIGN KEY (message_id) REFERENCES message (id);

-- Upgrade version
UPDATE
	application_configuration 
SET
	`value`='1.43'
WHERE
	name='VERSION';
