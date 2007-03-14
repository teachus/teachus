ALTER TABLE
	period 
		ADD COLUMN repeat_every_week INTEGER NOT NULL;

UPDATE 
	period 
SET 
	repeat_every_week = 1;
