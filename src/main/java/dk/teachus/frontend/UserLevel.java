package dk.teachus.frontend;

public enum UserLevel {
	ADMIN(1),
	TEACHER(2),
	PUPIL(3);
	
	private int level;

	private UserLevel(int level) {
		this.level = level;
	}
	
	public boolean authorized(UserLevel actual) {
		boolean authorized = false;
		
		if (actual.level <= this.level) {
			authorized = true;
		}
		
		return authorized;
	}
}
