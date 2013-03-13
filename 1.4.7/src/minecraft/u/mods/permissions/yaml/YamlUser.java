package u.mods.permissions.yaml;

import java.util.ArrayList;
import java.util.List;

public class YamlUser
{
	private String			group;
	private List<String>	permissions = new ArrayList<String>();
	
	public String		getGroup()
	{ return this.group; }
	
	public void			setGroup(String group)
	{ this.group = group; }
	
	public List<String>	getPermissions()
	{ return this.permissions; }
	
	public void			serPermissions(List<String> permissions)
	{ this.permissions = permissions; }
}
