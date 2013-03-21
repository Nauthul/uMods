package u.mods.core.utils;

public enum ChatColor
{
	Black		("\u00a7", "0"),
	DarkBlue	("\u00a7", "1"),
	DarkGreen	("\u00a7", "2"),
	DarkAqua	("\u00a7", "3"),
	DarkRed		("\u00a7", "4"),
	Purple		("\u00a7", "5"),
	Gold		("\u00a7", "6"),
	Gray		("\u00a7", "7"),
	DarkGray	("\u00a7", "8"),
	Blue		("\u00a7", "9"),
	Green		("\u00a7", "a"),
	Aqua		("\u00a7", "b"),
	Red			("\u00a7", "c"),
	LightPurple	("\u00a7", "d"),
	Yellow		("\u00a7", "e"),
	White		("\u00a7", "f");
	
	private final String	spchar;
	private final String	color;

	ChatColor(String spchar, String color)
	{
		this.spchar = spchar;
		this.color = color;
	}
	
	@Override
	public String	toString()
	{
		return this.spchar + this.color;
	}
	
	public static String	colorizeAll(String str)
	{
		if (str == null)
			return str;
		for (ChatColor c : ChatColor.values())
		{
			String search = "&" + c.color;
			String replace = c.toString();
			str = str.replace(search, replace);
		}
		return str;
	}
}
