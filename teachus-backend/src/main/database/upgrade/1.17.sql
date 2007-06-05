ALTER TABLE
	period 
		ADD COLUMN active TINYINT DEFAULT TRUE;

UPDATE 
	period 
SET 
	active = 1;
