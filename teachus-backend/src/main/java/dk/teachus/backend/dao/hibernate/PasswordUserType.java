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
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;
import org.springframework.util.ObjectUtils;

public class PasswordUserType implements UserType {
	private static final long serialVersionUID = 1L;
	
	private static final Pattern SHA1_PATTERN = Pattern.compile("^[0-9a-fA-F]{40}$");

	public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException {
		return rs.getString(names[0]);
	}

	public void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
		if (value != null) {
			if (value instanceof String == false) {
				throw new HibernateException("Value must be instance of string");
			}
			
			String stringValue = (String) value;
			
			// Encrypt the value with sha1
			try {
				// Bad way of figuring out if we should encrypt or not.
				// Please think of something smarter, but for now I guess it's ok
				Matcher matcher = SHA1_PATTERN.matcher(stringValue);
				if (matcher.matches()) {
					st.setString(index, stringValue);
				} else {
					String sha1Value = sha1(stringValue);
					st.setString(index, sha1Value);
				}
			} catch (NoSuchAlgorithmException e) {
				throw new HibernateException(e);
			} catch (UnsupportedEncodingException e) {
				throw new HibernateException(e);
			}
		} else {
			st.setNull(index, Types.VARCHAR);
		}
	}
	
	public static String hex(byte[] array) {
		StringBuffer sb = new StringBuffer();
		for (byte element : array) {
			sb.append(Integer.toHexString((element & 0xFF) | 0x100).toUpperCase().substring(1, 3));
		}
		return sb.toString();
	}

	public static String sha1(String message) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		return hex(md.digest(message.getBytes("CP1252")));
	}

	public Object assemble(Serializable cached, Object owner) throws HibernateException {
		return cached;
	}

	public Object deepCopy(Object value) throws HibernateException {
		return value;
	}

	public Serializable disassemble(Object value) throws HibernateException {
		return (Serializable) value;
	}

	public boolean equals(Object x, Object y) throws HibernateException {
		return ObjectUtils.nullSafeEquals(x, y);
	}

	public int hashCode(Object x) throws HibernateException {
		return ObjectUtils.nullSafeHashCode(x);
	}

	public boolean isMutable() {
		return false;
	}

	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		return target;
	}

	public Class returnedClass() {
		return String.class;
	}

	public int[] sqlTypes() {
		return new int[] {Types.VARCHAR};
	} 

}
