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
import java.util.Set;
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

import cpw.mods.fml.common.Loader;

import u.mods.permissions.data.PermissionGroup;
import u.mods.permissions.data.PermissionUser;
import u.mods.permissions.exceptions.PermissionNotFoundException;
import u.mods.permissions.yaml.YamlGroup;
import u.mods.permissions.yaml.YamlPermissions;
import u.mods.permissions.yaml.YamlUser;

public class PermissionsController
{
	private HashMap<String, PermissionGroup>	groupPermissions;
	private HashMap<String, PermissionUser>		userPermissions;
	private HashMap<String, String>				userGroup;
	private HashMap<String, Boolean>			nativePermissions = new HashMap<String, Boolean>();
	private TreeMap<Integer, String>			groupsLadder;
	private String								defaultGroup;
	private File								permissionsFile;

	public	PermissionsController()
	{
		initialize();
		load();
	}

	/*
	 *  General Commands
	 */
	public void				load()
	{
		if (this.permissionsFile.exists())
		{
			System.out.println("[uPermissions] Loading permissions from file.");
			loadPermissions();
		}
		else
		{
			System.out.println("[uPermissions] Can't find permissions file, creating default.");
			createDefaultConfigFile();
		}
		System.out.println("[uPermissions] Permissions loaded.");
	}
	
	public void				reload()
	{
		System.out.println("[uPermissions] Permissions reload requested.");
		initialize();
		load();
	}
	
	private void			initialize()
	{
		this.groupPermissions = new HashMap<String, PermissionGroup>();
		this.userPermissions = new HashMap<String, PermissionUser>();
		this.userGroup = new HashMap<String, String>();
		this.groupsLadder = new TreeMap<Integer, String>();
		this.defaultGroup = "";
		this.permissionsFile = new File(Loader.instance().getConfigDir(), "permissions.yml");
	}

	/*
	 *  Permissions File Management
	 */
	public void				loadPermissions()
	{
		YamlPermissions	yamlPerms = loadFromYaml(this.permissionsFile);
		
		LinkedHashMap<String, YamlGroup> groups = (LinkedHashMap)yamlPerms.getGroups();
		for (String name : groups.keySet())
		{
			YamlGroup g = groups.get(name);
			name = name.toLowerCase();
			this.groupPermissions.put(name, new PermissionGroup(name, g));
			if (g.getRank() > 0)
			{
				if (this.groupsLadder.containsKey(g.getRank()))
					System.err.println("[uPermissions] Group rank redundancy detected. Assuming first one.");
				else
					groupsLadder.put(g.getRank(), name);
			}
			if (g.getDefault())
			{
				if (this.defaultGroup.equals(""))
					this.defaultGroup = name;
				else
					System.err.println("[uPermissions] Default group redundancy detected. Assuming first one.");
			}
		}
		
		LinkedHashMap<String, YamlUser> users = (LinkedHashMap)yamlPerms.getUsers();
		for (String name : users.keySet())
		{
			YamlUser u = users.get(name);
			name = name.toLowerCase();
			this.userPermissions.put(name, new PermissionUser(name, u));
			if (u.getGroup() != null)
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
		}
		return null;
	}
	
	public void				savePermissions()
	{
		YamlPermissions	yamlPerms = new YamlPermissions();
		
		Map<String, YamlGroup>	groups = new LinkedHashMap<String, YamlGroup>();
		for (String name : this.groupPermissions.keySet())
			groups.put(name, this.groupPermissions.get(name).toYaml());
		yamlPerms.setGroups(groups);
		
		Map<String, YamlUser>	users = new LinkedHashMap<String, YamlUser>();
		for (String name : this.userPermissions.keySet())
			users.put(name, this.userPermissions.get(name).toYaml());
		yamlPerms.setUsers(users);
		
		System.out.println("[uPermissions] Saving permissions to file.");
		saveToYaml(yamlPerms);
		
	}
	
