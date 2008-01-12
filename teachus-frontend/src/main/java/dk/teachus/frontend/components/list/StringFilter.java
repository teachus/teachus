package dk.teachus.frontend.components.list;

public class StringFilter implements IFilter<String> {
	private static final long serialVersionUID = 1L;

	public boolean include(String o1, String o2) {
		boolean include = false;
		
		if (o1 == null) {
			o1 = "";
		}
		if (o2 == null) {
			o2 = "";
		}
		
		if (o1.toLowerCase().contains(o2.toLowerCase())) {
			include = true;
		}
		
		return include;
	}
	
}
