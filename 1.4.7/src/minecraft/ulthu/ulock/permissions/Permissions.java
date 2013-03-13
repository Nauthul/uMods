package ulthu.ulock.permissions;

public class Permissions implements PersissionsInterface {

	public static PersissionsInterface Instance = new Permissions();
	private final static String[] permissions = {"-ulock.*"};

	@Override
	public boolean hasPermission(String player, String permission) {
	return true;
	}
}
