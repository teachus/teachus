package dk.teachus.frontend.components.list;

public class StringFilter implements IFilter<String> {
	private static final long serialVersionUID = 1L;

	public boolean include(String objectProperty, String stateProperty) {
		boolean include = false;
		
		if (objectProperty == null) {
			objectProperty = "";
		}
		if (stateProperty == null) {
			stateProperty = "";
		}
		
		if (objectProperty.toLowerCase().contains(stateProperty.toLowerCase())) {
			include = true;
		}
		
		return include;
	}
	
}
