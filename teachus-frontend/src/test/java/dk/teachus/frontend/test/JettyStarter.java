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
package dk.teachus.frontend.test;

import org.eclipse.jetty.plus.jndi.Resource;
import org.eclipse.jetty.plus.webapp.EnvConfiguration;
import org.eclipse.jetty.plus.webapp.PlusConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.FragmentConfiguration;
import org.eclipse.jetty.webapp.JettyWebXmlConfiguration;
import org.eclipse.jetty.webapp.MetaInfConfiguration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebInfConfiguration;
import org.eclipse.jetty.webapp.WebXmlConfiguration;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;


public class JettyStarter {

	public static void main(String[] args) throws Exception {
		
		System.setProperty("wicket.configuration", "development");
		
		Server server = new Server(8080);
		
		WebAppContext webAppContext = new WebAppContext("src/main/webapp", "/");
		webAppContext.setConfigurationClasses(new String[]{
				WebInfConfiguration.class.getName(),
				WebXmlConfiguration.class.getName(),
				MetaInfConfiguration.class.getName(),
				FragmentConfiguration.class.getName(),
				EnvConfiguration.class.getName(),
				PlusConfiguration.class.getName(),
				JettyWebXmlConfiguration.class.getName()
		});
		
		webAppContext.setInitParameter("doDynamicDataImport", "true");
		
		MysqlConnectionPoolDataSource dataSource = new MysqlConnectionPoolDataSource();
		dataSource.setUrl("jdbc:mysql://localhost/teachus");
		dataSource.setUser("root");
		dataSource.setPassword("");
		new Resource("jdbc/teachus", dataSource);
		
		server.setHandler(webAppContext);
		
		server.start();
		server.join();
	}
	
}
