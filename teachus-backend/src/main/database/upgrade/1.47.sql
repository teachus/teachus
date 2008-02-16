-- Fix invalid messages so they will be sent
UPDATE
	message
SET
	state = 'SENT',
	processing_date = NOW()
WHERE
	state IS NULL;


-- Upgrade version
UPDATE
	application_configuration 
SET
	`value`='1.47'
WHERE
	name='VERSION';
