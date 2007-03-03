package dk.teachus.frontend.utils;

import wicket.ResourceReference;
import wicket.markup.html.resources.CompressedResourceReference;
import wicket.markup.html.resources.JavascriptResourceReference;
import dk.teachus.frontend.TeachUsApplication;

public abstract class Resources {

	public static final ResourceReference CALENDAR = new ResourceReference(TeachUsApplication.class, "resources/calendar.png"); //$NON-NLS-1$
	public static final ResourceReference AVAILABLE = new ResourceReference(TeachUsApplication.class, "resources/available_static.png"); //$NON-NLS-1$
	public static final ResourceReference BOOKED = new ResourceReference(TeachUsApplication.class, "resources/booked.png"); //$NON-NLS-1$
	public static final ResourceReference OCCUPIED = new ResourceReference(TeachUsApplication.class, "resources/occupied.png");
	public static final ResourceReference SIGN_IN = new ResourceReference(TeachUsApplication.class, "resources/signin.png"); //$NON-NLS-1$
	public static final ResourceReference LOCK = new ResourceReference(TeachUsApplication.class, "resources/lock.png"); //$NON-NLS-1$
	public static final ResourceReference SIGNOUT_SMALL = new ResourceReference(TeachUsApplication.class, "resources/signout_small.png"); //$NON-NLS-1$
	public static final ResourceReference CALENDAR_SMALL = new ResourceReference(TeachUsApplication.class, "resources/calendar_small.png"); //$NON-NLS-1$
	public static final ResourceReference PUPIL_SMALL = new ResourceReference(TeachUsApplication.class, "resources/pupil_small.png"); //$NON-NLS-1$
	public static final ResourceReference TEACHER_SMALL = new ResourceReference(TeachUsApplication.class, "resources/teacher_small.png"); //$NON-NLS-1$
	public static final ResourceReference ADMIN_SMALL = new ResourceReference(TeachUsApplication.class, "resources/admin_small.png"); //$NON-NLS-1$
	public static final ResourceReference PERIOD_SMALL = new ResourceReference(TeachUsApplication.class, "resources/period_small.png"); //$NON-NLS-1$;
	public static final ResourceReference AGENDA_SMALL = new ResourceReference(TeachUsApplication.class, "resources/agenda_small.png"); //$NON-NLS-1$;
	public static final ResourceReference PAYMENT_SMALL = new ResourceReference(TeachUsApplication.class, "resources/payment_small.png"); //$NON-NLS-1$;
	public static final ResourceReference PAYMENT1_SMALL = new ResourceReference(TeachUsApplication.class, "resources/payment1_small.png"); //$NON-NLS-1$;
	public static final ResourceReference PAYMENT2_SMALL = new ResourceReference(TeachUsApplication.class, "resources/payment2_small.png"); //$NON-NLS-1$;
	public static final ResourceReference STATS_SMALL = new ResourceReference(TeachUsApplication.class, "resources/stats_small.png"); //$NON-NLS-1$;
	public static final ResourceReference SETTINGS_SMALL = new ResourceReference(TeachUsApplication.class, "resources/settings_small.png"); //$NON-NLS-1$;
	public static final ResourceReference ADMIN = new ResourceReference(TeachUsApplication.class, "resources/admin.png"); //$NON-NLS-1$
	public static final ResourceReference PUPIL = new ResourceReference(TeachUsApplication.class, "resources/pupil.png"); //$NON-NLS-1$
	public static final ResourceReference TEACHER = new ResourceReference(TeachUsApplication.class, "resources/teacher.png"); //$NON-NLS-1$
	public static final ResourceReference PERIOD = new ResourceReference(TeachUsApplication.class, "resources/period.png"); //$NON-NLS-1$;
	public static final ResourceReference AGENDA = new ResourceReference(TeachUsApplication.class, "resources/agenda.png"); //$NON-NLS-1$;
	public static final ResourceReference PAYMENT = new ResourceReference(TeachUsApplication.class, "resources/payment.png"); //$NON-NLS-1$;
	public static final ResourceReference PAYMENT1 = new ResourceReference(TeachUsApplication.class, "resources/payment1.png"); //$NON-NLS-1$;
	public static final ResourceReference PAYMENT2 = new ResourceReference(TeachUsApplication.class, "resources/payment2.png"); //$NON-NLS-1$;
	public static final ResourceReference LEFT = new ResourceReference(TeachUsApplication.class, "resources/left.png"); //$NON-NLS-1$;
	public static final ResourceReference RIGHT = new ResourceReference(TeachUsApplication.class, "resources/right.png"); //$NON-NLS-1$;
	public static final ResourceReference UNPAID = new ResourceReference(TeachUsApplication.class, "resources/unpaid.png"); //$NON-NLS-1$;
	public static final ResourceReference PAID = new ResourceReference(TeachUsApplication.class, "resources/paid.png"); //$NON-NLS-1$;
	public static final ResourceReference STATS = new ResourceReference(TeachUsApplication.class, "resources/stats.png"); //$NON-NLS-1$;
	public static final ResourceReference SETTINGS = new ResourceReference(TeachUsApplication.class, "resources/settings.png"); //$NON-NLS-1$;
	
	public static final ResourceReference CSS_ANDREAS09 = new CompressedResourceReference(TeachUsApplication.class, "resources/andreas09.css"); //$NON-NLS-1$
	public static final ResourceReference CSS_SCREEN = new CompressedResourceReference(TeachUsApplication.class, "resources/screen.css"); //$NON-NLS-1$
	public static final ResourceReference JS_PROTOTYPE = new JavascriptResourceReference(TeachUsApplication.class, "resources/prototype.js"); //$NON-NLS-1$
	
}
