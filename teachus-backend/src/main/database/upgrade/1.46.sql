-- ---------------------------------------------------------------------------
-- CONVERT SENT AND SENT_DATE TO MESSAGE_STATE AND PROCESSING_DATE
-- ---------------------------------------------------------------------------

ALTER TABLE
	message
		ADD COLUMN state VARCHAR(255) AFTER sent,
		CHANGE sent_date processing_date DATETIME;

UPDATE
	message
SET
	state = 'SENT'
WHERE
	sent = 1;

UPDATE
	message
SET
	state = 'FINAL'
WHERE
	sent = 0;

ALTER TABLE
	message
		DROP COLUMN sent;

-- ---------------------------------------------------------------------------
-- MERGE MESSAGE AND MESSAGE_RECIPIENT TABLES
-- ---------------------------------------------------------------------------

/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;

ALTER TABLE 
	message_recipient 
		DROP INDEX FK398E4FE149BE803A, 
		DROP FOREIGN KEY FK398E4FE149BE803A;

ALTER TABLE 
	message_recipient 
		DROP INDEX FK398E4FE1CAFD62F8, 
		DROP FOREIGN KEY FK398E4FE1CAFD62F8;

-- Add the recipient column to the message table
ALTER TABLE
	message
		ADD COLUMN recipient BIGINT AFTER sender, 
		ADD INDEX IDX_MESSAGE_RECIPIENT (recipient), 
		ADD CONSTRAINT FK_MESSAGE_RECIPIENT_PERSON FOREIGN KEY (recipient) REFERENCES person (id),
-- This column is temporary so we now what to clean up
		ADD COLUMN tmp TINYINT(1) DEFAULT 0;

UPDATE
	message
SET
	recipient = (SELECT person_id FROM message_recipient WHERE message.id=message_recipient.message_id LIMIT 1),
	tmp = 1;

INSERT INTO
	message
(
	message_type, create_date, subject, body, sender, recipient, sent, sent_date, mail_type
)
SELECT
	message_type, create_date, subject, body, sender, mr.person_id, sent, sent_date, mail_type
FROM
	message AS m
	INNER JOIN
	message_recipient AS mr ON m.id=mr.message_id;

DELETE FROM
	message
WHERE
	tmp = 1;

ALTER TABLE
	message
		DROP COLUMN tmp;

-- Drop the message_recipient table
DROP TABLE
	message_recipient;

/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;




-- Upgrade version
UPDATE
	application_configuration 
SET
	`value`='1.46'
WHERE
	name='VERSION';
