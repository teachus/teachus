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
import dk.frankbille.teachus.domain.Booking;
import dk.frankbille.teachus.domain.Bookings;
import dk.frankbille.teachus.domain.Period;
import dk.frankbille.teachus.domain.Pupil;
import dk.frankbille.teachus.domain.PupilBooking;
import dk.frankbille.teachus.domain.Teacher;
import dk.frankbille.teachus.domain.TeacherBooking;
import dk.frankbille.teachus.domain.impl.BookingImpl;
import dk.frankbille.teachus.domain.impl.BookingsImpl;
import dk.frankbille.teachus.domain.impl.PupilBookingImpl;
import dk.frankbille.teachus.domain.impl.TeacherBookingImpl;

@Transactional(propagation=Propagation.REQUIRED)
public class BookingDAOHibernate extends HibernateDaoSupport implements BookingDAO {
	private static final long serialVersionUID = 1L;

	public void book(Booking booking) {
		// Validate the booking
		Period period = booking.getPeriod();
		Date date = booking.getDate();
		
		if (period.hasDate(date) == false) {
			throw new IllegalArgumentException("The period can not be booked on this date");
		}
		
		getHibernateTemplate().save(booking);
	}

	@Transactional(readOnly=true)
	public PupilBooking createPupilBookingObject() {
		return new PupilBookingImpl();
	}
	
	@Transactional(readOnly=true)
	public TeacherBooking createTeacherBookingObject() {
		return new TeacherBookingImpl();
	}
	
	public void deleteBooking(Booking booking) {
		getHibernateTemplate().delete(booking);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public Bookings getBookingsForDate(Period period, Date date) {
		DetachedCriteria c = DetachedCriteria.forClass(BookingImpl.class);
		
		DateTime start = new DateTime(date).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);
		DateTime end = new DateTime(date).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).withMillisOfSecond(999);
		
		c.add(Restrictions.eq("period", period));
		c.add(Restrictions.between("date", start.toDate(), end.toDate()));
		
		c.setResultTransformer(new DistinctRootEntityResultTransformer());
		
		List<Booking> bookings = getHibernateTemplate().findByCriteria(c);
		
		return new BookingsImpl(bookings);
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public List<PupilBooking> getFutureBookingsForTeacher(Teacher teacher) {
		DetachedCriteria c = DetachedCriteria.forClass(PupilBookingImpl.class);
		
		DateTime end = new DateTime().minusDays(1).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).withMillisOfSecond(999);
		
		c.createCriteria("pupil").add(Restrictions.eq("teacher", teacher));
		c.add(Restrictions.gt("date", end.toDate()));
		
		c.addOrder(Order.asc("date"));
		
		c.setResultTransformer(new DistinctRootEntityResultTransformer());
		
		return getHibernateTemplate().findByCriteria(c);
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public List<PupilBooking> getUnpaidBookings(Pupil pupil) {
		DetachedCriteria c = DetachedCriteria.forClass(PupilBookingImpl.class);
		
		c.add(Restrictions.eq("pupil", pupil));
		c.add(Restrictions.lt("date", new DateMidnight().toDate()));
		
		c.addOrder(Order.asc("date"));
		
		return getHibernateTemplate().findByCriteria(c);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public List<PupilBooking> getUnpaidBookings(Teacher teacher) {
		DetachedCriteria c = DetachedCriteria.forClass(PupilBookingImpl.class);
		
		c.createCriteria("pupil").add(Restrictions.eq("teacher", teacher));
		c.add(Restrictions.lt("date", new DateMidnight().toDate()));
		
		c.addOrder(Order.asc("date"));
		
		return getHibernateTemplate().findByCriteria(c);
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public List<PupilBooking> getUnsentBookings(Teacher teacher) {
		DetachedCriteria c = DetachedCriteria.forClass(PupilBookingImpl.class);
		
		c.createCriteria("pupil").add(Restrictions.eq("teacher", teacher));
		c.add(Restrictions.eq("notificationSent", false));
		c.add(Restrictions.lt("createDate", new DateTime().minusHours(1).toDate()));
		
		return getHibernateTemplate().findByCriteria(c);
	}
	
	public void newBookingsMailSent(List<PupilBooking> pupilBookings) {
		if (pupilBookings.isEmpty() == false) {
			StringBuilder hql = new StringBuilder();
			
			hql.append("UPDATE PupilBookingImpl SET notificationSent=true WHERE id IN(");
			
			String sep = "";
			for (PupilBooking pupilBooking : pupilBookings) {
				hql.append(sep);
				hql.append(pupilBooking.getId());
				sep = ",";
			}
			
			hql.append(")");
			
			getHibernateTemplate().bulkUpdate(hql.toString());
			getHibernateTemplate().flush();
		}
	}

}
