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

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.dialect.Dialect;
import org.hibernate.type.DiscriminatorType;
import org.hibernate.type.PrimitiveType;

public class BooleanType extends PrimitiveType implements DiscriminatorType {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Class<Boolean> getPrimitiveClass() {
		return boolean.class;
	}

	@Override
	public Serializable getDefaultValue() {
		return Boolean.FALSE;
	}

	@Override
	public Boolean get(ResultSet rs, String name) throws HibernateException,
			SQLException {
		return rs.getInt(name) == 1 ? Boolean.TRUE : Boolean.FALSE;
	}

	@Override
	public void set(PreparedStatement st, Object value, int index)
			throws HibernateException, SQLException {
		Boolean bool = (Boolean) value;
		
		st.setInt( index, (bool.booleanValue() ? 1 : 0));
	}

	@Override
	public int sqlType() {
		return Types.TINYINT;
	}

	@Override
	public Object fromStringValue(String xml) throws HibernateException {
		return Boolean.valueOf(xml);
	}

	public String objectToSQLString(Object value, Dialect dialect)
			throws Exception {
		return ((Boolean) value) ? ""+1 : ""+0;
	}

	public Class<Boolean> getReturnedClass() {
		return Boolean.class;
	}

	public String getName() {
		return "boolean";
	}

	public Object stringToObject(String xml) throws Exception {
		return fromStringValue(xml);
	}
}
