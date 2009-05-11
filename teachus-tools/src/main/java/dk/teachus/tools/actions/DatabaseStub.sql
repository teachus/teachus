-- Upgrade version
UPDATE
	application_configuration 
SET
	`value`='{THEVERSION}'
WHERE
	name='VERSION';