	private void			saveToYaml(YamlPermissions yamlPerms)
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
	    	BufferedWriter out = new BufferedWriter(new FileWriter(this.permissionsFile));
	    	out.write(yaml.dumpAsMap(yamlPerms));
	    	out.close();
	    	System.out.println("[uPermissions] Permissions saved.");
	    }
	    catch (IOException e)
	    {
	    	System.err.println("[uPermissions] Saving failed, unable to write to file!");
	    }
	}

	private void			createDefaultConfigFile()
	{
		YamlPermissions	yamlPerms = new YamlPermissions();
		
		Map<String, YamlGroup>	groups = yamlPerms.getGroups();
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
		
		Map<String, YamlUser>	users = yamlPerms.getUsers();
		YamlUser	notch = new YamlUser();
		notch.setGroup("guest");
		users.put("notch", notch);

		saveToYaml(yamlPerms);
	}
	
	/*
	 *  Getters
	 */
	public PermissionGroup	getGroupPermissions(String group)
	{
		if (this.groupPermissions.containsKey(group))
			return this.groupPermissions.get(group);
		return null;
	}
	
	public Set<String>		getGroups()
	{ return this.groupPermissions.keySet(); }
	
	public String			getGroupPrefix(String group)
	{
		if (this.groupPermissions.containsKey(group))
			return this.groupPermissions.get(group).getPrefix();
		return "";
	}

	public String			getUserGroup(String user)
	{
		if (this.userGroup.containsKey(user))
			return this.userGroup.get(user);
		return this.defaultGroup;
	}
	
	/*
	 *  Setters
	 */
	public void				setNativePermissions(String[] permissions)
	{
		for (String perm : permissions)
		{
			boolean value = true;
			if (perm.startsWith("-"))
			{
				value = false;
				perm = perm.substring(1);
			}
			if (!this.nativePermissions.containsKey(perm))
				this.nativePermissions.put(perm, value);
		}
	}
	
	/*
	 *  Permissions Check
	 */

	public boolean			hasPermission(String user, String permission)
	{
		try {
			boolean	result = hasPermissionThrow(user, permission);
			return result;
		} catch (PermissionNotFoundException e) {
		}
		return false;
	}
	
	public boolean			hasPermissionThrow(String user, String permission) throws PermissionNotFoundException
	{
		user = user.toLowerCase();
		permission = permission.toLowerCase();

		if (isOp(user))
			return true;
		if (this.userPermissions.containsKey(user))
		{
			try {
				boolean result = this.userPermissions.get(user).hasPermission(permission);
				return result;
			} catch (PermissionNotFoundException e) {
			}
		}
		if (this.userGroup.containsKey(user))
		{
			String group = this.userGroup.get(user);
			if (this.groupPermissions.containsKey(group))
			{
				try {
					boolean result = this.groupPermissions.get(group).hasPermission(permission);
					return result;
				} catch (PermissionNotFoundException e) {
				}
			}
		}
		else if (this.defaultGroup != null && !this.defaultGroup.equals("") && this.groupPermissions.containsKey(this.defaultGroup))
		{
			try {
				boolean result = this.groupPermissions.get(this.defaultGroup).hasPermission(permission);
				return result;
			} catch (PermissionNotFoundException e) {
			}
		}
		throw new PermissionNotFoundException();
	}
		
	private boolean			isOp(String user)
	{
		if (!UPermissions.instance.allowOps)
			return false;
		MinecraftServer	server = MinecraftServer.getServer();
		return ((server != null) && (server.getConfigurationManager().getOps().contains(user)));
	}
	
	/*
	 *  Users Management
	 */
	public String 			addUser(String user)
	{
		user = user.toLowerCase();
		if (this.userPermissions.containsKey(user))
			return "User already exist.";
		this.userPermissions.put(user, new PermissionUser(user, new YamlUser()));
		savePermissions();
		return "Added user " + user + " to permissions.";
	}

	//	public void 			delUser(String user) throws Exception
//	{}
	
	public List<String> 	listUsers()
	{
		List<String>	users = new ArrayList();
		
		users.add("users:");
		for (String name : this.userGroup.keySet())
		{
			users.add(" " + name + " " + "\u00a72" + "[" + this.userGroup.get(name) + "]");
		}
		
		return users;
	}
	
	/*
	 *  Users Permissions Management
	 */
//	public String 			addUserPerm(String user, String permission)
//	{}
//	public void 			delUserPerm(String user, String permission) throws Exception
//	{}
	
	public String 			checkUserPerm(String user, String permission)
	{
		String res;
		try {
			boolean	result = hasPermissionThrow(user, permission);
			res = "Player \"" + user + "\" have \"" + (result ? "" : "-") + permission + "\" = " + String.valueOf(result);
			return res;
		} catch (PermissionNotFoundException e) {
		}
		res = "Player \"" + user + "\" don't have such permission.";
		return res;
	}
	
//	public List<String> 	listUserPerms(String user)
//	{
//		user = user.toLowerCase();
//
//		List<String>	perms = new ArrayList();
//		perms.add("Permissions for " + user + ":");
//		if (this.userPermissions.containsKey(user))
//		{
//			
//		}
//		return perms;
//	}
	
	/*
	 *  Users Group Management
	 */
