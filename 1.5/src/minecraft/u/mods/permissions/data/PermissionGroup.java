package u.mods.permissions.data;

import u.mods.permissions.yaml.YamlGroup;

public class PermissionGroup extends PermissionAbstract
{
	private boolean	isDefault = false;
	private int		rank = 0;
	private String	prefix = "";
	
	public	PermissionGroup(String name, YamlGroup group)
	{
		this.name = name;
		this.isDefault = group.getDefault();
		this.rank = group.getRank();
		this.prefix = group.getPrefix();
		this.inheritance = group.getInheritance();
		this.permissions = analyzePermissions(group.getPermissions());
	}
	
	public YamlGroup	toYaml()
	{
		YamlGroup	group = new YamlGroup();
		group.setDefault(this.isDefault);
		group.setPrefix(this.prefix);
		group.setRank(this.rank);
		group.setInheritance(this.inheritance);
		group.setPermissions(this.permissionsToYaml(this.permissions));
		return group;
	}
	
	public int			getRank()
	{ return this.rank; }
	
	public String		getPrefix()
	{ return this.prefix; }
	
	/*
	public HashMap<String, Boolean>	permissions = new LinkedHashMap<String, Boolean>();
	public List<String>				inheritance = new ArrayList<String>();
	
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
	*/
}
