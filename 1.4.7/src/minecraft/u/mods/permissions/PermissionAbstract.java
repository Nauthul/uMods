package u.mods.permissions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import cpw.mods.fml.common.FMLLog;

public abstract class PermissionAbstract
{
	protected String					name = "";
	protected List<String>				inheritance = new ArrayList<String>();
	protected HashMap<String, Boolean>	permissions = new LinkedHashMap<String, Boolean>();
	protected HashMap<String, Boolean>	cachedPermissions = new LinkedHashMap<String, Boolean>();
	
	public	String						getName()
	{ return this.name; }
	
	public	List<String>				getInheritance()
	{ return this.inheritance; }
	
	public boolean						hasPermission(String permission) throws PermissionNotFoundException
	{
		boolean	result = false;
		try {
			result = this.hasPermission(permission, new HashSet<String>());
		} catch (PermissionNotFoundException e) {
			throw e;
		}
		return result;
	}
	
	public boolean						hasPermission(String permission, Set<String> pastNodes) throws PermissionNotFoundException
	{
		if (pastNodes.contains(this.name))
			throw new PermissionNotFoundException();
		if (this.cachedPermissions.containsKey(permission))
			return this.cachedPermissions.get(permission);
		for (String key : this.permissions.keySet())
		{
			if (key.equals("*"))
				return true;
			if (permission.matches(key))
			{
				this.cachedPermissions.put(permission, this.permissions.get(key));
				return this.permissions.get(key);
			}
		}
		pastNodes.add(this.name);
		for (String parent : this.inheritance)
		{
			PermissionAbstract	gp = UPermissions.getController().getGroupPermissions(parent);
			if (gp != null)
			{
				try {
					boolean result = gp.hasPermission(permission, pastNodes);
					return result;
				} catch (PermissionNotFoundException e) {
				}
			}
		}
		throw new PermissionNotFoundException();
	}
	
	protected HashMap<String, Boolean>	analyzePermissions(List<String> permissions)
	{
		HashMap<String, Boolean>	perms = new LinkedHashMap<String, Boolean>();
		for (String p : permissions)
		{
			if (p.startsWith("*") && p.length() > 1)
				System.out.println("[uPermissions] Invalid permission node: " + p);
			else
			{
				boolean	value = true;
				if (p.startsWith("-"))
				{
					value = false;
					p = p.substring(1);
				}
				if (perms.containsKey(p))
					System.out.println("[uPermissions] Redundant permission node: " + p);
				else
					perms.put(p, value);
			}
		}
		return perms;
	}
	
	protected List<String>				permissionsToYaml(HashMap<String, Boolean> permissions)
	{
		List<String>	perms = new ArrayList<String>();
		for (String p : permissions.keySet())
		{
			if (permissions.get(p) == false)
				p = "-" + p;
			perms.add(p);
		}
		return perms;
	}
}
