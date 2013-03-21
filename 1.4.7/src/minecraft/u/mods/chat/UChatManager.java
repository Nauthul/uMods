package u.mods.chat;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Mod.ServerStarting;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(modid="uChat", name="uChat", version="1.0-Beta", dependencies = "required-after:uCoreMod" )
public class UChatManager
{
	public String	messageFormat;

	@Instance("uChat")
	public static UChatManager instance;
	
	@PreInit
	public void	preInit(FMLPreInitializationEvent event)
	{
		Configuration	config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		this.messageFormat = config.get("Config", "messageFormat", "%prefix %username: %message").value;
		config.save();
	}
	
	@ServerStarting
	public void	serverStarting(FMLServerStartingEvent event)
	{
		MinecraftForge.EVENT_BUS.register(new ChatEventHandler());
	}
}
