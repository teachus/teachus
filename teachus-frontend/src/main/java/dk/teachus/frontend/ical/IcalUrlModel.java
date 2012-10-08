package dk.teachus.frontend.ical;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.Strings;

import dk.teachus.backend.domain.Person;
import dk.teachus.backend.domain.Pupil;
import dk.teachus.backend.domain.Teacher;
import dk.teachus.frontend.TeachUsApplication;
import dk.teachus.frontend.TeachUsSession;
import dk.teachus.frontend.models.PersonModel;

public class IcalUrlModel extends AbstractReadOnlyModel<String> {
	private static final long serialVersionUID = 1L;
	
	private PersonModel<? extends Person> personModel;
	
	public IcalUrlModel() {
	}

	public IcalUrlModel(PersonModel<? extends Person> personModel) {
		this.personModel = personModel;
	}

	@Override
	public String getObject() {
		String label = null;
		
		Person person = null;
		
		if (personModel != null) {
			person = personModel.getObject();
		}
		
		if (person == null) {
			person = TeachUsSession.get().getPerson();
		}
		
		if (person != null) {
			if ((person instanceof Pupil || person instanceof Teacher) 
					&& Strings.isEmpty(person.getUsername()) == false) {
				PageParameters pp = new PageParameters();
				pp.set(0, person.getUsername());
				pp.set(1, person.getPassword());
				
				String url = RequestCycle.get().urlFor(IcalResource.RESOURCE, pp).toString();
				
				// Lets clean up the URL
				url = url.replace("../", "");
				url = url.replaceFirst("/$", "");
				
				// Now add the server url to it
				url = TeachUsApplication.get().getServerUrl() + "/" + url;
				
				label = url;
			}
		}
		
		return label;
	}
	
}
