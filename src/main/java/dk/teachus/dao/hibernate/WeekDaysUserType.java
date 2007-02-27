package dk.teachus.dao.hibernate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.hibernate.HibernateException;
import org.hibernate.type.ImmutableType;

import dk.teachus.domain.impl.PeriodImpl.WeekDay;

public class WeekDaysUserType extends ImmutableType {
	private static final long serialVersionUID = 1L;

	@Override
	public Object fromStringValue(String xml) throws HibernateException {
		return null;
	}

	@Override
	public Object get(ResultSet rs, String name) throws HibernateException, SQLException {
		String wdString = rs.getString(name);
		List<WeekDay> weekDays = null;
		
		if (wdString != null && wdString.length() > 0) {
			weekDays = new ArrayList<WeekDay>();
			
			StringTokenizer stringTokenizer = new StringTokenizer(wdString, ",");
			while(stringTokenizer.hasMoreTokens()) {
				String token = stringTokenizer.nextToken();
				weekDays.add(WeekDay.parse(token));
			}
		}
		
		return weekDays;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void set(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
		StringBuilder sb = new StringBuilder();
		
		if (value != null) {
			List<WeekDay> weekDays = (List<WeekDay>) value;
			for (WeekDay day : weekDays) {
				if (sb.length() > 0) {
					sb.append(",");
				}
				sb.append(day.getYodaWeekDay());
			}
		}
		
		st.setString(index, sb.toString());
	}

	@Override
	public int sqlType() {
		return Types.VARCHAR;
	}

	@Override
	public String toString(Object value) throws HibernateException {
		return null;
	}

	public String getName() {
		return "WeekDay user type";
	}

	public Class getReturnedClass() {
		return ArrayList.class;
	}

}
