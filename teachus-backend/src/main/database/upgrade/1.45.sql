-- Upgrade version
UPDATE
	application_configuration 
SET
	`value`='1.45'
WHERE
	name='VERSION';
