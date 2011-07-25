-- Upgrade version
UPDATE
	application_configuration 
SET
	`value`='1.58'
WHERE
	name='VERSION';
