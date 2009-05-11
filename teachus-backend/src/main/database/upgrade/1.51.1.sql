-- Upgrade version
UPDATE
	application_configuration 
SET
	`value`='1.51.1'
WHERE
	name='VERSION';
