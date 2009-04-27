package dk.teachus.frontend;

import javax.servlet.http.Cookie;

import org.apache.wicket.RequestCycle;
import org.apache.wicket.authorization.strategies.page.AbstractPageAuthorizationStrategy;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.WebRequestCycle;
import org.apache.wicket.util.string.Strings;

import dk.teachus.frontend.pages.UnAuthenticatedBasePage;

public class TeachUsCookieAuthentication extends AbstractPageAuthorizationStrategy {

	@SuppressWarnings("unchecked")
	@Override
	protected boolean isPageAuthorized(Class pageClass) {
		if (TeachUsSession.get().isAuthenticated() == false) {
			WebRequestCycle requestCycle = (WebRequestCycle) RequestCycle.get();
			WebRequest request = requestCycle.getWebRequest();
			
			String username = null;
			String password = null;
			
			Cookie[] cookies = request.getCookies();

			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if (UnAuthenticatedBasePage.USERNAME_PATH.equals(cookie.getName())) {
						username = cookie.getValue();
					}
					if (UnAuthenticatedBasePage.PASSWORD_PATH.equals(cookie.getName())) {
						password = cookie.getValue();
					}
				}
			}
			
			if (Strings.isEmpty(username) == false) {
				TeachUsSession.get().signIn(username, password);
			}
		}
		
		return true;
	}
	
}
