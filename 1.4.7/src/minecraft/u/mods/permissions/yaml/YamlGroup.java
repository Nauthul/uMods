package u.mods.permissions.yaml;

import java.util.ArrayList;
import java.util.List;

public class YamlGroup
{
	private boolean 		isDefault = false;
	private String			prefix;
	private int				rank = 0;
	private List<String>	inheritance = new ArrayList<String>();
	private List<String>	permissions = new ArrayList<String>();
	
	public boolean		getDefault()
	{ return this.isDefault; }
	
	public void			setDefault(boolean isDefault)
	{ this.isDefault = isDefault; }
	
	public String		getPrefix()
	{ return this.prefix; }
	
	public void			setPrefix(String prefix)
	{ this.prefix = prefix; }
	
	public int			getRank()
	{ return this.rank; }
	
	public void			setRank(int rank)
	{ this.rank = rank; }
	
	public List<String>	getInheritance()
	{ return this.inheritance; }
	
	public void			setInheritance(List<String> inheritance)
	{ this.inheritance = inheritance; }
	
	public List<String>	getPermissions()
	{ return this.permissions; }
	
	public void 		setPermissions(List<String> permissions)
	{ this.permissions = permissions; }
}