//	public String 			getUserGroup(String user)
//	{}
	
	public String			setUserGroup(String user, String group)
	{
		user = user.toLowerCase();
		group = group.toLowerCase();
		
		if (!this.groupPermissions.containsKey(group))
			return "Group " + group + " does not exist.";
		if (!this.userPermissions.containsKey(user))
			addUser(user);
		this.userPermissions.get(user).setGroup(group);
		this.userGroup.put(user, group);
		savePermissions();
		return "User " + user + " is now a member of the group " + group + ".";
	}
	
	/*
	 *  Groups Management
	 */
//	public void 			addGroup(String group) throws Exception
//	{}
//	public void 			delGroup(String group) throws Exception
//	{}
	
	public List<String> 	listGroups()
	{
		List<String>	groups = new ArrayList();
		groups.add("groups:");
		for (String name : this.groupPermissions.keySet())
		{
			String inheritance = "[";
			int count = 0;
			for (String i : this.groupPermissions.get(name).getInheritance())
			{
				if (count != 0)
					inheritance += ", ";
				inheritance += i;
			}
			inheritance += "]";
			String rank = String.valueOf(this.groupPermissions.get(name).getRank());
			groups.add(" " + name + " (rank: " + rank + ") " + "\u00a72" + inheritance);			
		}
		return groups;
	}
	
	/*
	 *  Groups Permissions Management
	 */
//	public void 			addGroupPerm(String group, String permission) throws Exception
//	{}
//	public void 			delGroupPerm(String group, String permission) throws Exception
//	{}
	
	public boolean 			checkGroupPerm(String group, String permission)
	{
		group = group.toLowerCase();
		permission = permission.toLowerCase();
		
		if (this.groupPermissions.containsKey(group))
		{
			try {
				boolean result = this.groupPermissions.get(group).hasPermission(permission);
				return result;
			} catch (PermissionNotFoundException e) {
			}
		}
		return false;
	}
	
//	public List<String> 	listGroupPerms(String group)
//	{}
	
	/*
	 *  Group Management
	 */
//	public List<String> 	listGroupUsers(String group)
//	{}
//	public String 			getGroupPrefix(String group) throws Exception
//	{}
//	public void 			setGroupPrefix(String group, String prefix) throws Exception
//	{}
//	public int 				getGroupRank(String group) throws Exception
//	{}
//	public void 			setGroupRank(String group, int rank) throws Exception
//	{}
//	public String 			getDefaultGroup() throws Exception
//	{}
//	public void 			setDefaultGroup(String group) throws Exception
//	{}
	
	/*
	 *  Ladder Management
	 */
	public String 			promote(String user)
	{
		String group = this.defaultGroup;
		user = user.toLowerCase();
		if (this.userGroup.containsKey(user))
			group = this.userGroup.get(user);
		int rank = 0;
		if (this.groupPermissions.containsKey(group))
			rank = this.groupPermissions.get(group).getRank();
		else
			return "User group is not valid.";
		if (rank < 1 || !this.groupsLadder.containsKey(rank) || !this.groupsLadder.get(rank).equals(group))
			return "User group is not in ladder.";
		if (this.groupsLadder.lowerKey(rank) != null)
		{
			rank = this.groupsLadder.lowerKey(rank);
			group = this.groupsLadder.get(rank);
			return "Promotion: " + setUserGroup(user, group);
		}
		else
			return "User is already in the top ranked group.";
	}
	
	public String 			demote(String user)
	{
		user = user.toLowerCase();
		if (!this.userPermissions.containsKey(user))
			return "User is not yet registered in permissions.";
		if (!this.userGroup.containsKey(user) || this.userGroup.get(user) == null)
			return "User is not in a group.";
		String group = this.userGroup.get(user);
		if (!this.groupPermissions.containsKey(group))
			return "User group is not valid.";
		int rank = this.groupPermissions.get(group).getRank();
		if (rank < 1 || !this.groupsLadder.containsKey(rank) || !this.groupsLadder.get(rank).equals(group))
			return "User group is not in ladder.";
		if (this.groupsLadder.higherKey(rank) != null)
		{
			rank = this.groupsLadder.higherKey(rank);
			group = this.groupsLadder.get(rank);
			return "Demotion: " + setUserGroup(user, group);
		}
		else
			return "User is already in the lowest ranked group.";
		
	}
	
	public List<String> 	getLadder()
	{
		List<String>	ladder = new ArrayList();
		ladder.add("Ladder:");
		for (int i : this.groupsLadder.keySet())
			ladder.add("- " + String.valueOf(i) + " - " + this.groupsLadder.get(i));
		return ladder;
	}
	
	public List<String>		getHierarchy()
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
}
