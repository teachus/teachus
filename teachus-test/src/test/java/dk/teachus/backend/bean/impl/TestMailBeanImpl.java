package dk.teachus.backend.bean.impl;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.List;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessagePreparator;

import dk.teachus.backend.bean.MailBean;
import dk.teachus.backend.bean.VelocityBean;
import dk.teachus.backend.domain.Pupil;
import dk.teachus.backend.domain.PupilBooking;
import dk.teachus.backend.domain.Teacher;
import dk.teachus.backend.test.SpringTestCase;

public class TestMailBeanImpl extends SpringTestCase {
	private static final long serialVersionUID = 1L;

	public void testSendWelcomeMail() throws Exception {
		MailBean mailBean = getMailBean();
		
		Pupil pupil = (Pupil) getPersonDAO().getPerson(3L);
		endTransaction();
		
		Method createWelcomeMailMethod = MailBeanImpl.class.getDeclaredMethod("createWelcomeMail", new Class[] {Pupil.class, String.class, String.class});
		createWelcomeMailMethod.setAccessible(true);
		MimeMessagePreparator preparator = (MimeMessagePreparator) createWelcomeMailMethod.invoke(mailBean, new Object[] {pupil, "No intro message", "http://www.teachus.dk/"});
		preparator.prepare(new JavaMailSenderImpl().createMimeMessage());
	}
	
	public void testSendNewBookingsMail() throws Exception {
		MailBean mailBean = getMailBean();
		
		Teacher teacher = (Teacher) getPersonDAO().getPerson(2L);
		endTransaction();
		
		List<PupilBooking> pupilBookings = getBookingDAO().getUnpaidBookings(teacher);
		endTransaction();
		
		mailBean.sendNewBookingsMail(teacher, pupilBookings);
	}

	private MailBean getMailBean() throws NoSuchFieldException, IllegalAccessException {
		// Replace mailsender with a dummy
		JavaMailSender mailSender = new JavaMailSender() {
			private JavaMailSender wrappedSender = new JavaMailSenderImpl();

			public MimeMessage createMimeMessage() {
				return wrappedSender.createMimeMessage();
			}

			public MimeMessage createMimeMessage(InputStream arg0) throws MailException {
				return wrappedSender.createMimeMessage(arg0);
			}

			public void send(MimeMessage arg0) throws MailException {				
			}

			public void send(MimeMessage[] arg0) throws MailException {				
			}

			public void send(MimeMessagePreparator arg0) throws MailException {		
				try {
					arg0.prepare(createMimeMessage());
				} catch (Exception e) {
					throw new MailException("", e) {
						private static final long serialVersionUID = 1L;
					};
				}
			}

			public void send(MimeMessagePreparator[] arg0) throws MailException {				
			}

			public void send(SimpleMailMessage arg0) throws MailException {				
			}

			public void send(SimpleMailMessage[] arg0) throws MailException {				
			}			
		};

		VelocityBean velocityBean = (VelocityBean) applicationContext.getBean("velocityBean");
		return new MailBeanImpl(mailSender, velocityBean);
	}
	
}
