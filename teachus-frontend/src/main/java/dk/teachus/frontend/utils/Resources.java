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
package dk.teachus.frontend.utils;

import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.request.resource.SharedResourceReference;

import dk.teachus.frontend.TeachUsApplication;

public abstract class Resources {

	/*
	 * IMAGES
	 */
	public static final ResourceReference AVAILABLE = new SharedResourceReference(TeachUsApplication.class, "resources/img/available_static.png"); //$NON-NLS-1$
	public static final ResourceReference AVAILABLE_HOVER = new SharedResourceReference(TeachUsApplication.class, "resources/img/available_static_hover.png"); //$NON-NLS-1$
	public static final ResourceReference BOOKED = new SharedResourceReference(TeachUsApplication.class, "resources/img/booked.png"); //$NON-NLS-1$
	public static final ResourceReference BOOKED_HOVER = new SharedResourceReference(TeachUsApplication.class, "resources/img/booked_hover.png"); //$NON-NLS-1$
	public static final ResourceReference OCCUPIED = new SharedResourceReference(TeachUsApplication.class, "resources/img/occupied.png");
	public static final ResourceReference LEFT = new SharedResourceReference(TeachUsApplication.class, "resources/img/left.png"); //$NON-NLS-1$;
	public static final ResourceReference RIGHT = new SharedResourceReference(TeachUsApplication.class, "resources/img/right.png"); //$NON-NLS-1$;
	public static final ResourceReference UNPAID = new SharedResourceReference(TeachUsApplication.class, "resources/img/unpaid.png"); //$NON-NLS-1$;
	public static final ResourceReference PAID = new SharedResourceReference(TeachUsApplication.class, "resources/img/paid.png"); //$NON-NLS-1$;
	public static final ResourceReference TOOLBAR = new SharedResourceReference(TeachUsApplication.class, "resources/img/toolbar_back.png"); //$NON-NLS-1$;
	public static final ResourceReference LIST_HEADER = new SharedResourceReference(TeachUsApplication.class, "resources/img/list_header_back.png"); //$NON-NLS-1$;
	public static final ResourceReference EMPTY = new SharedResourceReference(TeachUsApplication.class, "resources/img/empty.gif"); //$NON-NLS-1$;
	public static final ResourceReference DOT_INDICATOR = new SharedResourceReference(TeachUsApplication.class, "resources/img/dot_indicator.gif");
	
	public static final ResourceReference TOOLBAR_RED = new SharedResourceReference(TeachUsApplication.class, "resources/img/toolbar_back_red.png"); //$NON-NLS-1$;
	public static final ResourceReference LIST_HEADER_RED = new SharedResourceReference(TeachUsApplication.class, "resources/img/list_header_back_red.png"); //$NON-NLS-1$;
	public static final ResourceReference TOOLBAR_ORANGE = new SharedResourceReference(TeachUsApplication.class, "resources/img/toolbar_back_orange.png"); //$NON-NLS-1$;
	public static final ResourceReference LIST_HEADER_ORANGE = new SharedResourceReference(TeachUsApplication.class, "resources/img/list_header_back_orange.png"); //$NON-NLS-1$;
	public static final ResourceReference TOOLBAR_BLACK = new SharedResourceReference(TeachUsApplication.class, "resources/img/toolbar_back_black.png"); //$NON-NLS-1$;
	public static final ResourceReference LIST_HEADER_BLACK = new SharedResourceReference(TeachUsApplication.class, "resources/img/list_header_back_black.png"); //$NON-NLS-1$;
	public static final ResourceReference TOOLBAR_GREEN = new SharedResourceReference(TeachUsApplication.class, "resources/img/toolbar_back_green.png"); //$NON-NLS-1$;
	public static final ResourceReference LIST_HEADER_GREEN = new SharedResourceReference(TeachUsApplication.class, "resources/img/list_header_back_green.png"); //$NON-NLS-1$
	public static final ResourceReference TOOLBAR_PURPLE = new SharedResourceReference(TeachUsApplication.class, "resources/img/toolbar_back_purple.png"); //$NON-NLS-1$;
	public static final ResourceReference LIST_HEADER_PURPLE = new SharedResourceReference(TeachUsApplication.class, "resources/img/list_header_back_purple.png"); //$NON-NLS-1$
	
	public static final ResourceReference ANDREAS09_BODYBG = new SharedResourceReference(TeachUsApplication.class, "resources/img/bodybg.jpg"); //$NON-NLS-1$;
	public static final ResourceReference ANDREAS09_MENUHOVER = new SharedResourceReference(TeachUsApplication.class, "resources/img/menuhover.jpg"); //$NON-NLS-1$;
	public static final ResourceReference ANDREAS09_FOOTERBG = new SharedResourceReference(TeachUsApplication.class, "resources/img/footerbg.jpg"); //$NON-NLS-1$;
	
