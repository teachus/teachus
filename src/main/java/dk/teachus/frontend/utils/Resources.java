package dk.teachus.frontend.utils;

import wicket.ResourceReference;
import wicket.markup.html.resources.CompressedResourceReference;
import wicket.markup.html.resources.JavascriptResourceReference;
import dk.teachus.frontend.TeachUsApplication;

public abstract class Resources {

	public static final ResourceReference AVAILABLE = new ResourceReference(TeachUsApplication.class, "resources/img/available_static.png"); //$NON-NLS-1$
	public static final ResourceReference BOOKED = new ResourceReference(TeachUsApplication.class, "resources/img/booked.png"); //$NON-NLS-1$
	public static final ResourceReference OCCUPIED = new ResourceReference(TeachUsApplication.class, "resources/img/occupied.png");
	public static final ResourceReference LEFT = new ResourceReference(TeachUsApplication.class, "resources/img/left.png"); //$NON-NLS-1$;
	public static final ResourceReference RIGHT = new ResourceReference(TeachUsApplication.class, "resources/img/right.png"); //$NON-NLS-1$;
	public static final ResourceReference UNPAID = new ResourceReference(TeachUsApplication.class, "resources/img/unpaid.png"); //$NON-NLS-1$;
	public static final ResourceReference PAID = new ResourceReference(TeachUsApplication.class, "resources/img/paid.png"); //$NON-NLS-1$;
	
	public static final ResourceReference CSS_ANDREAS09 = new CompressedResourceReference(TeachUsApplication.class, "resources/andreas09.css"); //$NON-NLS-1$
	public static final ResourceReference CSS_SCREEN = new CompressedResourceReference(TeachUsApplication.class, "resources/screen.css"); //$NON-NLS-1$
	public static final ResourceReference JS_PROTOTYPE = new JavascriptResourceReference(TeachUsApplication.class, "resources/prototype.js"); //$NON-NLS-1$
	
}
