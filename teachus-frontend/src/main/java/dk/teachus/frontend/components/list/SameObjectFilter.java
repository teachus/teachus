package dk.teachus.frontend.components.list;

public class SameObjectFilter implements IFilter<Object> {
	private static final long serialVersionUID = 1L;

	public boolean include(Object objectProperty, Object stateProperty) {
		boolean include = false;
		
		if (objectProperty != null && stateProperty != null) {
			include = objectProperty == stateProperty;
		} else if (stateProperty == null) {
			include = true;
		}
		
		return include;
	}
	
}
