-- Add mail type
ALTER TABLE
	message
	ADD COLUMN mail_type VARCHAR(255) AFTER sent_date;

UPDATE
	message
SET
	mail_type = 'PLAIN';

-- Upgrade version
UPDATE
	application_configuration 
SET
	`value`='1.44'
WHERE
	name='VERSION';
