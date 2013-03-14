package u.mods.permissions;

import u.mods.permissions.yaml.YamlUser;

public class PermissionUser extends PermissionAbstract
{
	public	PermissionUser(String name, YamlUser user)
	{
		this.name = name;
		this.inheritance.add(user.getGroup());
		this.permissions = analyzePermissions(user.getPermissions());
	}
	
	public void		setGroup(String group)
	{
		this.inheritance.clear();
		this.inheritance.add(group);
	}
	
	public YamlUser	toYaml()
	{
		YamlUser	user = new YamlUser();
		if (!this.inheritance.isEmpty())
			user.setGroup(this.inheritance.get(0));
		user.serPermissions(this.permissionsToYaml(this.permissions));
		return user;
	}
}
