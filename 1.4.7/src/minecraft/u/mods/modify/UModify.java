package u.mods.modify;

import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.ServerStarting;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(modid="uModify", name="uModify", version="1.0-Beta")
public class UModify
{
	@ServerStarting
	public void	serverStarting(FMLServerStartingEvent event)
	{
		System.out.println("{WTFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF} 1");
		MinecraftForge.EVENT_BUS.register(new EntityEventHandler());
		MinecraftForge.EVENT_BUS.register(new PlayerInteractEventHandler());
		System.out.println("{WTFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF} 2");
	}
}
