-- Application configuration
CREATE TABLE
	application_configuration
(
	name VARCHAR(255) NOT NULL, 
	version INTEGER NOT NULL, 
	value MEDIUMTEXT, 
	primary key (name)
) type=InnoDB;

INSERT INTO 
	application_configuration 
(
	name, 
	value
) 
VALUES 
(
	'VERSION', 
	'1.40'
);

-- Period status
ALTER TABLE
	period 
		ADD COLUMN status VARCHAR(10) NOT NULL AFTER active;

UPDATE
	period
SET
	status = IF(active = 1, 'FINAL', 'DELETED');

ALTER TABLE
	period
		DROP COLUMN active;
