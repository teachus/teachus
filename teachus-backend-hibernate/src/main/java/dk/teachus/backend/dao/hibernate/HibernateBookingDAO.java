/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dk.teachus.backend.dao.hibernate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.DistinctRootEntityResultTransformer;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import dk.teachus.backend.dao.BookingDAO;
import dk.teachus.backend.domain.Booking;
import dk.teachus.backend.domain.Bookings;
import dk.teachus.backend.domain.Period;
import dk.teachus.backend.domain.Period.Status;
import dk.teachus.backend.domain.Pupil;
import dk.teachus.backend.domain.PupilBooking;
import dk.teachus.backend.domain.Teacher;
import dk.teachus.backend.domain.TeacherBooking;
import dk.teachus.backend.domain.impl.BookingImpl;
import dk.teachus.backend.domain.impl.BookingsImpl;
import dk.teachus.backend.domain.impl.PupilBookingImpl;
import dk.teachus.backend.domain.impl.TeacherBookingImpl;

@Transactional(propagation=Propagation.MANDATORY)
public class HibernateBookingDAO extends HibernateDaoSupport implements BookingDAO {
	private static final long serialVersionUID = 1L;

	public void saveBooking(Booking booking) {
		getHibernateTemplate().saveOrUpdate(booking);
		getHibernateTemplate().flush();
	}
	
	@Transactional(readOnly=true)
	public TeacherBooking createTeacherBookingObject() {
		return new TeacherBookingImpl();
	}
	
