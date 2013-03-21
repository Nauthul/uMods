package u.mods.chat.permissions;

public interface PermissionsInterface
{
	public boolean	hasPermission(String player, String permission);
	public String	getUserGroup(String user);
	public String	getGroupPrefix(String group);
}
