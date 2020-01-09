package com.ysmind.common.persistence.dialect.db;

import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.MySQL5Dialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.StandardBasicTypes;

/**
 * http://blog.csdn.net/sd4000784/article/details/7693046
 * @author almt
 *
 */
public class MySQLLocalDialect extends Dialect {

	
	public MySQLLocalDialect() {
		super();
		//其中需要注意的是，在hibernate4版本中，使用StandardBasicTypes.STRING(来自包org.hibernate.type.StandardBasicTypes;)而不是Hibernate.STRING.
		registerFunction("convert_mine", new SQLFunctionTemplate(
				StandardBasicTypes.STRING, "convert(?1 using gbk)"));
	}

	@Override
	public String getTableTypeString() {
		return "engine=MyISAM";
	}
}
