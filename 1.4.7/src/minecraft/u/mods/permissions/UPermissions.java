package u.mods.permissions;

import u.mods.permissions.command.CommanduPerm;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.ModLoader;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Mod.ServerStarting;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(modid="uPermissions", name="uPermissions", version="1.0")
public class UPermissions
{
	public boolean	allowOps;
	public boolean	createUsersRecords;
	public boolean	debug;
	public PermissionsController	permController;

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
	}
	
	@ServerStarting
	public void 							serverStarting(FMLServerStartingEvent event)
	{
		MinecraftServer	server = ModLoader.getMinecraftServerInstance();
		ICommandManager commandManager = server.getCommandManager();
    	ServerCommandManager serverCommandManager = (ServerCommandManager)commandManager;
    	serverCommandManager.registerCommand(new CommanduPerm());
	}
}
