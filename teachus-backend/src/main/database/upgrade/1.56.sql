-- Upgrade version
UPDATE
	application_configuration 
SET
	`value`='1.56'
WHERE
	name='VERSION';
