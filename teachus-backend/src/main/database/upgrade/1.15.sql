ALTER TABLE
	period 
		ADD COLUMN interval_between_lesson_start INTEGER NOT NULL;

UPDATE 
	period 
SET 
	interval_between_lesson_start = 60;
