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
