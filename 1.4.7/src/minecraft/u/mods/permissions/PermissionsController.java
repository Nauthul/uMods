package u.mods.permissions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
	private HashMap	groupsPermissions = new HashMap();
	private HashMap usersGroups = new HashMap();
	private HashMap userPermissions = new HashMap();
	private YamlPermissions	yamlPerms;

	public PermissionsController()
	{
		File permissionsFile = new File(Loader.instance().getConfigDir(), "permissions.yml");
		if (permissionsFile.exists())
			loadPermissions(permissionsFile);
		else
		{}
	}
	
	public void	loadPermissions(File permissionsFile)
	{
		this.yamlPerms = loadFromYaml(permissionsFile);
		//for (String name : this.yamlPerms.getGroups().keySet())
		//{}
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
	
	public void	savePermissions()
	{
		//FileWriter fstream = new FileWriter("outPerm.txt");
				//yaml.dump(p, fstream);
	}
	
	private void	saveToYaml()
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
}
