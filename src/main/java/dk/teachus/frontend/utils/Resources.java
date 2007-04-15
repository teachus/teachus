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

import wicket.ResourceReference;
import wicket.markup.html.resources.CompressedResourceReference;
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
	
	public static final ResourceReference TOOLBAR_RED = new ResourceReference(TeachUsApplication.class, "resources/img/toolbar_back_red.png"); //$NON-NLS-1$;
	public static final ResourceReference LIST_HEADER_RED = new ResourceReference(TeachUsApplication.class, "resources/img/list_header_back_red.png"); //$NON-NLS-1$;
	public static final ResourceReference TOOLBAR_ORANGE = new ResourceReference(TeachUsApplication.class, "resources/img/toolbar_back_orange.png"); //$NON-NLS-1$;
	public static final ResourceReference LIST_HEADER_ORANGE = new ResourceReference(TeachUsApplication.class, "resources/img/list_header_back_orange.png"); //$NON-NLS-1$;
	public static final ResourceReference TOOLBAR_BLACK = new ResourceReference(TeachUsApplication.class, "resources/img/toolbar_back_black.png"); //$NON-NLS-1$;
	public static final ResourceReference LIST_HEADER_BLACK = new ResourceReference(TeachUsApplication.class, "resources/img/list_header_back_black.png"); //$NON-NLS-1$;
	public static final ResourceReference TOOLBAR_GREEN = new ResourceReference(TeachUsApplication.class, "resources/img/toolbar_back_green.png"); //$NON-NLS-1$;
	public static final ResourceReference LIST_HEADER_GREEN = new ResourceReference(TeachUsApplication.class, "resources/img/list_header_back_green.png"); //$NON-NLS-1$
	public static final ResourceReference TOOLBAR_PURPLE = new ResourceReference(TeachUsApplication.class, "resources/img/toolbar_back_purple.png"); //$NON-NLS-1$;
	public static final ResourceReference LIST_HEADER_PURPLE = new ResourceReference(TeachUsApplication.class, "resources/img/list_header_back_purple.png"); //$NON-NLS-1$
	
	public static final ResourceReference ANDREAS09_BODYBG = new ResourceReference(TeachUsApplication.class, "resources/img/bodybg.jpg"); //$NON-NLS-1$;
	public static final ResourceReference ANDREAS09_MENUHOVER = new ResourceReference(TeachUsApplication.class, "resources/img/menuhover.jpg"); //$NON-NLS-1$;
	public static final ResourceReference ANDREAS09_FOOTERBG = new ResourceReference(TeachUsApplication.class, "resources/img/footerbg.jpg"); //$NON-NLS-1$;
	
	public static final ResourceReference ANDREAS09_BODYBG_RED = new ResourceReference(TeachUsApplication.class, "resources/img/bodybg-red.jpg"); //$NON-NLS-1$;
	public static final ResourceReference ANDREAS09_MENUHOVER_RED = new ResourceReference(TeachUsApplication.class, "resources/img/menuhover-red.jpg"); //$NON-NLS-1$;
	public static final ResourceReference ANDREAS09_BODYBG_BLACK = new ResourceReference(TeachUsApplication.class, "resources/img/bodybg-black.jpg"); //$NON-NLS-1$;
	public static final ResourceReference ANDREAS09_MENUHOVER_BLACK = new ResourceReference(TeachUsApplication.class, "resources/img/menuhover-black.jpg"); //$NON-NLS-1$;
	public static final ResourceReference ANDREAS09_BODYBG_GREEN = new ResourceReference(TeachUsApplication.class, "resources/img/bodybg-green.jpg"); //$NON-NLS-1$;
	public static final ResourceReference ANDREAS09_MENUHOVER_GREEN = new ResourceReference(TeachUsApplication.class, "resources/img/menuhover-green.jpg"); //$NON-NLS-1$;
	public static final ResourceReference ANDREAS09_BODYBG_ORANGE = new ResourceReference(TeachUsApplication.class, "resources/img/bodybg-orange.jpg"); //$NON-NLS-1$;
	public static final ResourceReference ANDREAS09_MENUHOVER_ORANGE = new ResourceReference(TeachUsApplication.class, "resources/img/menuhover-orange.jpg"); //$NON-NLS-1$;
	public static final ResourceReference ANDREAS09_BODYBG_PURPLE = new ResourceReference(TeachUsApplication.class, "resources/img/bodybg-purple.jpg"); //$NON-NLS-1$;
	public static final ResourceReference ANDREAS09_MENUHOVER_PURPLE = new ResourceReference(TeachUsApplication.class, "resources/img/menuhover-purple.jpg"); //$NON-NLS-1$;
	
	/*
	 * CSS and JS
	 */
	public static final ResourceReference CSS_ANDREAS09 = new CompressedResourceReference(TeachUsApplication.class, "resources/andreas09.css"); //$NON-NLS-1$
	public static final ResourceReference CSS_ANDREAS09_RED = new CompressedResourceReference(TeachUsApplication.class, "resources/andreas09_red.css"); //$NON-NLS-1$
	public static final ResourceReference CSS_ANDREAS09_BLACK = new CompressedResourceReference(TeachUsApplication.class, "resources/andreas09_black.css"); //$NON-NLS-1$
	public static final ResourceReference CSS_ANDREAS09_ORANGE = new CompressedResourceReference(TeachUsApplication.class, "resources/andreas09_orange.css"); //$NON-NLS-1$
	public static final ResourceReference CSS_ANDREAS09_PURPLE = new CompressedResourceReference(TeachUsApplication.class, "resources/andreas09_purple.css"); //$NON-NLS-1$
	public static final ResourceReference CSS_ANDREAS09_GREEN = new CompressedResourceReference(TeachUsApplication.class, "resources/andreas09_green.css"); //$NON-NLS-1$
	
	public static final ResourceReference CSS_SCREEN = new CompressedResourceReference(TeachUsApplication.class, "resources/screen.css"); //$NON-NLS-1$
	public static final ResourceReference CSS_PRINT = new CompressedResourceReference(TeachUsApplication.class, "resources/print.css"); //$NON-NLS-1$
	
	
	/*
	 * SCREENSHOTS
	 */
	public static final ResourceReference SCREENSHOT_1 = new ResourceReference(TeachUsApplication.class, "resources/screenshots/screenshot1.jpg"); //$NON-NLS-1$;
	public static final ResourceReference SCREENSHOT_1_THUMB = new ResourceReference(TeachUsApplication.class, "resources/screenshots/screenshot1_thumb.jpg"); //$NON-NLS-1$;
	public static final ResourceReference SCREENSHOT_2 = new ResourceReference(TeachUsApplication.class, "resources/screenshots/screenshot2.jpg"); //$NON-NLS-1$;
	public static final ResourceReference SCREENSHOT_2_THUMB = new ResourceReference(TeachUsApplication.class, "resources/screenshots/screenshot2_thumb.jpg"); //$NON-NLS-1$;
	public static final ResourceReference SCREENSHOT_3 = new ResourceReference(TeachUsApplication.class, "resources/screenshots/screenshot3.jpg"); //$NON-NLS-1$;
	public static final ResourceReference SCREENSHOT_3_THUMB = new ResourceReference(TeachUsApplication.class, "resources/screenshots/screenshot3_thumb.jpg"); //$NON-NLS-1$;
	public static final ResourceReference SCREENSHOT_4 = new ResourceReference(TeachUsApplication.class, "resources/screenshots/screenshot4.jpg"); //$NON-NLS-1$;
	public static final ResourceReference SCREENSHOT_4_THUMB = new ResourceReference(TeachUsApplication.class, "resources/screenshots/screenshot4_thumb.jpg"); //$NON-NLS-1$;
	public static final ResourceReference SCREENSHOT_5 = new ResourceReference(TeachUsApplication.class, "resources/screenshots/screenshot5.jpg"); //$NON-NLS-1$;
	public static final ResourceReference SCREENSHOT_5_THUMB = new ResourceReference(TeachUsApplication.class, "resources/screenshots/screenshot5_thumb.jpg"); //$NON-NLS-1$;
	public static final ResourceReference SCREENSHOT_6 = new ResourceReference(TeachUsApplication.class, "resources/screenshots/screenshot6.jpg"); //$NON-NLS-1$;
	public static final ResourceReference SCREENSHOT_6_THUMB = new ResourceReference(TeachUsApplication.class, "resources/screenshots/screenshot6_thumb.jpg"); //$NON-NLS-1$;
	public static final ResourceReference SCREENSHOT_7 = new ResourceReference(TeachUsApplication.class, "resources/screenshots/screenshot7.jpg"); //$NON-NLS-1$;
	public static final ResourceReference SCREENSHOT_7_THUMB = new ResourceReference(TeachUsApplication.class, "resources/screenshots/screenshot7_thumb.jpg"); //$NON-NLS-1$;
	public static final ResourceReference SCREENSHOT_8 = new ResourceReference(TeachUsApplication.class, "resources/screenshots/screenshot8.jpg"); //$NON-NLS-1$;
	public static final ResourceReference SCREENSHOT_8_THUMB = new ResourceReference(TeachUsApplication.class, "resources/screenshots/screenshot8_thumb.jpg"); //$NON-NLS-1$;
	public static final ResourceReference SCREENSHOT_9 = new ResourceReference(TeachUsApplication.class, "resources/screenshots/screenshot9.jpg"); //$NON-NLS-1$;
	public static final ResourceReference SCREENSHOT_9_THUMB = new ResourceReference(TeachUsApplication.class, "resources/screenshots/screenshot9_thumb.jpg"); //$NON-NLS-1$;
	
}
