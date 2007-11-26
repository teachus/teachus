-- Add mail type
ALTER TABLE
	message
	ADD COLUMN mail_type VARCHAR(255) AFTER sent_date;

UPDATE
	message
SET
	mail_type = 'PLAIN';

-- Add pupil notification flag
ALTER TABLE
	booking
	ADD COLUMN pupil_notification_sent TINYINT(1) AFTER notification_sent;

UPDATE
	booking
SET
	pupil_notification_sent = 1;

-- Upgrade version
UPDATE
	application_configuration 
SET
	`value`='1.44'
WHERE
	name='VERSION';
