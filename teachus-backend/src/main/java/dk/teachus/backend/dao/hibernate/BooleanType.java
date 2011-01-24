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

import org.hibernate.dialect.Dialect;
import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.DiscriminatorType;
import org.hibernate.type.PrimitiveType;
import org.hibernate.type.descriptor.java.BooleanTypeDescriptor;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;
import org.hibernate.type.descriptor.sql.TinyIntTypeDescriptor;

/**
 * A type that maps between {@link java.sql.Types#TINYINT} and {@link Boolean}
 */
public class BooleanType
		extends AbstractSingleColumnStandardBasicType<Boolean>
		implements PrimitiveType<Boolean>, DiscriminatorType<Boolean> {
	private static final long serialVersionUID = 1L;
	
	public static final BooleanType INSTANCE = new BooleanType();

	public BooleanType() {
		this( TinyIntTypeDescriptor.INSTANCE, BooleanTypeDescriptor.INSTANCE );
	}

	protected BooleanType(SqlTypeDescriptor sqlTypeDescriptor, BooleanTypeDescriptor javaTypeDescriptor) {
		super( sqlTypeDescriptor, javaTypeDescriptor );
	}

	public String getName() {
		return "boolean";
	}

	@Override
	public String[] getRegistrationKeys() {
		return new String[] { getName(), boolean.class.getName(), Boolean.class.getName() };
	}

	public Class getPrimitiveClass() {
		return boolean.class;
	}

	public Serializable getDefaultValue() {
		return Boolean.FALSE;
	}

	public Boolean stringToObject(String string) {
		return fromString( string );
	}

	public String objectToSQLString(Boolean value, Dialect dialect) {
		return dialect.toBooleanValueString( value.booleanValue() );
	}
}
