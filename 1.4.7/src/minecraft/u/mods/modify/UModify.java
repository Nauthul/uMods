package u.mods.modify;

import net.minecraft.entity.EntityList;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid="uModify", name="uLock", version="1.0")
@NetworkMod(clientSideRequired=false, serverSideRequired=true)
public class UModify
{
	@PostInit
	public void	postInit(FMLPostInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(new EntityEventHandler());
		MinecraftForge.EVENT_BUS.register(new PlayerInteractEventHandler());
	}
}
