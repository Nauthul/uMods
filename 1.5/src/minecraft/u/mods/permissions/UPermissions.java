package u.mods.permissions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import u.mods.permissions.command.CommanduPerm;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.ModLoader;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Mod.ServerStarting;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.discovery.asm.ASMModParser;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(modid="uPermissions", name="uPermissions", version="1.0-Beta")
public class UPermissions
{
	public boolean						allowOps;
	public boolean						createUsersRecords;
	public boolean						debug;
	public PermissionsController		permController;
	public PermissionInvocationHandler	permHandler;

	@Instance("uPermissions")
	public static UPermissions	instance;
	
	public static PermissionsController	getController()
	{ return UPermissions.instance.permController; }
	
	@PreInit
	public void							preInit(FMLPreInitializationEvent event)
	{
		Configuration	config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		this.allowOps = config.get("Settings", "allowOps", true).getBoolean(true);
		this.createUsersRecords = config.get("Settings", "createUsersRecords", false).getBoolean(false);
		this.debug = config.get("Settings", "debug", false).getBoolean(false);
		config.save();
		this.permController = new PermissionsController();
		this.permHandler = new PermissionInvocationHandler();
		ArrayList<String> set = new ArrayList();
		for (ModContainer mod : Loader.instance().getActiveModList())
		{
			if (!set.contains(mod.getSource().getAbsolutePath()))
				set.add(mod.getSource().getAbsolutePath());
		}
		for (String path : set)
			checkLoadedModForPermissionClass(new File(path));
	}
	
	@ServerStarting
	public void 							serverStarting(FMLServerStartingEvent event)
	{
		MinecraftServer	server = ModLoader.getMinecraftServerInstance();
		ICommandManager commandManager = server.getCommandManager();
    	ServerCommandManager serverCommandManager = (ServerCommandManager)commandManager;
    	serverCommandManager.registerCommand(new CommanduPerm());
	}
	
	private void	checkLoadedModForPermissionClass(File modFile)
	{
		try {
			Pattern p = Pattern.compile("permission", Pattern.CASE_INSENSITIVE);
			if (modFile.isDirectory())
			{
				for (File file : modFile.listFiles())
				{
					checkLoadedModForPermissionClass(file);
				}
			}
			else if ((modFile.getName().endsWith(".zip")) || (modFile.getName().endsWith(".jar")))
			{
				ZipFile		zip = new ZipFile(modFile);
				Enumeration entries = zip.entries();
				while (entries.hasMoreElements())
				{
					ZipEntry zipentry = (ZipEntry)entries.nextElement();
					if (	zipentry != null &&
							zipentry.getName().endsWith(".class") &&
							p.matcher(zipentry.getName()).find())
					{
						checkLoadedClass(zip.getInputStream(zipentry));
					}
				}
				zip.close();
			}
			else if (modFile.getName().endsWith(".class") && p.matcher(modFile.getName()).find())
			{
				checkLoadedClass(new FileInputStream(modFile));
			}
		} catch (IOException e) {
		}
	}

	private void	checkLoadedClass(InputStream stream)
	{
		try {
			ASMModParser	parser = new ASMModParser(stream);
			Class	cls = Class.forName(parser.getASMType().getClassName(), false, Loader.instance().getModClassLoader());
			// Check if class has enough fields
			if (cls.getDeclaredFields().length < 2)
				return;
			Field field;
			Field perms;
			// Check if class has expected fields
			try {
				field = cls.getDeclaredField("Instance");
				perms = cls.getDeclaredField("permissions");
			} catch (Exception e) {
				return;
			}
			// Check if class inherit correctly and fields are of the right type
			if (cls.getInterfaces().length != 1 || field.getType() != cls.getInterfaces()[0] || !perms.getType().isArray() || perms.getType().getComponentType() != java.lang.String.class)
				return;
			// Check if class has minimum required method
			try {
				cls.getMethod("hasPermission", new Class[] {String.class, String.class});
			} catch (Exception e) {
				return;
			}
			Object instance = Proxy.newProxyInstance(cls.getClassLoader(), new Class[] { cls.getInterfaces()[0] }, this.permHandler);
			field.setAccessible(true);
			perms.setAccessible(true);
			try {
				field.set(null, instance);
				this.permController.setNativePermissions((String[])perms.get(null));
				System.out.println("[uPermissions] Loaded " + cls.getCanonicalName() + " permissions succesfully.");
			} catch (Exception e) {
				System.err.println("[uPermissions] Failed to load permissions from: " + cls.getCanonicalName());
				e.printStackTrace();
			}
		} catch (Exception e) {
		}
	}
}
