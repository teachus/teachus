package dk.teachus.backend.db;

import liquibase.database.structure.type.CustomType;
import liquibase.database.structure.type.DataType;
import liquibase.database.typeconversion.core.MySQLTypeConverter;

public class TeachUsMySQLTypeConverter extends MySQLTypeConverter {
	
	public TeachUsMySQLTypeConverter() {
	}
	
	@Override
	public DataType getDataType(String columnTypeString, Boolean autoIncrement) {
		if (columnTypeString.startsWith("[") && columnTypeString.endsWith("]")) {
			return new CustomType(columnTypeString.substring(1, columnTypeString.length()-1), 0, 2);
		} else {
			return super.getDataType(columnTypeString, autoIncrement);
		}
	}
	
}
