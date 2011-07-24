-- Drop timezone columns
ALTER TABLE
	booking
		DROP COLUMN date_tz,
		DROP COLUMN create_date_tz,
		DROP COLUMN update_date_tz;
		
ALTER TABLE
	period
		DROP COLUMN begin_date_tz,
		DROP COLUMN end_date_tz,
		DROP COLUMN start_time_tz,
		DROP COLUMN end_time_tz;

-- Upgrade version
UPDATE
	application_configuration 
SET
	`value`='1.57'
WHERE
	name='VERSION';
