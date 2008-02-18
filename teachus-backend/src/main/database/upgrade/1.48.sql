-- Fix invalid messages so they will be sent
ALTER TABLE
	person
		ADD COLUMN notes MEDIUMTEXT AFTER teacher_id;


-- Upgrade version
UPDATE
	application_configuration 
SET
	`value`='1.48'
WHERE
	name='VERSION';
