package dk.frankbille.teachus.frontend.utils;

import wicket.ResourceReference;
import dk.frankbille.teachus.frontend.pages.AdminsPage;
import dk.frankbille.teachus.frontend.pages.PupilCalendarPage;
import dk.frankbille.teachus.frontend.pages.PupilsPage;
import dk.frankbille.teachus.frontend.pages.SignInPage;
import dk.frankbille.teachus.frontend.pages.SignOutPage;
import dk.frankbille.teachus.frontend.pages.TeachersPage;

public abstract class Icons {

	public static final ResourceReference CALENDAR = new ResourceReference(PupilCalendarPage.class, "resources/calendar.png"); //$NON-NLS-1$
	public static final ResourceReference AVAILABLE = new ResourceReference(PupilCalendarPage.class, "resources/available.png"); //$NON-NLS-1$
	public static final ResourceReference SIGN_IN = new ResourceReference(SignInPage.class, "resources/signin.png"); //$NON-NLS-1$
	public static final ResourceReference LOCK = new ResourceReference(SignInPage.class, "resources/lock.png"); //$NON-NLS-1$
	public static final ResourceReference SIGNOUT_SMALL = new ResourceReference(SignOutPage.class, "resources/signout_small.png"); //$NON-NLS-1$
	public static final ResourceReference CALENDAR_SMALL = new ResourceReference(PupilCalendarPage.class, "resources/calendar_small.png"); //$NON-NLS-1$
	public static final ResourceReference PUPIL_SMALL = new ResourceReference(PupilsPage.class, "resources/pupil_small.png"); //$NON-NLS-1$
	public static final ResourceReference TEACHER_SMALL = new ResourceReference(TeachersPage.class, "resources/teacher_small.png"); //$NON-NLS-1$
	public static final ResourceReference ADMIN_SMALL = new ResourceReference(AdminsPage.class, "resources/admin_small.png"); //$NON-NLS-1$
	public static final ResourceReference PERIOD_SMALL = new ResourceReference(AdminsPage.class, "resources/period_small.png"); //$NON-NLS-1$;
	public static final ResourceReference AGENDA_SMALL = new ResourceReference(AdminsPage.class, "resources/agenda_small.png"); //$NON-NLS-1$;
	public static final ResourceReference PAYMENT_SMALL = new ResourceReference(AdminsPage.class, "resources/payment_small.png"); //$NON-NLS-1$;
	public static final ResourceReference PAYMENT1_SMALL = new ResourceReference(AdminsPage.class, "resources/payment1_small.png"); //$NON-NLS-1$;
	public static final ResourceReference PAYMENT2_SMALL = new ResourceReference(AdminsPage.class, "resources/payment2_small.png"); //$NON-NLS-1$;
	public static final ResourceReference ADMIN = new ResourceReference(AdminsPage.class, "resources/admin.png"); //$NON-NLS-1$
	public static final ResourceReference PUPIL = new ResourceReference(AdminsPage.class, "resources/pupil.png"); //$NON-NLS-1$
	public static final ResourceReference TEACHER = new ResourceReference(AdminsPage.class, "resources/teacher.png"); //$NON-NLS-1$
	public static final ResourceReference PERIOD = new ResourceReference(AdminsPage.class, "resources/period.png"); //$NON-NLS-1$;
	public static final ResourceReference AGENDA = new ResourceReference(AdminsPage.class, "resources/agenda.png"); //$NON-NLS-1$;
	public static final ResourceReference PAYMENT = new ResourceReference(AdminsPage.class, "resources/payment.png"); //$NON-NLS-1$;
	public static final ResourceReference PAYMENT1 = new ResourceReference(AdminsPage.class, "resources/payment1.png"); //$NON-NLS-1$;
	public static final ResourceReference PAYMENT2 = new ResourceReference(AdminsPage.class, "resources/payment2.png"); //$NON-NLS-1$;
	public static final ResourceReference LEFT = new ResourceReference(PupilCalendarPage.class, "resources/left.png"); //$NON-NLS-1$;
	public static final ResourceReference RIGHT = new ResourceReference(PupilCalendarPage.class, "resources/right.png"); //$NON-NLS-1$;
	public static final ResourceReference UNPAID = new ResourceReference(PupilCalendarPage.class, "resources/unpaid.png"); //$NON-NLS-1$;
	public static final ResourceReference PAID = new ResourceReference(PupilCalendarPage.class, "resources/paid.png"); //$NON-NLS-1$;
	
}