	public static final ResourceReference ANDREAS09_BODYBG_RED = new SharedResourceReference(TeachUsApplication.class, "resources/img/bodybg-red.jpg"); //$NON-NLS-1$;
	public static final ResourceReference ANDREAS09_MENUHOVER_RED = new SharedResourceReference(TeachUsApplication.class, "resources/img/menuhover-red.jpg"); //$NON-NLS-1$;
	public static final ResourceReference ANDREAS09_BODYBG_BLACK = new SharedResourceReference(TeachUsApplication.class, "resources/img/bodybg-black.jpg"); //$NON-NLS-1$;
	public static final ResourceReference ANDREAS09_MENUHOVER_BLACK = new SharedResourceReference(TeachUsApplication.class, "resources/img/menuhover-black.jpg"); //$NON-NLS-1$;
	public static final ResourceReference ANDREAS09_BODYBG_GREEN = new SharedResourceReference(TeachUsApplication.class, "resources/img/bodybg-green.jpg"); //$NON-NLS-1$;
	public static final ResourceReference ANDREAS09_MENUHOVER_GREEN = new SharedResourceReference(TeachUsApplication.class, "resources/img/menuhover-green.jpg"); //$NON-NLS-1$;
	public static final ResourceReference ANDREAS09_BODYBG_ORANGE = new SharedResourceReference(TeachUsApplication.class, "resources/img/bodybg-orange.jpg"); //$NON-NLS-1$;
	public static final ResourceReference ANDREAS09_MENUHOVER_ORANGE = new SharedResourceReference(TeachUsApplication.class, "resources/img/menuhover-orange.jpg"); //$NON-NLS-1$;
	public static final ResourceReference ANDREAS09_BODYBG_PURPLE = new SharedResourceReference(TeachUsApplication.class, "resources/img/bodybg-purple.jpg"); //$NON-NLS-1$;
	public static final ResourceReference ANDREAS09_MENUHOVER_PURPLE = new SharedResourceReference(TeachUsApplication.class, "resources/img/menuhover-purple.jpg"); //$NON-NLS-1$;
	
	public static final ResourceReference ASC = new SharedResourceReference(TeachUsApplication.class, "resources/img/asc.png"); //$NON-NLS-1$;
	public static final ResourceReference DESC = new SharedResourceReference(TeachUsApplication.class, "resources/img/desc.png"); //$NON-NLS-1$;
	
	public static final ResourceReference ICON_CALENDAR = new SharedResourceReference(TeachUsApplication.class, "resources/img/calendar.png"); //$NON-NLS-1$;
	public static final ResourceReference ICON_EMAIL_NEW_PASSWORD = new SharedResourceReference(TeachUsApplication.class, "resources/img/email_new_password.png"); //$NON-NLS-1$;
	public static final ResourceReference ICON_DELETE = new SharedResourceReference(TeachUsApplication.class, "resources/img/delete.png"); //$NON-NLS-1$;
	
	public static final ResourceReference ICON_USERNAME = new SharedResourceReference(TeachUsApplication.class, "resources/img/username.png"); //$NON-NLS-1$;
	public static final ResourceReference ICON_PASSWORD = new SharedResourceReference(TeachUsApplication.class, "resources/img/password.png"); //$NON-NLS-1$;
	
	/*
	 * CSS and JS
	 */
	public static final ResourceReference CSS_ANDREAS09 = new JavaScriptResourceReference(TeachUsApplication.class, "resources/andreas09.css"); //$NON-NLS-1$
	public static final ResourceReference CSS_ANDREAS09_RED = new JavaScriptResourceReference(TeachUsApplication.class, "resources/andreas09_red.css"); //$NON-NLS-1$
	public static final ResourceReference CSS_ANDREAS09_BLACK = new JavaScriptResourceReference(TeachUsApplication.class, "resources/andreas09_black.css"); //$NON-NLS-1$
	public static final ResourceReference CSS_ANDREAS09_ORANGE = new JavaScriptResourceReference(TeachUsApplication.class, "resources/andreas09_orange.css"); //$NON-NLS-1$
	public static final ResourceReference CSS_ANDREAS09_PURPLE = new JavaScriptResourceReference(TeachUsApplication.class, "resources/andreas09_purple.css"); //$NON-NLS-1$
	public static final ResourceReference CSS_ANDREAS09_GREEN = new JavaScriptResourceReference(TeachUsApplication.class, "resources/andreas09_green.css"); //$NON-NLS-1$
	
