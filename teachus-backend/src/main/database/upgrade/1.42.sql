-- Upgrade version
UPDATE
	application_configuration 
SET
	`value`='1.42'
WHERE
	name='VERSION';
