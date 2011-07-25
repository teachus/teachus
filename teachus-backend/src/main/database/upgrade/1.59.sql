-- Upgrade version
UPDATE
	application_configuration 
SET
	`value`='1.59'
WHERE
	name='VERSION';
