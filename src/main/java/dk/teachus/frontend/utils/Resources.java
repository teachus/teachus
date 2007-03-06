package dk.teachus.frontend.utils;

import wicket.ResourceReference;
import wicket.markup.html.resources.CompressedResourceReference;
import wicket.markup.html.resources.JavascriptResourceReference;
import dk.teachus.frontend.TeachUsApplication;

public abstract class Resources {

	/*
	 * IMAGES
	 */
	public static final ResourceReference AVAILABLE = new ResourceReference(TeachUsApplication.class, "resources/img/available_static.png"); //$NON-NLS-1$
	public static final ResourceReference AVAILABLE_HOVER = new ResourceReference(TeachUsApplication.class, "resources/img/available_static_hover.png"); //$NON-NLS-1$
	public static final ResourceReference BOOKED = new ResourceReference(TeachUsApplication.class, "resources/img/booked.png"); //$NON-NLS-1$
	public static final ResourceReference BOOKED_HOVER = new ResourceReference(TeachUsApplication.class, "resources/img/booked_hover.png"); //$NON-NLS-1$
	public static final ResourceReference OCCUPIED = new ResourceReference(TeachUsApplication.class, "resources/img/occupied.png");
	public static final ResourceReference LEFT = new ResourceReference(TeachUsApplication.class, "resources/img/left.png"); //$NON-NLS-1$;
	public static final ResourceReference RIGHT = new ResourceReference(TeachUsApplication.class, "resources/img/right.png"); //$NON-NLS-1$;
	public static final ResourceReference UNPAID = new ResourceReference(TeachUsApplication.class, "resources/img/unpaid.png"); //$NON-NLS-1$;
	public static final ResourceReference PAID = new ResourceReference(TeachUsApplication.class, "resources/img/paid.png"); //$NON-NLS-1$;
	public static final ResourceReference TOOLBAR = new ResourceReference(TeachUsApplication.class, "resources/img/toolbar_back.png"); //$NON-NLS-1$;
	public static final ResourceReference LIST_HEADER = new ResourceReference(TeachUsApplication.class, "resources/img/list_header_back.png"); //$NON-NLS-1$;
	public static final ResourceReference EMPTY = new ResourceReference(TeachUsApplication.class, "resources/img/empty.gif"); //$NON-NLS-1$;
	
	public static final ResourceReference ANDREAS09_BODYBG = new ResourceReference(TeachUsApplication.class, "resources/img/bodybg.jpg"); //$NON-NLS-1$;
	public static final ResourceReference ANDREAS09_MENUHOVER = new ResourceReference(TeachUsApplication.class, "resources/img/menuhover.jpg"); //$NON-NLS-1$;
	public static final ResourceReference ANDREAS09_FOOTERBG = new ResourceReference(TeachUsApplication.class, "resources/img/footerbg.jpg"); //$NON-NLS-1$;
	
	/*
	 * CSS and JS
	 */
	public static final ResourceReference CSS_ANDREAS09 = new CompressedResourceReference(TeachUsApplication.class, "resources/andreas09.css"); //$NON-NLS-1$
	public static final ResourceReference CSS_SCREEN = new CompressedResourceReference(TeachUsApplication.class, "resources/screen.css"); //$NON-NLS-1$
	public static final ResourceReference JS_PROTOTYPE = new JavascriptResourceReference(TeachUsApplication.class, "resources/prototype.js"); //$NON-NLS-1$
	
	/*
	 * SCREENSHOTS
	 */
	public static final ResourceReference SCREENSHOT_1 = new ResourceReference(TeachUsApplication.class, "resources/screenshots/screenshot1.png"); //$NON-NLS-1$;
	public static final ResourceReference SCREENSHOT_1_THUMB = new ResourceReference(TeachUsApplication.class, "resources/screenshots/screenshot1_thumb.jpg"); //$NON-NLS-1$;
	public static final ResourceReference SCREENSHOT_2 = new ResourceReference(TeachUsApplication.class, "resources/screenshots/screenshot2.png"); //$NON-NLS-1$;
	public static final ResourceReference SCREENSHOT_2_THUMB = new ResourceReference(TeachUsApplication.class, "resources/screenshots/screenshot2_thumb.jpg"); //$NON-NLS-1$;
	public static final ResourceReference SCREENSHOT_3 = new ResourceReference(TeachUsApplication.class, "resources/screenshots/screenshot3.png"); //$NON-NLS-1$;
	public static final ResourceReference SCREENSHOT_3_THUMB = new ResourceReference(TeachUsApplication.class, "resources/screenshots/screenshot3_thumb.jpg"); //$NON-NLS-1$;
	public static final ResourceReference SCREENSHOT_4 = new ResourceReference(TeachUsApplication.class, "resources/screenshots/screenshot4.png"); //$NON-NLS-1$;
	public static final ResourceReference SCREENSHOT_4_THUMB = new ResourceReference(TeachUsApplication.class, "resources/screenshots/screenshot4_thumb.jpg"); //$NON-NLS-1$;
	public static final ResourceReference SCREENSHOT_5 = new ResourceReference(TeachUsApplication.class, "resources/screenshots/screenshot5.png"); //$NON-NLS-1$;
	public static final ResourceReference SCREENSHOT_5_THUMB = new ResourceReference(TeachUsApplication.class, "resources/screenshots/screenshot5_thumb.jpg"); //$NON-NLS-1$;
	public static final ResourceReference SCREENSHOT_6 = new ResourceReference(TeachUsApplication.class, "resources/screenshots/screenshot6.png"); //$NON-NLS-1$;
	public static final ResourceReference SCREENSHOT_6_THUMB = new ResourceReference(TeachUsApplication.class, "resources/screenshots/screenshot6_thumb.jpg"); //$NON-NLS-1$;
	public static final ResourceReference SCREENSHOT_7 = new ResourceReference(TeachUsApplication.class, "resources/screenshots/screenshot7.png"); //$NON-NLS-1$;
	public static final ResourceReference SCREENSHOT_7_THUMB = new ResourceReference(TeachUsApplication.class, "resources/screenshots/screenshot7_thumb.jpg"); //$NON-NLS-1$;
	public static final ResourceReference SCREENSHOT_8 = new ResourceReference(TeachUsApplication.class, "resources/screenshots/screenshot8.png"); //$NON-NLS-1$;
	public static final ResourceReference SCREENSHOT_8_THUMB = new ResourceReference(TeachUsApplication.class, "resources/screenshots/screenshot8_thumb.jpg"); //$NON-NLS-1$;
	public static final ResourceReference SCREENSHOT_9 = new ResourceReference(TeachUsApplication.class, "resources/screenshots/screenshot9.png"); //$NON-NLS-1$;
	public static final ResourceReference SCREENSHOT_9_THUMB = new ResourceReference(TeachUsApplication.class, "resources/screenshots/screenshot9_thumb.jpg"); //$NON-NLS-1$;
	
}
