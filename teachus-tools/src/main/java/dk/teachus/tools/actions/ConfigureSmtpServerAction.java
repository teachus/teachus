package dk.teachus.tools.actions;

import java.io.File;
import java.util.Properties;

import dk.teachus.tools.config.SmtpServerNode;

public class ConfigureSmtpServerAction extends
		AbstractConfigureTeachUsPropertiesAction {

	private final SmtpServerNode smtpServer;

	public ConfigureSmtpServerAction(File projectDirectory, SmtpServerNode smtpServer) {
		super(projectDirectory);
		this.smtpServer = smtpServer;
	}

	@Override
	protected void configureProperties(Properties properties) {
		properties.put("mail.smtp", smtpServer.getSmtpServer());
	}

}
