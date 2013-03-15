package u.mods.modify.permissions;



public class Permissions implements PermissionsInterface
{
	public static PermissionsInterface Instance = new Permissions();
	private final static String[] permissions = {"-umodify.*"};

	@Override
	public boolean hasPermission(String player, String permission)
	{
		return true;
	}
}
