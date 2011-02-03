-- Upgrade version
UPDATE
	application_configuration 
SET
	`value`='1.55'
WHERE
	name='VERSION';
