package dk.frankbille.teachus.dao.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.DistinctRootEntityResultTransformer;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import dk.frankbille.teachus.dao.BookingDAO;
import dk.frankbille.teachus.domain.Period;
import dk.frankbille.teachus.domain.Pupil;
import dk.frankbille.teachus.domain.PupilBooking;
import dk.frankbille.teachus.domain.PupilBookings;
import dk.frankbille.teachus.domain.Teacher;
import dk.frankbille.teachus.domain.impl.PupilBookingImpl;
import dk.frankbille.teachus.domain.impl.PupilBookingsImpl;

@Transactional(propagation=Propagation.REQUIRED)
public class BookingDAOHibernate extends HibernateDaoSupport implements BookingDAO {
	private static final long serialVersionUID = 1L;

	public void bookPupil(PupilBooking pupilBooking) {
		// Validate the booking
		Period period = pupilBooking.getPeriod();
		Date date = pupilBooking.getDate();
		
		if (period.hasDate(date) == false) {
			throw new IllegalArgumentException("The period can not be booked on this date");
		}
		
		getHibernateTemplate().save(pupilBooking);
	}

	@Transactional(readOnly=true)
	public PupilBooking createBookingObject() {
		return new PupilBookingImpl();
	}
	
	public void deleteBooking(PupilBooking booking) {
		getHibernateTemplate().delete(booking);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public PupilBookings getBookingsForDate(Period period, Date date) {
		DetachedCriteria c = DetachedCriteria.forClass(PupilBooking.class);
		
		DateTime start = new DateTime(date).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);
		DateTime end = new DateTime(date).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).withMillisOfSecond(999);
		
		c.add(Restrictions.eq("period", period));
		c.add(Restrictions.between("date", start.toDate(), end.toDate()));
		
		c.setResultTransformer(new DistinctRootEntityResultTransformer());
		
		List<PupilBooking> bookings = getHibernateTemplate().findByCriteria(c);
		
		return new PupilBookingsImpl(bookings);
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public PupilBookings getFutureBookingsForTeacher(Teacher teacher) {
		DetachedCriteria c = DetachedCriteria.forClass(PupilBooking.class);
		
		DateTime end = new DateTime().minusDays(1).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).withMillisOfSecond(999);
		
		c.createCriteria("pupil").add(Restrictions.eq("teacher", teacher));
		c.add(Restrictions.gt("date", end.toDate()));
		
		c.addOrder(Order.asc("date"));
		
		c.setResultTransformer(new DistinctRootEntityResultTransformer());
		
		List<PupilBooking> bookings = getHibernateTemplate().findByCriteria(c);
		
		return new PupilBookingsImpl(bookings);
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public PupilBookings getUnpaidBookings(Pupil pupil) {
		DetachedCriteria c = DetachedCriteria.forClass(PupilBookingImpl.class);
		
		c.add(Restrictions.eq("pupil", pupil));
		c.add(Restrictions.lt("date", new DateMidnight().toDate()));
		
		c.addOrder(Order.asc("date"));
		
		List<PupilBooking> result = getHibernateTemplate().findByCriteria(c);
		
		return new PupilBookingsImpl(result);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public PupilBookings getUnpaidBookings(Teacher teacher) {
		DetachedCriteria c = DetachedCriteria.forClass(PupilBookingImpl.class);
		
		c.createCriteria("pupil").add(Restrictions.eq("teacher", teacher));
		c.add(Restrictions.lt("date", new DateMidnight().toDate()));
		
		c.addOrder(Order.asc("date"));
		
		List<PupilBooking> result = getHibernateTemplate().findByCriteria(c);
		
		return new PupilBookingsImpl(result);
	}

}
