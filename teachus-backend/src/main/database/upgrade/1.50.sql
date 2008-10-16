-- Add timezone information to the booking and period table

ALTER TABLE
	booking
		ADD COLUMN date_tz VARCHAR(255) AFTER date,
		ADD COLUMN create_date_tz VARCHAR(255) AFTER create_date,
		ADD COLUMN update_date_tz VARCHAR(255) AFTER update_date;

ALTER TABLE
	period
		ADD COLUMN begin_date_tz VARCHAR(255) AFTER begin_date,
		ADD COLUMN end_date_tz VARCHAR(255) AFTER end_date,
		ADD COLUMN start_time_tz VARCHAR(255) AFTER start_time,
		ADD COLUMN end_time_tz VARCHAR(255) AFTER end_time;

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

UPDATE
	period
SET
	begin_date_tz = 'Europe/Copenhagen'
WHERE
	begin_date IS NOT NULL;

UPDATE
	period
SET
	end_date_tz = 'Europe/Copenhagen'
WHERE
	end_date IS NOT NULL;

UPDATE
	period
SET
	start_time_tz = 'Europe/Copenhagen'
WHERE
	start_time IS NOT NULL;

UPDATE
	period
SET
	end_time_tz = 'Europe/Copenhagen'
WHERE
	end_time IS NOT NULL;

INSERT INTO
	teacher_attribute
	(
		attribute,
		version,
		teacher_id,
		value
	)
VALUES
	(
		'TIMEZONE',
		0,
		2,
		'Europe/Copenhagen'
	);

INSERT INTO
	application_configuration
	(
		name,
		version,
		value
	)
VALUES
	(
		'DEFAULT_TIMEZONE',
		0,
		'Europe/Copenhagen'
	);

-- Upgrade version
UPDATE
	application_configuration 
SET
	`value`='1.50'
WHERE
	name='VERSION';
