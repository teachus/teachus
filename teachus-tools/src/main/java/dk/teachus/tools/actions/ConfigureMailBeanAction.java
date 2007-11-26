package dk.teachus.tools.actions;

import java.io.File;

public class ConfigureMailBeanAction extends AbstractConfigureApplicationContextAction {

	public ConfigureMailBeanAction(File projectDirectory) {
		super(projectDirectory);
	}

	@Override
	protected String doExecute(String applicationContext) {
		int beanStartIndex = applicationContext.indexOf("<bean id=\"mailBean\"");
		int beanEndIndex = applicationContext.indexOf("</bean>", beanStartIndex);
		beanEndIndex += "</bean>".length();
		
		String mailBean = applicationContext.substring(beanStartIndex, beanEndIndex);
		String dummyMailBean = mailBean.replace("impl.MailBeanImpl", "impl.DummyMailBean");
		dummyMailBean = dummyMailBean.replace("<constructor-arg ref=\"mailSender\" />", "");
		
		return applicationContext.substring(0, beanStartIndex)+dummyMailBean+applicationContext.substring(beanEndIndex);
	}

}
