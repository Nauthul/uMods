package u.mods.modify;

import u.mods.modify.permissions.Permissions;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class PlayerInteractEventHandler
{
	@ForgeSubscribe
	public void	onPlayerInteract(PlayerInteractEvent event)
	{
		if (event.entityPlayer.worldObj.isRemote)
			return;
		EntityPlayer player = event.entityPlayer;
		if (event.action == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK)
		{
			int blockId = player.worldObj.getBlockId(event.x, event.y, event.z);
			if (!Permissions.Instance.hasPermission(player.username, "umodify.blocks.destroy." + String.valueOf(blockId)))
				event.setCanceled(true);
		}
		else if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)
		{
			int blockId = player.worldObj.getBlockId(event.x, event.y, event.z);
			if (!Permissions.Instance.hasPermission(player.username, "umodify.blocks.interact." + String.valueOf(blockId)))
			{
				event.useBlock = Event.Result.DENY;
				event.setCanceled(true);
				return;
			}
			if (player.getHeldItem() != null)
			{
				int itemID = player.getHeldItem().itemID;
				if (	!Permissions.Instance.hasPermission(player.username, "umodify.items.use." + String.valueOf(itemID)) ||
						!Permissions.Instance.hasPermission(player.username, "umodify.blocks.place." + String.valueOf(itemID)))
				{
					event.useItem = Event.Result.DENY;
					event.setCanceled(true);
				}
			}
		}
		else if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR && player.getHeldItem() != null)
		{
			int itemID = player.getHeldItem().itemID;
			if (!Permissions.Instance.hasPermission(player.username, "umodify.items.use." + String.valueOf(itemID)))
			{
				event.useItem = Event.Result.DENY;
				event.setCanceled(true);
			}
		}
	}
}
