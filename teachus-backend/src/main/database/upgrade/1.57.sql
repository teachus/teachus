-- Upgrade version
UPDATE
	application_configuration 
SET
	`value`='1.57'
WHERE
	name='VERSION';
