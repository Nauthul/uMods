 package u.mods.permissions.yaml;

import java.util.LinkedHashMap;
import java.util.Map;

public class YamlPermissions
{
	private Map	groups = new LinkedHashMap();
	private Map	users = new LinkedHashMap();
	
	public Map	getGroups()
	{ return this.groups; }
	
	public void setGroups(Map groups)
	{ this.groups = groups; }
	
	public Map getUsers()
	{ return this.users; }
	
	public void	setUsers(Map users)
	{ this.users = users; }
}
