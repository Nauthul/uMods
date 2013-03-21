package u.mods.chat.permissions;

public class Permissions implements PermissionsInterface
{
	public static PermissionsInterface Instance = new Permissions();
	private final static String[] permissions = {"-uchat.*"};
	
	@Override
	public boolean hasPermission(String player, String permission)
	{
		return true;
	}
	
	@Override
	public String	getUserGroup(String user)
	{
		return "";
	}
	
	@Override
	public String	getGroupPrefix(String group)
	{
		return "";
	}
}
