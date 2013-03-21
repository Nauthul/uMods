package u.mods.modify;

import u.mods.modify.permissions.Permissions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;

public class EntityEventHandler
{

	/*
	 * umodify.damage.deal.<entity_name>
	 * umodify.damage.take.<entyty_name>
	 */
	@ForgeSubscribe
	public void	onPlayerIsAttacked(LivingAttackEvent event)
	{
		if (event.entityLiving.worldObj.isRemote)
			return;
		// PvP
		if (	event.entityLiving instanceof EntityPlayer &&
				event.source.getEntity() instanceof EntityPlayer)
		{
			EntityPlayer	player = (EntityPlayer)event.entityLiving;
			EntityPlayer	target = (EntityPlayer)event.source.getEntity();
			String			dealPerm = "umodify.damage.deal.player." + target.username.toLowerCase();
			String			takePerm = "umodify.damage.take.player." + player.username.toLowerCase();
			if (	!Permissions.Instance.hasPermission(player.username, dealPerm) ||
					!Permissions.Instance.hasPermission(target.username, takePerm))
				event.setCanceled(true);
		}
		// PvE
		else if (event.source.getEntity() instanceof EntityPlayer)
		{
			EntityPlayer	player = (EntityPlayer)event.source.getEntity();
			String			entityType = EntityList.getEntityString(event.entityLiving).toLowerCase();
			String			perm = "umodify.damage.deal." + entityType;
			if (!Permissions.Instance.hasPermission(player.username, perm))
				event.setCanceled(true);
		}
		// EvP
		else if (event.entityLiving instanceof EntityPlayer)
		{
			EntityPlayer	player = (EntityPlayer)event.entityLiving;
			Entity			src = event.source.getEntity();
			String			entityType = "";
			if (src != null)
				entityType = EntityList.getEntityString(src);
			if (entityType == null)
				return;
			String			perm = "umodify.damage.take." + entityType.toLowerCase();
			if (!Permissions.Instance.hasPermission(player.username, perm.toLowerCase()))
				event.setCanceled(true);
		}
	}

	/*
	 * umodify.mobtarget.<entity_name>
	 */
	@ForgeSubscribe
	void	onPlayerIsTargeted(LivingSetAttackTargetEvent event)
	{
		if (!event.entity.worldObj.isRemote & event.target instanceof EntityPlayer)
		{
			EntityPlayer	player = (EntityPlayer)event.target;
			String			entityType = EntityList.getEntityString(event.entity).toLowerCase();
			String			perm = "umodify.mobtarget." + entityType;
			if (!Permissions.Instance.hasPermission(player.username, perm))
				event.entityLiving.setAttackTarget(null);
		}
	}
	
	/*
	 * umodify.interact.<entity_name>
	 */
	@ForgeSubscribe
	void	onPlayerInteract(EntityInteractEvent event)
	{
		String	entityType = EntityList.getEntityString(event.target).toLowerCase();
		String	perm = "umodify.interact." + entityType;
		if (!Permissions.Instance.hasPermission(event.entityPlayer.username, perm))
			event.setCanceled(true);
	}
}
