package u.mods.essentials;

import u.mods.essentials.permissions.Permissions;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public class DeathEventHandler
{
	@ForgeSubscribe
	public void	onPlayerDeath(LivingDeathEvent event)
	{
		if (event.entity instanceof EntityPlayerMP)
		{
			EntityPlayerMP	player = (EntityPlayerMP)event.entity;
			if (Permissions.Instance.hasPermission(player.username, "uessentials.back.ondeath"))
			{
				UEssentials.instance.setPlayerBack(player);
			}
		}
	}
}
