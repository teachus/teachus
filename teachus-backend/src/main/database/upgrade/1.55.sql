-- Rename NewCalendarNarrowTimesTeacherAttribute to CalendarNarrowTimesTeacherAttribute
UPDATE
	teacher_attribute
SET
	attribute = 'CALENDARNARROWTIMES'
WHERE
	attribute = 'NEWCALENDARNARROWTIMES';


-- Upgrade version
UPDATE
	application_configuration 
SET
	`value`='1.55'
WHERE
	name='VERSION';
