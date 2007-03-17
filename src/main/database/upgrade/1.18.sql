ALTER TABLE 
	person 
MODIFY 
	password VARCHAR(40);

UPDATE
	person
SET
	password = SHA1(password);
	
UPDATE
	person
SET
	password = UPPER(password);
