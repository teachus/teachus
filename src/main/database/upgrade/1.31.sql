-- Active
ALTER TABLE
	booking 
		ADD COLUMN active TINYINT AFTER version;

UPDATE
	booking
SET
	active = 1;
	
-- Update date
ALTER TABLE
	booking 
		ADD COLUMN update_date DATETIME AFTER create_date;
		