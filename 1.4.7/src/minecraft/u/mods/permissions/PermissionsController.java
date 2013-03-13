package u.mods.permissions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.minecraft.server.MinecraftServer;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;

import u.mods.permissions.yaml.YamlGroup;
import u.mods.permissions.yaml.YamlPermissions;
import u.mods.permissions.yaml.YamlUser;

public class PermissionsController
{
	private HashMap<String, PermissionGroup>	groupPermissions;
	private HashMap<String, PermissionGroup>	userPermissions;
	private HashMap<String, String>				userGroup;
	private TreeMap<Integer, String>			groupsLadder;
	private String								defaultGroup;
	private File								permissionsFile;
	private YamlPermissions						yamlPerms;

	public	PermissionsController()
	{
		this.reload();
	}

	/*
	 *  General Commands
	 */
	public void				reload()
	{
		this.groupPermissions = new HashMap();
		this.userPermissions = new HashMap();
		this.userGroup = new HashMap();
		this.groupsLadder = new TreeMap();
		File permissionsFile = new File(Loader.instance().getConfigDir(), "permissions.yml");
		if (permissionsFile.exists())
			loadPermissions(permissionsFile);
		else
			createDefaultConfigFile();
	}
	
	private void			initialize()
	{
		this.groupPermissions = new HashMap();
		this.userPermissions = new HashMap();
		this.userGroup = new HashMap();
		this.groupsLadder = new TreeMap();
		this.defaultGroup = "";
		this.permissionsFile = new File(Loader.instance().getConfigDir(), "permissions.yml");
	}

//	public List<String>		getHierarchy()
//	{}
	/*
	 *  Users Management
	 */
	public void 			addUser(String user) throws Exception
	{}
	public void 			delUser(String user) throws Exception
	{}
//	public List<String> 	listUsers()
//	{}
	
	/*
	 *  Users Permissions Management
	 */
	public void 			addUserPerm(String user, String permission) throws Exception
	{}
	public void 			delUserPerm(String user, String permission) throws Exception
	{}
//	public boolean 			checkUserPerm(String user, String permission)
//	{}
//	public List<String> 	listUserPerms(String user)
//	{}
	
	/*
	 *  Users Group Management
	 */
//	public String 			getUserGroup(String user)
//	{}
	public void 			setUserGroup(String user, String group) throws Exception
	{}
	
	/*
	 *  Groups Management
	 */
	public void 			addGroup(String group) throws Exception
	{}
	public void 			delGroup(String group) throws Exception
	{}
//	public List<String> 	listGroups()
//	{}
	
	/*
	 *  Groups Permissions Management
	 */
	public void 			addGroupPerm(String group, String permission) throws Exception
	{}
	public void 			delGroupPerm(String group, String permission) throws Exception
	{}
//	public boolean 			checkGroupPerm(String group, String permission)
//	{}
//	public List<String> 	listGroupPerms(String group)
//	{}
	
	/*
	 *  Group Management
	 */
//	public List<String> 	listGroupUsers(String group)
//	{}
//	public String 			getGroupPrefix(String group) throws Exception
//	{}
	public void 			setGroupPrefix(String group, String prefix) throws Exception
	{}
//	public int 				getGroupRank(String group) throws Exception
//	{}
	public void 			setGroupRank(String group, int rank) throws Exception
	{}
//	public String 			getDefaultGroup() throws Exception
//	{}
	public void 			setDefaultGroup(String group) throws Exception
	{}
	
	/*
	 *  Ladder Management
	 */
	public void 			promote(String user) throws Exception
	{}
	public void 			demote(String user) throws Exception
	{}
//	public List<String> 	getLadder()
//	{}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
/*
 * 	
 */
	public List<String>	getFormatedUsers()
	{
		List<String>	users = new ArrayList();
		
		users.add("users:");
		for (String name : this.userGroup.keySet())
		{
			users.add(" " + name + " " + "\u00a72" + "[" + this.userGroup.get(name) + "]");
		}
		
		return users;
	}

	public List<String>	getFormatedGroups()
	{
		List<String>	groups = new ArrayList();
		groups.add("groups:");
		for (String name : this.groupPermissions.keySet())
		{
			String inheritance = "[";
			int count = 0;
			for (String i : this.groupPermissions.get(name).inheritance)
			{
				if (count != 0)
					inheritance += ", ";
				inheritance += i;
			}
			inheritance += "]";
			String rank = String.valueOf(((YamlGroup)this.yamlPerms.getGroups().get(name)).getRank());
			groups.add(" " + name + " (rank: " + rank + ") " + "\u00a72" + inheritance);			
		}
		return groups;
	}
	
	public List	getFormatedHierarchy()
	{
		List<String>	hierarchy = new ArrayList();
		
		if (this.groupsLadder.size() > 0)
		{
			int key = this.groupsLadder.lastKey();
			boolean c = true;
			do {
				String name = this.groupsLadder.get(key);
				hierarchy.add("- " + name + ":");
				
				for (String uname : this.userGroup.keySet())
				{
					if (this.userGroup.get(uname).equals(name))
						hierarchy.add(" + " + uname);
				}
				
				if (this.groupsLadder.lowerKey(key) == null)
					c = false;
				else
					key = this.groupsLadder.lowerKey(key);
			}
			while (c);
		}
		return hierarchy;
	}
	
	
	
	/*
	 *  Permissions Check
	 */
	
	public boolean			hasPermission(String user, String permission)
	{
		if (isOp(user))
			return true;
		if (this.userPermissions.containsKey(user) && this.userPermissions.get(user).hasPermission(permission))
			return true;
		if (this.userGroup.containsKey(user) && hasGroupPermission(this.userGroup.get(user), permission))
			return true;
		return false;
	}
	