	public static final ResourceReference CSS_SCREEN = new JavaScriptResourceReference(TeachUsApplication.class, "resources/screen.css"); //$NON-NLS-1$
	public static final ResourceReference CSS_PRINT = new JavaScriptResourceReference(TeachUsApplication.class, "resources/print.css"); //$NON-NLS-1$
	
	
	/*
	 * SCREENSHOTS
	 */
	public static final ResourceReference SCREENSHOT_2 = new SharedResourceReference(TeachUsApplication.class, "resources/screenshots/screenshot2.jpg"); //$NON-NLS-1$;
	public static final ResourceReference SCREENSHOT_2_THUMB = new SharedResourceReference(TeachUsApplication.class, "resources/screenshots/screenshot2_thumb.jpg"); //$NON-NLS-1$;
	public static final ResourceReference SCREENSHOT_4 = new SharedResourceReference(TeachUsApplication.class, "resources/screenshots/screenshot4.jpg"); //$NON-NLS-1$;
	public static final ResourceReference SCREENSHOT_4_THUMB = new SharedResourceReference(TeachUsApplication.class, "resources/screenshots/screenshot4_thumb.jpg"); //$NON-NLS-1$;
	public static final ResourceReference SCREENSHOT_7 = new SharedResourceReference(TeachUsApplication.class, "resources/screenshots/screenshot7.jpg"); //$NON-NLS-1$;
	public static final ResourceReference SCREENSHOT_7_THUMB = new SharedResourceReference(TeachUsApplication.class, "resources/screenshots/screenshot7_thumb.jpg"); //$NON-NLS-1$;
	public static final ResourceReference SCREENSHOT_8 = new SharedResourceReference(TeachUsApplication.class, "resources/screenshots/screenshot8.jpg"); //$NON-NLS-1$;
	public static final ResourceReference SCREENSHOT_8_THUMB = new SharedResourceReference(TeachUsApplication.class, "resources/screenshots/screenshot8_thumb.jpg"); //$NON-NLS-1$;
	public static final ResourceReference SCREENSHOT_9 = new SharedResourceReference(TeachUsApplication.class, "resources/screenshots/screenshot9.jpg"); //$NON-NLS-1$;
	public static final ResourceReference SCREENSHOT_9_THUMB = new SharedResourceReference(TeachUsApplication.class, "resources/screenshots/screenshot9_thumb.jpg"); //$NON-NLS-1$;
	public static final ResourceReference SCREENSHOT_10 = new SharedResourceReference(TeachUsApplication.class, "resources/screenshots/screenshot10.jpg"); //$NON-NLS-1$;
	public static final ResourceReference SCREENSHOT_10_THUMB = new SharedResourceReference(TeachUsApplication.class, "resources/screenshots/screenshot10_thumb.jpg"); //$NON-NLS-1$;
	public static final ResourceReference SCREENSHOT_11 = new SharedResourceReference(TeachUsApplication.class, "resources/screenshots/screenshot11.jpg"); //$NON-NLS-1$;
	public static final ResourceReference SCREENSHOT_11_THUMB = new SharedResourceReference(TeachUsApplication.class, "resources/screenshots/screenshot11_thumb.jpg"); //$NON-NLS-1$;
	public static final ResourceReference SCREENSHOT_12 = new SharedResourceReference(TeachUsApplication.class, "resources/screenshots/screenshot12.jpg"); //$NON-NLS-1$;
	public static final ResourceReference SCREENSHOT_12_THUMB = new SharedResourceReference(TeachUsApplication.class, "resources/screenshots/screenshot12_thumb.jpg"); //$NON-NLS-1$;
	public static final ResourceReference SCREENSHOT_13 = new SharedResourceReference(TeachUsApplication.class, "resources/screenshots/screenshot13.jpg"); //$NON-NLS-1$;
	public static final ResourceReference SCREENSHOT_13_THUMB = new SharedResourceReference(TeachUsApplication.class, "resources/screenshots/screenshot13_thumb.jpg"); //$NON-NLS-1$;
	public static final ResourceReference SCREENSHOT_14 = new SharedResourceReference(TeachUsApplication.class, "resources/screenshots/screenshot14.jpg"); //$NON-NLS-1$;
	public static final ResourceReference SCREENSHOT_14_THUMB = new SharedResourceReference(TeachUsApplication.class, "resources/screenshots/screenshot14_thumb.jpg"); //$NON-NLS-1$;
	
}
