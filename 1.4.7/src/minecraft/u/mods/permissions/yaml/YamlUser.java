package u.mods.permissions.yaml;

import java.util.ArrayList;
import java.util.List;

public class YamlUser
{
	private String	group;
	private List	permissions = new ArrayList();
	
	public String	getGroup()
	{ return this.group; }
	
	public void	setGroup(String group)
	{ this.group = group; }
	
	public List	getPermissions()
	{ return this.permissions; }
	
	public void	serPermissions(List permissions)
	{ this.permissions = permissions; }
}
