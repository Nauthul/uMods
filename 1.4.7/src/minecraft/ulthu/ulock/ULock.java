package ulthu.ulock;

import java.util.HashMap;

import ulthu.ulock.utils.Position;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.ModLoader;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Mod.ServerStarting;
import cpw.mods.fml.common.Mod.ServerStopping;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid="uLock", name="uLock", version="13.3.10")
@NetworkMod(clientSideRequired=false, serverSideRequired=true)
public class ULock {
	
	public static MinecraftServer		server;
	public String						signPrivate;
	public String						signMore;
	public String						signEveryone;
	public boolean						debug;
	public String						actionPermission;
	public String						actionSelection;
	public HashMap<String, Position>	selectedSigns;

	@Instance("uLock")
	public static ULock instance;
	
	@PreInit
    public void preInit(FMLPreInitializationEvent event) {
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		this.signPrivate = config.get("Keywords", "signPrivate", "[Private]").value;
		this.signMore = config.get("Keywords", "signMore", "[More]").value;
		this.signEveryone = config.get("Keywords", "signEveryone", "[Everyone]").value;
		this.debug = config.get("Config", "debug", false).getBoolean(false);
		this.actionPermission = config.get("Messages", "actionPermission", "You don't have permission to perform this action.").value;
		this.actionSelection = config.get("Messages", "actionSelection", "This sign has been selected for edition.").value;
		//this.var = config.get("Messages", "Var", true).getBoolean(true);
		config.save();
		this.selectedSigns = new HashMap<String, Position>();
    }
    
    @Init
    public void load(FMLInitializationEvent event) {
    	// Stub Method
    }
    
    @PostInit
    public void postInit(FMLPostInitializationEvent event) {
    	// Stub Method
    	MinecraftForge.EVENT_BUS.register(new EventHandler());
    }
    
    @ServerStarting
    public void serverStarting(FMLServerStartingEvent event) {
    	// Stub Method
    	server = ModLoader.getMinecraftServerInstance();
    	ICommandManager commandManager = server.getCommandManager();
    	ServerCommandManager serverCommandManager = (ServerCommandManager)commandManager;
    	addCommands(serverCommandManager);
    }
    
    @ServerStopping
    public void serverStopping(FMLServerStoppingEvent event) {
    	// Stub Method
    }
    
    public void addCommands(ServerCommandManager manager) {
    	//manager.registerCommand(new CommanduLock());
    }
    
}
