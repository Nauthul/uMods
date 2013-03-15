 package u.mods.permissions.yaml;

import java.util.LinkedHashMap;
import java.util.Map;

public class YamlPermissions
{
	private Map<String, YamlGroup>	groups = new LinkedHashMap<String, YamlGroup>();
	private Map<String, YamlUser>	users = new LinkedHashMap<String, YamlUser>();
	
	public Map<String, YamlGroup>	getGroups()
	{ return this.groups; }
	
	public void 					setGroups(Map<String, YamlGroup> groups)
	{ this.groups = groups; }
	
	public Map<String, YamlUser> 	getUsers()
	{ return this.users; }
	
	public void						setUsers(Map<String, YamlUser> users)
	{ this.users = users; }
}
