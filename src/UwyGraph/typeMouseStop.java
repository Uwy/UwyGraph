package UwyGraph;

public enum typeMouseStop {
	circles("Circles"), squares("Squares");

	private final String shapeName;

	typeMouseStop(String name) {
		this.shapeName = name;
		BootstrapSingleton.typeMouseStopNames.put(name, this);
	}

	public String toString() {
		return this.shapeName;
	}

	public static final typeMouseStop toTypeMouseStop(String name) {
		return BootstrapSingleton.typeMouseStopNames.get(name);
	}
}