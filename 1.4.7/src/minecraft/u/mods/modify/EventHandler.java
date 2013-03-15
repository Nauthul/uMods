package u.mods.modify;

import u.mods.modify.permissions.Permissions;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;

public class EventHandler {

	@ForgeSubscribe
	public void	onTargetEvent(LivingSetAttackTargetEvent event)
	{
		if (!event.entity.worldObj.isRemote & event.target instanceof EntityPlayer)
		{
			EntityPlayer	player = (EntityPlayer)event.target;
			String			entityType = EntityList.getEntityString(event.entity);
			String			perm = "umodify.damage.mobtarget." + entityType;
			if (!Permissions.Instance.hasPermission(player.username, perm.toLowerCase()))
				event.entityLiving.setAttackTarget(null);
		}
	}
	
	@ForgeSubscribe
	public void	onAttackEvent(LivingAttackEvent event)
	{
		if (!event.entityLiving.worldObj.isRemote && event.source.getEntity() instanceof EntityPlayer)
		{
			EntityPlayer	player = (EntityPlayer)event.source.getEntity();
			String			entityType = EntityList.getEntityString(event.entityLiving);
			String			perm = "umodify.damage.deal." + entityType;
			if (!Permissions.Instance.hasPermission(player.username, perm.toLowerCase()))
				event.setCanceled(true);
		}
	}
}
