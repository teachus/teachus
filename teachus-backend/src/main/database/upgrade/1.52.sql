-- Upgrade version
UPDATE
	application_configuration 
SET
	`value`='1.52'
WHERE
	name='VERSION';
