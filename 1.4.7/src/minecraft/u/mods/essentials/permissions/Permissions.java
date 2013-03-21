package u.mods.essentials.permissions;

public class Permissions implements PermissionsInterface
{
	public static PermissionsInterface	Instance = new Permissions();
	private final static String[] permissions = {"-uessentials.*", "-uhome.*"};

	@Override
	public boolean hasPermission(String player, String permission)
	{
		return true;
	}
}