	public void deleteBooking(Booking booking) {
		booking.setActive(false);
		// TODO Can we get the real timezone somehow?
		booking.setUpdateDate(new DateTime());
		
		getHibernateTemplate().update(booking);
		getHibernateTemplate().flush();
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public List<PupilBooking> getFutureBookingsForTeacher(Teacher teacher) {
		DetachedCriteria c = DetachedCriteria.forClass(PupilBookingImpl.class);
		
		DateTime end = new DateTime().minusDays(1).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).withMillisOfSecond(999);
		
		c.createCriteria("period")
			.add(Restrictions.eq("status", Status.FINAL));
		c.createCriteria("pupil")
			.add(Restrictions.eq("teacher", teacher))
			.add(Restrictions.eq("active", true))
			.createCriteria("teacher")
				.add(Restrictions.eq("active", true));
		c.add(Restrictions.gt("date", end));
		c.add(Restrictions.eq("active", true));
		
		c.addOrder(Order.asc("date"));
		
		c.setResultTransformer(DistinctRootEntityResultTransformer.INSTANCE);
		
		return getHibernateTemplate().findByCriteria(c);
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public List<PupilBooking> getUnpaidBookings(Pupil pupil) {
		DetachedCriteria c = DetachedCriteria.forClass(PupilBookingImpl.class);

		c.createCriteria("period").add(Restrictions.eq("status", Status.FINAL));
		c.createCriteria("pupil")
			.add(Restrictions.eq("active", true))
			.createCriteria("teacher")
				.add(Restrictions.eq("active", true));
		c.add(Restrictions.eq("pupil", pupil));
		c.add(Restrictions.lt("date", new DateTime()));
		c.add(Restrictions.eq("paid", false));
		c.add(Restrictions.eq("active", true));
		
		c.addOrder(Order.asc("date"));
		
		return getHibernateTemplate().findByCriteria(c);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public List<PupilBooking> getUnpaidBookings(Teacher teacher) {
		DetachedCriteria c = DetachedCriteria.forClass(PupilBookingImpl.class);
		
		c.createCriteria("period")
			.add(Restrictions.eq("status", Status.FINAL));
		c.createCriteria("pupil")
			.add(Restrictions.eq("teacher", teacher))
			.add(Restrictions.eq("active", true))
			.createCriteria("teacher")
				.add(Restrictions.eq("active", true));
		c.add(Restrictions.lt("date", new DateTime()));
		c.add(Restrictions.eq("paid", false));
		c.add(Restrictions.eq("active", true));
		
		c.addOrder(Order.asc("date"));
		
		return getHibernateTemplate().findByCriteria(c);
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public List<PupilBooking> getTeacherNotificationBookings(Teacher teacher) {
		DetachedCriteria c = DetachedCriteria.forClass(PupilBookingImpl.class);
		
		c.createCriteria("period")
			.add(Restrictions.eq("status", Status.FINAL));
		c.createCriteria("pupil")
			.add(Restrictions.eq("teacher", teacher))
			.add(Restrictions.eq("active", true))
			.createCriteria("teacher")
				.add(Restrictions.eq("active", true));
		c.add(Restrictions.eq("notificationSent", false));
		c.add(Restrictions.lt("createDate", new DateTime().minusMinutes(15)));
		c.add(Restrictions.eq("active", true));
		
		return getHibernateTemplate().findByCriteria(c);
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public Map<Pupil, List<PupilBooking>> getPupilNotificationBookings() {
		Map<Pupil, List<PupilBooking>> pupilNotificationBookings = new HashMap<Pupil, List<PupilBooking>>();
		
		DetachedCriteria c = DetachedCriteria.forClass(PupilBookingImpl.class);
		
		c.createCriteria("period").add(Restrictions.eq("status", Status.FINAL));
		c.createCriteria("pupil").add(Restrictions.eq("active", true)).createCriteria("teacher").add(Restrictions.eq("active", true));
		c.add(Restrictions.eq("pupilNotificationSent", false));
		c.add(Restrictions.lt("createDate", new DateTime().minusMinutes(15)));
		c.add(Restrictions.eq("active", true));
		
		List<PupilBooking> bookings = getHibernateTemplate().findByCriteria(c);
		
		for (PupilBooking pupilBooking : bookings) {
			List<PupilBooking> pupilBookings = pupilNotificationBookings.get(pupilBooking.getPupil());
			if (pupilBookings == null) {
				pupilBookings = new ArrayList<PupilBooking>();
				pupilNotificationBookings.put(pupilBooking.getPupil(), pupilBookings);
			}
			
			pupilBookings.add(pupilBooking);
		}
		
		return pupilNotificationBookings;
	}
	
	public void teacherNotificationMailSent(List<PupilBooking> pupilBookings) {
		if (pupilBookings.isEmpty() == false) {
			StringBuilder hql = new StringBuilder();
			
			hql.append("UPDATE PupilBookingImpl SET notificationSent=1, updateDate=NOW() WHERE active = 1 AND id IN(");
			
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
	
	public void pupilNotificationMailSent(Map<Pupil, List<PupilBooking>> pupilBookings) {
		if (pupilBookings.isEmpty() == false) {
			StringBuilder hql = new StringBuilder();
			
			hql.append("UPDATE PupilBookingImpl SET pupilNotificationSent=1, updateDate=NOW() WHERE active = 1 AND id IN(");
			
			String sep = "";
			for (List<PupilBooking> pupilBookingList : pupilBookings.values()) {
				for (PupilBooking pupilBooking : pupilBookingList) {
					hql.append(sep);
					hql.append(pupilBooking.getId());
					sep = ",";
				}
			}
			
			hql.append(")");
			
			getHibernateTemplate().bulkUpdate(hql.toString());
			getHibernateTemplate().flush();
		}
	}
	
	public void changePaidStatus(PupilBooking pupilBooking) {
		pupilBooking.setPaid(pupilBooking.isPaid() == false);
		// TODO Can we get the real timezone somehow?
		pupilBooking.setUpdateDate(new DateTime());
		
		getHibernateTemplate().update(pupilBooking);
		getHibernateTemplate().flush();
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public List<PupilBooking> getPaidBookings(Teacher teacher, DateMidnight startDate, DateMidnight endDate) {
		DetachedCriteria c = DetachedCriteria.forClass(PupilBookingImpl.class);
		
		c.createCriteria("period")
			.add(Restrictions.eq("status", Status.FINAL));
		c.createCriteria("pupil")
			.add(Restrictions.eq("teacher", teacher))
			.add(Restrictions.eq("active", true))
			.createCriteria("teacher")
				.add(Restrictions.eq("active", true));
		c.add(Restrictions.eq("paid", true));
		c.add(Restrictions.eq("active", true));
		
		if (startDate != null && endDate != null) {
			c.add(Restrictions.between("date", startDate.toDateTime(), endDate.toDateTime()));
		} else if (startDate != null) {
			c.add(Restrictions.gt("date", startDate.toDateTime()));
		} else if (endDate != null) {
			c.add(Restrictions.lt("date", endDate.toDateTime()));
		}
		
		c.addOrder(Order.asc("date"));
		
		return getHibernateTemplate().findByCriteria(c);
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public List<PupilBooking> getUnPaidBookings(Teacher teacher, DateMidnight startDate, DateMidnight endDate) {
		DetachedCriteria c = DetachedCriteria.forClass(PupilBookingImpl.class);
		
		c.createCriteria("period")
			.add(Restrictions.eq("status", Status.FINAL));
		c.createCriteria("pupil")
			.add(Restrictions.eq("teacher", teacher))
			.add(Restrictions.eq("active", true))
			.createCriteria("teacher")
				.add(Restrictions.eq("active", true));
		c.add(Restrictions.eq("paid", false));
		c.add(Restrictions.eq("active", true));
		
		if (startDate != null && endDate != null) {
			c.add(Restrictions.between("date", startDate.toDateTime(), endDate.toDateTime()));
		} else if (startDate != null) {
			c.add(Restrictions.gt("date", startDate.toDateTime()));
		} else if (endDate != null) {
			c.add(Restrictions.lt("date", endDate.toDateTime()));
		}
		
		c.addOrder(Order.asc("date"));
		
		return getHibernateTemplate().findByCriteria(c);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public List<Integer> getYearsWithPaidBookings(Teacher teacher) {
		List<Integer> years = getHibernateTemplate().find("SELECT year(b.date.date) AS theYear FROM PupilBookingImpl b JOIN b.pupil p JOIN b.period pe JOIN p.teacher t WHERE b.active = 1 AND p.teacher = ? AND p.active = 1 AND b.paid = 1 AND pe.status = ? AND t.active = 1 GROUP BY year(b.date)", new Object[] {
				teacher,
				Status.FINAL
		});
		
		return years;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public List<Integer> getYearsWithBookings(Teacher teacher) {
		List<Integer> years = getHibernateTemplate().find("SELECT year(b.date.date) AS theYear FROM PupilBookingImpl b JOIN b.pupil p JOIN b.period pe JOIN p.teacher t WHERE b.active = 1 AND p.teacher = ? AND p.active = 1 AND pe.status = ? AND t.active = 1 GROUP BY year(b.date)", new Object[] {
				teacher,
				Status.FINAL
		});
		
		return years;
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public Bookings getAllBookings(Teacher teacher) {
		DetachedCriteria c = DetachedCriteria.forClass(BookingImpl.class);
		
		c.createCriteria("period").add(Restrictions.eq("status", Status.FINAL));
		c.createCriteria("teacher").add(Restrictions.eq("active", true));
		c.add(Restrictions.eq("teacher", teacher));
		c.add(Restrictions.eq("active", true));
		
		c.setResultTransformer(DistinctRootEntityResultTransformer.INSTANCE);
		
		List<Booking> bookings = getHibernateTemplate().findByCriteria(c);
		List<Booking> filteredBookings = filterBookings(bookings);
		
		return new BookingsImpl(filteredBookings);
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public Bookings getBookings(Teacher teacher, DateMidnight fromDate, DateMidnight toDate) {
		DetachedCriteria c = DetachedCriteria.forClass(BookingImpl.class);
		
		DateTime start = fromDate.toDateTime().withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);
		DateTime end = toDate.toDateTime().withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).withMillisOfSecond(999);
		
		c.createCriteria("period").add(Restrictions.eq("status", Status.FINAL));
		c.createCriteria("teacher").add(Restrictions.eq("active", true));
		c.add(Restrictions.eq("teacher", teacher));
		c.add(Restrictions.between("date", start, end));
		c.add(Restrictions.eq("active", true));
		
		c.setResultTransformer(DistinctRootEntityResultTransformer.INSTANCE);
		
		List<Booking> bookings = getHibernateTemplate().findByCriteria(c);
		List<Booking> filteredBookings = filterBookings(bookings);
		
		return new BookingsImpl(filteredBookings);
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public Bookings getBookings(Pupil pupil, DateMidnight fromDate, DateMidnight toDate) {
		DetachedCriteria c = DetachedCriteria.forClass(BookingImpl.class);
		
		DateTime start = new DateTime(fromDate).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);
		DateTime end = new DateTime(toDate).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).withMillisOfSecond(999);
		
		c.createCriteria("period").add(Restrictions.eq("status", Status.FINAL));
		c.createCriteria("teacher").add(Restrictions.eq("active", true));
		c.add(Restrictions.eq("pupil", pupil));
		c.add(Restrictions.between("date", start, end));
		c.add(Restrictions.eq("active", true));
		
		c.setResultTransformer(DistinctRootEntityResultTransformer.INSTANCE);
		
		List<Booking> bookings = getHibernateTemplate().findByCriteria(c);
		List<Booking> filteredBookings = filterBookings(bookings);
		
		return new BookingsImpl(filteredBookings);
	}
	
	@Transactional(readOnly=true)
	public Booking getBooking(Long id) {
		return (Booking) getHibernateTemplate().get(BookingImpl.class, id);
	}
	
	@Transactional(readOnly=true)
	public int getBookingCount(Period period) {
		DetachedCriteria c = DetachedCriteria.forClass(BookingImpl.class);
		c.add(Restrictions.eq("period", period));
		c.setProjection(Projections.count("id"));
		
		int count = getCount(c);
		
		return count;
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public DateTime getLastBookingDate(Period period) {
		DetachedCriteria c = DetachedCriteria.forClass(BookingImpl.class);
		c.createCriteria("pupil").add(Restrictions.eq("active", true));
		c.add(Restrictions.eq("period", period));
		c.add(Restrictions.eq("active", true));
		c.addOrder(Order.desc("date"));
		
		List<Booking> list = getHibernateTemplate().findByCriteria(c, 0, 1);
		
		DateTime lastBookingDate = null;
		if (list.size() == 1) {
			Booking booking = list.get(0);
			lastBookingDate = booking.getDate();
		}
		
		return lastBookingDate;
	}

	@SuppressWarnings("unchecked")
	private int getCount(DetachedCriteria c) {
		List<Object> result = getHibernateTemplate().findByCriteria(c);
		if (result.size() != 1) {
			throw new IllegalStateException("Something weird going on");
		}
		int count = (Integer) result.get(0);
		return count;
	}

	private List<Booking> filterBookings(List<Booking> bookings) {
		List<Booking> filteredBookings = new ArrayList<Booking>();
		for (Booking booking : bookings) {
			if (booking instanceof PupilBooking) {
				PupilBooking pupilBooking = (PupilBooking) booking;
				if (pupilBooking.getPupil().isActive()) {
					filteredBookings.add(booking);
				}
			} else {
				filteredBookings.add(booking);
			}
		}
		return filteredBookings;
	}
}
