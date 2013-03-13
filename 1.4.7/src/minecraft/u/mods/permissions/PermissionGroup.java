package u.mods.permissions;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import u.mods.permissions.yaml.YamlGroup;
import u.mods.permissions.yaml.YamlUser;

public class PermissionGroup
{
	public HashMap<String, Boolean>	permissions;
	public List<String>				inheritance;
	
	public	PermissionGroup(YamlGroup group)
	{
		addPermissions(group.getPermissions());
		this.inheritance = group.getInheritance();
	}
	
	public	PermissionGroup(YamlUser user)
	{
		addPermissions(user.getPermissions());
	}
	
	public boolean	hasPermission(String permission)
	{
		if (this.permissions.containsKey(permission))
			return (this.permissions.get(permission));
		for (String perm : this.permissions.keySet())
		{
			if (permission.matches(perm))
				return (this.permissions.get(perm));
		}
		for (String parent : this.inheritance)
		{
			if (UPermissions.instance.permController.hasGroupPermission(parent, permission))
				return true;
		}
		return false;
	}
	
	private void	addPermissions(List<String> permissions)
	{
		for (String perm : permissions)
		{
			boolean value = true;
			if (perm.startsWith("-"))
			{
				value = false;
				perm = perm.substring(1);
			}
			this.permissions.put(perm, value);
		}
	}
}
