ALTER TABLE
	booking 
		ADD COLUMN active TINYINT AFTER version;

UPDATE
	booking
SET
	active = 1;
		