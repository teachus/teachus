-- Upgrade version
UPDATE
	application_configuration 
SET
	`value`='1.49'
WHERE
	name='VERSION';
