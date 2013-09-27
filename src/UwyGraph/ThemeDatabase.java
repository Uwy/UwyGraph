package UwyGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ThemeDatabase {
	private Map<String, Theme> themeData;

	public ThemeDatabase() {
		this.themeData = new HashMap<String, Theme>();
		this.themeData.put("Current", Theme.getDefaultTheme());
		this.themeData.get("Current").setIsModifiableByUser(true);
		this.themeData.put("Rainbow", Theme.getDefaultTheme());
		this.themeData.put("Miku", Theme.getMikuTheme());
		this.themeData.put("Blue to red", Theme.getBlueToRedTheme());
		this.themeData.put("Red to orange", Theme.getRedToOrangeTheme());
	}

	public synchronized String[] getModifiableThemesNames() {
		String names[] = this.themeData.keySet().toArray(new String[0]);
		ArrayList<String> namesModifiable = new ArrayList<String>();
		int i = 0;
		while (i < names.length) {
			if (this.themeData.get(names[i]).getIsModifiableByUser())
				namesModifiable.add(names[i]);
			i++;
		}
		return namesModifiable.toArray(new String[0]);
	}

	public synchronized String[] getThemesNames() {
		return this.themeData.keySet().toArray(new String[0]);
	}

	public synchronized Theme getTheme(String name) {
		return new Theme(this.themeData.get(name));
		
	}

	public synchronized void setTheme(String name, Theme theme) {
		this.themeData.put(name, theme);
	}

	public synchronized void setAsCurrent(String name) {
		this.themeData.put("Current", new Theme(this.themeData.get(name)));
		this.themeData.get("Current").setIsModifiableByUser(true);
	}

	public synchronized void setAsCurrent(Theme theme) {
		this.themeData.put("Current", new Theme(theme));
		this.themeData.get("Current").setIsModifiableByUser(true);
	}
}
