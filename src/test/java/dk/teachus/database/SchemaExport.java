package dk.teachus.database;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;

public class SchemaExport {
	
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {
				"/dk/teachus/frontend/applicationContext.xml",
				"/dk/teachus/database/applicationContext-schemaExport.xml",
		});

		LocalSessionFactoryBean sessionFactory = (LocalSessionFactoryBean) context.getBean("&sessionFactory");

		org.hibernate.tool.hbm2ddl.SchemaExport export = new org.hibernate.tool.hbm2ddl.SchemaExport(sessionFactory.getConfiguration());
		export.setOutputFile("target/schema.sql");
		export.execute(false,false,false,true);
	}
	
}
