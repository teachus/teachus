ALTER TABLE
	period 
		ADD COLUMN lesson_duration INTEGER NOT NULL;

UPDATE 
	period 
SET 
	lesson_duration = 60;
