package u.mods.permissions.yaml;

import java.util.ArrayList;
import java.util.List;

public class YamlGroup
{
	private boolean isDefault = false;
	private String	prefix;
	private int		rank = 0;
	private List	inheritance = new ArrayList();
	private List	permissions = new ArrayList();
	
	public boolean	getDefault()
	{ return this.isDefault; }
	
	public void	setDefault(boolean isDefault)
	{ this.isDefault = isDefault; }
	
	public String	getPrefix()
	{ return this.prefix; }
	
	public void	setPrefix(String prefix)
	{ this.prefix = prefix; }
	
	public int	getRank()
	{ return this.rank; }
	
	public void	setRank(int rank)
	{ this.rank = rank; }
	
	public List	getInheritance()
	{ return this.inheritance; }
	
	public void	setInheritance(List inheritance)
	{ this.inheritance = inheritance; }
	
	public List	getPermissions()
	{ return this.permissions; }
	
	public void setPermissions(List permissions)
	{ this.permissions = permissions; }
}
