package u.mods.lock.permissions;

public class Permissions implements PermissionsInterface {

	public static PermissionsInterface Instance = new Permissions();
	private final static String[] permissions = {"-ulock.*"};

	@Override
	public boolean hasPermission(String player, String permission) {
	return true;
	}
}
