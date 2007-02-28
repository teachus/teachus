ALTER TABLE 
	booking 
MODIFY 
	teacher_id BIGINT AFTER create_date;

UPDATE 
	booking, 
	person 
SET 
	booking.teacher_id=person.teacher_id 
WHERE 
	booking.booking_type='PUPIL' 
	AND 
	booking.pupil_id=person.id;
	