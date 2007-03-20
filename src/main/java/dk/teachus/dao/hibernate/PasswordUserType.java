package dk.teachus.dao.hibernate;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;
import org.springframework.util.ObjectUtils;

public class PasswordUserType implements UserType {
	private static final long serialVersionUID = 1L;

	public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException {
		String password = rs.getString(names[0]);
		return password;
	}

	public void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
		if (value instanceof String == false) {
			throw new HibernateException("Value must be instance of string");
		}
		
		String stringValue = (String) value;
		
		// Encrypt the value with md5
		try {
			String md5Value = sha1(stringValue);
			st.setString(index, md5Value);
		} catch (NoSuchAlgorithmException e) {
			throw new HibernateException(e);
		} catch (UnsupportedEncodingException e) {
			throw new HibernateException(e);
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
