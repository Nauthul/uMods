package u.mods.permissions.permissions;

public class ModPermissions implements ModPermissionsInterface
{
	public static ModPermissionsInterface Instance = new ModPermissions();
	private final static String[] permissions = {"-uperm.*"};
	
	@Override
	public boolean hasPermission(String player, String permission)
	{
		return true;
	}

}
