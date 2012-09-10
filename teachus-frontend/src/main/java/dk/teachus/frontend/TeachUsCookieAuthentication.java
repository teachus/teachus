package dk.teachus.frontend;

import java.util.List;

import javax.servlet.http.Cookie;

import org.apache.wicket.Page;
import org.apache.wicket.authorization.strategies.page.AbstractPageAuthorizationStrategy;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.util.string.Strings;

import dk.teachus.frontend.pages.UnAuthenticatedBasePage;

public class TeachUsCookieAuthentication extends AbstractPageAuthorizationStrategy {
	
	@Override
	protected <T extends Page> boolean isPageAuthorized(Class<T> pageClass) {
		if (TeachUsSession.get().isAuthenticated() == false) {
			RequestCycle requestCycle = RequestCycle.get();
			WebRequest request = (WebRequest) requestCycle.getRequest();
			
			String username = null;
			String password = null;
			
			List<Cookie> cookies = request.getCookies();

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
