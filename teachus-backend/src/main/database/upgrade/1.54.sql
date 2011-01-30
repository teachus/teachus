-- Upgrade version
UPDATE
	application_configuration 
SET
	`value`='1.54'
WHERE
	name='VERSION';
