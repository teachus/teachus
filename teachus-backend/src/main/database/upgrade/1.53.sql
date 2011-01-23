-- Upgrade version
UPDATE
	application_configuration 
SET
	`value`='1.53'
WHERE
	name='VERSION';