	public boolean			hasGroupPermission(String group, String permission)
	{
		if (this.groupPermissions.containsKey(group))
			return this.groupPermissions.get(group).hasPermission(permission);
		return false;
	}
	
	private boolean			isOp(String user)
	{
		if (!UPermissions.instance.allowOps)
			return false;
		MinecraftServer	server = MinecraftServer.getServer();
		return ((server != null) && (server.getConfigurationManager().getOps().contains(user)));
	}

	/*
	 *  Configuration Management
	 */
	
	public void				loadPermissions(File permissionsFile)
	{
		this.yamlPerms = loadFromYaml(permissionsFile);
		
		LinkedHashMap<String, YamlGroup> groups = (LinkedHashMap<String, YamlGroup>)this.yamlPerms.getGroups();
		for (String name : groups.keySet())
		{
			YamlGroup g = groups.get(name);
			this.groupPermissions.put(name, new PermissionGroup(g));
			if (g.getRank() > 0)
				groupsLadder.put(g.getRank(), name);
		}
		
		LinkedHashMap<String, YamlUser> users = (LinkedHashMap<String, YamlUser>)this.yamlPerms.getUsers();
		for (String name : users.keySet())
		{
			YamlUser u = users.get(name);
			this.userPermissions.put(name, new PermissionGroup(u));
			this.userGroup.put(name, u.getGroup());
		}
	}
	
	private YamlPermissions	loadFromYaml(File permissionsFile)
	{
		Constructor		constructor = new Constructor(YamlPermissions.class);
		
		TypeDescription	permDescrip = new TypeDescription(YamlPermissions.class); 
		permDescrip.putMapPropertyType("groups", String.class, YamlGroup.class);
		permDescrip.putMapPropertyType("users", String.class, YamlUser.class);
		constructor.addTypeDescription(permDescrip);

		Representer		rep = new Representer();
		rep.addClassTag(YamlGroup.class, Tag.MAP);
		rep.addClassTag(YamlUser.class, Tag.MAP);
		
		PropertyUtils	prop = new PropertyUtils();
		prop.setSkipMissingProperties(true);
		constructor.setPropertyUtils(prop);
		
		DumperOptions	options = new DumperOptions();
		options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		options.setDefaultScalarStyle(DumperOptions.ScalarStyle.PLAIN);
		options.setCanonical(false);

		try {
			Yaml	yaml = new Yaml(constructor, rep, options);
			FileInputStream	stream = new FileInputStream(permissionsFile);
			YamlPermissions	p = (YamlPermissions)yaml.load(stream);
			return p;
		} catch (FileNotFoundException e) {
			//FMLLog.warning("", new Object());
		}
		return null;
	}
	
	public void				savePermissions()
	{
		saveToYaml();
	}
	
	private void			saveToYaml()
	{
		Constructor	constructor = new Constructor(YamlPermissions.class);
		
		TypeDescription	permDescription = new TypeDescription(YamlPermissions.class);
		permDescription.putMapPropertyType("groups", String.class, YamlGroup.class);
		permDescription.putMapPropertyType("users", String.class, YamlUser.class);
		constructor.addTypeDescription(permDescription);
		
		DumperOptions	options = new DumperOptions();
		options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		options.setDefaultScalarStyle(DumperOptions.ScalarStyle.PLAIN);
		options.setCanonical(false);
		
		Representer	rep = new Representer()
		{
	    	protected NodeTuple representJavaBeanProperty(Object javaBean, Property property, Object propertyValue, Tag customTag)
	    	{
	    		if (	(propertyValue == null) ||
	    				(propertyValue.toString().isEmpty()) ||
	    				((propertyValue instanceof Boolean) && (!((Boolean)propertyValue).booleanValue())) ||
	    				((propertyValue instanceof Integer) && ((Integer)propertyValue).intValue() == 0) ||
	    				((propertyValue instanceof List) && (((List)propertyValue).size() <= 0)))
	    		{ return null; }
	    		return super.representJavaBeanProperty(javaBean, property, propertyValue, customTag);
	    	}
	    };
	    rep.addClassTag(YamlGroup.class, Tag.MAP);
	    rep.addClassTag(YamlUser.class, Tag.MAP);
	    
	    Yaml yaml = new Yaml(constructor, rep, options);
	    try
	    {
	    	File permissionFile = new File(Loader.instance().getConfigDir(), "permissions.yml");
	    	BufferedWriter out = new BufferedWriter(new FileWriter(permissionFile));
	    	out.write(yaml.dumpAsMap(this.yamlPerms));
	    	out.close();
	    }
	    catch (IOException e)
	    {
	    	e.printStackTrace();
	    }
	}

	private void			createDefaultConfigFile()
	{
		this.yamlPerms = new YamlPermissions();
		
		Map			groups = this.yamlPerms.getGroups();
		YamlGroup	guest = new YamlGroup();
		guest.setDefault(true);
		guest.setPrefix("[Guest]");
		guest.setRank(1000);
		groups.put("guest", guest);
		YamlGroup	member = new YamlGroup();
		member.setPrefix("[Member]");
		member.setRank(999);
		member.getPermissions().add("example.permission");
		member.getPermissions().add("-example.permission.negative");
		groups.put("member", member);
		YamlGroup	admin = new YamlGroup();
		admin.setPrefix("[Admin]");
		admin.setRank(1);
		admin.getPermissions().add("*");
		groups.put("admin", admin);
		
		Map 		users = this.yamlPerms.getUsers();
		YamlUser	notch = new YamlUser();
		notch.setGroup("guest");
		users.put("notch", notch);
		saveToYaml();
	}
}
