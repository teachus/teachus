package dk.frankbille.teachus.bean.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.web.util.HtmlUtils;

import dk.frankbille.teachus.bean.MailBean;
import dk.frankbille.teachus.bean.VelocityBean;
import dk.frankbille.teachus.domain.Pupil;
import dk.frankbille.teachus.domain.PupilBooking;
import dk.frankbille.teachus.domain.Teacher;
import dk.frankbille.teachus.utils.ApplicationUtils;

public class MailBeanImpl implements MailBean {
	private static final long serialVersionUID = 1L;
	
	private JavaMailSender mailSender;
	private VelocityBean velocityBean;
	
	public MailBeanImpl(JavaMailSender mailSender, VelocityBean velocityBean) {
		this.mailSender = mailSender;
		this.velocityBean = velocityBean;
	}

	public void sendWelcomeMail(final Pupil pupil) {
		mailSender.send(new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage, false, "UTF-8");

				// Sender and recipient
				String email = pupil.getTeacher().getEmail();
				String fromName = pupil.getTeacher().getName();
				
				message.setFrom(new InternetAddress(email, fromName));
				message.setTo(new InternetAddress(pupil.getEmail(), pupil.getName()));
				
				// Parse the template
				Map<String, Object> model = new HashMap<String, Object>();
				model.put("server", "http://teachus.billen.dk/");
				model.put("from", HtmlUtils.htmlEscape(fromName));
				model.put("name", HtmlUtils.htmlEscape(pupil.getName()));
				model.put("username", HtmlUtils.htmlEscape(pupil.getUsername()));
				model.put("password", HtmlUtils.htmlEscape(pupil.getPassword()));
				String template = velocityBean.mergeTemplate("dk/frankbille/teachus/bean/impl/WelcomeMail", model, pupil.getTeacher().getLocale());
				
				// Subject
				// First line in parsed template is the subject
				String subject = template.substring(0, template.indexOf('\n'));
				message.setSubject(subject);
				
				// Text
				String text = template.substring(template.indexOf('\n'));
				message.setText(text, true);

				mimeMessage.addHeader("X-Mailer", "TeachUs ("+ApplicationUtils.getVersion()+")");
			}
		});
	}
	
	public void sendNewBookingsMail(final Teacher teacher, final List<PupilBooking> pupilBookings) {
		if (pupilBookings.isEmpty() == false) {
			mailSender.send(new MimeMessagePreparator() {
				public void prepare(MimeMessage mimeMessage) throws Exception {
					MimeMessageHelper message = new MimeMessageHelper(mimeMessage, false, "UTF-8");
	
					// Sender and recipient
					String fromEmail = "teachus@billen.dk";
					String fromName = "TeachUs Booking System";
					
					message.setFrom(new InternetAddress(fromEmail, fromName));
					message.setTo(new InternetAddress(teacher.getEmail(), teacher.getName()));
					
					Locale locale = teacher.getLocale();
					
					// Build up bookingslist and format date
					List<FormattedPupilBooking> pupilBookingList = new ArrayList<FormattedPupilBooking>();
					for (PupilBooking pupilBooking : pupilBookings) {
						FormattedPupilBooking formattedPupilBooking = new FormattedPupilBooking();
						formattedPupilBooking.setPupilBooking(pupilBooking);
						SimpleDateFormat dateFormat = new SimpleDateFormat("EE, d. MMM yyyy H:mm", locale);
						formattedPupilBooking.setFormattedDate(dateFormat.format(pupilBooking.getDate()));
						pupilBookingList.add(formattedPupilBooking);
					}
					
					// Parse the template
					Map<String, Object> model = new HashMap<String, Object>();
					model.put("from", HtmlUtils.htmlEscape(fromName));
					model.put("pupilBookingList", pupilBookingList);
					model.put("pupilHeader", "Sanger"); // TODO externalize
					model.put("dateHeader", "Dato/Tidspunkt"); // TODO externalize
					String template = velocityBean.mergeTemplate("dk/frankbille/teachus/bean/impl/NewBookingsMail", model, locale);
					
					// Subject
					// First line in parsed template is the subject
					String subject = template.substring(0, template.indexOf('\n'));
					message.setSubject(subject);
					
					// Text
					String text = template.substring(template.indexOf('\n'));
					message.setText(text, true);
	
					mimeMessage.addHeader("X-Mailer", "TeachUs ("+ApplicationUtils.getVersion()+")");
				}
			});
		}
	}
	
	public static class FormattedPupilBooking {
		private PupilBooking pupilBooking;

		private String formattedDate;

		public String getFormattedDate() {
			return formattedDate;
		}

		public void setFormattedDate(String formattedDate) {
			this.formattedDate = formattedDate;
		}

		public PupilBooking getPupilBooking() {
			return pupilBooking;
		}

		public void setPupilBooking(PupilBooking pupilBooking) {
			this.pupilBooking = pupilBooking;
		}
	}
	
}
