CREATE TABLE 
	currency 
(
	id BIGINT NOT NULL auto_increment, 
	version INTEGER NOT NULL, 
	teacher_id BIGINT NOT NULL, 
	label VARCHAR(10) NOT NULL,
	base TINYINT DEFAULT FALSE, 
	exchange_rate DOUBLE PRECISION NOT NULL, 
	primary key (id)
) type=InnoDB;
		
ALTER TABLE
	currency 
		ADD INDEX FK224BF0111CC24034 (teacher_id), 
		ADD CONSTRAINT FK224BF0111CC24034 FOREIGN KEY (teacher_id) REFERENCES person (id);
		
ALTER TABLE
	period 
		ADD COLUMN currency_id BIGINT AFTER price;
		
ALTER TABLE
	period 
		ADD INDEX FKC4E375C17D5D44A0 (currency_id),
		ADD CONSTRAINT FKC4E375C17D5D44A0 FOREIGN KEY (currency_id) REFERENCES currency (id);
