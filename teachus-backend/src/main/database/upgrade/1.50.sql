-- Add timezone information to the booking table

ALTER TABLE
	booking
		ADD COLUMN date_tz VARCHAR(255) AFTER date,
		ADD COLUMN create_date_tz VARCHAR(255) AFTER create_date,
		ADD COLUMN update_date_tz VARCHAR(255) AFTER update_date;

-- Assume that the current customers is in Denmark
UPDATE
	booking
SET
	date_tz = 'Europe/Copenhagen'
WHERE
	date IS NOT NULL;

UPDATE
	booking
SET
	create_date_tz = 'Europe/Copenhagen'
WHERE
	create_date IS NOT NULL;

UPDATE
	booking
SET
	update_date_tz = 'Europe/Copenhagen'
WHERE
	update_date IS NOT NULL;

-- Upgrade version
UPDATE
	application_configuration 
SET
	`value`='1.50'
WHERE
	name='VERSION';
