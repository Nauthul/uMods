package u.mods.lock;

import u.mods.lock.permissions.Permissions;
import u.mods.lock.protect.ProtectedItem;
import u.mods.lock.utils.BlockFace;
import u.mods.lock.utils.Position;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.dispenser.PositionImpl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet53BlockChange;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class EventHandler {

	@ForgeSubscribe(priority=EventPriority.HIGHEST)
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		
		if (event.isCancelable() && event.isCanceled())
			return;

		if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)
		{
			if (event.entityPlayer.getHeldItem() != null && event.entityPlayer.getHeldItem().itemID == Item.sign.itemID)
			{
				if (ProtectedItem.isProtectedBlockAt(event.entityPlayer.worldObj, event.x, event.y, event.z))
				{
					// Player putting a sign on a protectable block
					if (event.entityPlayer.isSneaking())
					{
						ProtectedItem it = new ProtectedItem(event.entityPlayer.worldObj, event.x, event.y, event.z);
						
						if (it.hasOwner && !it.isOwner(event.entityPlayer.username))
						{
							event.setCanceled(true);
							event.entityPlayer.sendChatToPlayer(ULock.instance.actionPermission);
						}
					}
					// Player activating block
					else
						handleOpenning(event);
				}
				// Player putting a sign on a linked block
				else if (ProtectedItem.isValidSignPlaceSpace(event.entityPlayer.worldObj, event.x, event.y, event.z, event.face))
				{
					ForgeDirection dir = ForgeDirection.getOrientation(event.face);
					ProtectedItem it = ProtectedItem.getObjectFromSign(event.entityPlayer.worldObj, event.x + dir.offsetX, event.y + dir.offsetY, event.z + dir.offsetZ, event.face);
					
					if (it != null && it.hasOwner && !it.isOwner(event.entityPlayer.username))
					{
						event.setCanceled(true);
						event.entityPlayer.sendChatToPlayer(ULock.instance.actionPermission);
					}
				}
			}
			// Player activating block
			else if (ProtectedItem.isProtectedBlockAt(event.entityPlayer.worldObj, event.x, event.y, event.z))
				handleOpenning(event);
			// Player selecting sign
			else if (event.entityPlayer.worldObj.getBlockId(event.x, event.y, event.z) == Block.signWall.blockID)
			{
				TileEntitySign s = (TileEntitySign)event.entityPlayer.worldObj.getBlockTileEntity(event.x, event.y, event.z);
				if (s != null && (s.signText[0].equalsIgnoreCase(ULock.instance.signPrivate) || s.signText[0].equalsIgnoreCase(ULock.instance.signMore)))
				{
					int md = event.entityPlayer.worldObj.getBlockMetadata(event.x, event.y, event.z);
					ProtectedItem it = ProtectedItem.getObjectFromSign(event.entityPlayer.worldObj, event.x, event.y, event.z, md);
					if (it != null && it.isOwner(event.entityPlayer.username))
					{
						ULock.instance.selectedSigns.put(event.entityPlayer.username, new Position(event.x, event.y, event.z));
					}
				}
			}
		}
		else if (event.action == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK)
			handleDestroyBlock(event);
	}

	private void handleOpenning(PlayerInteractEvent event)
	{
		ProtectedItem it = new ProtectedItem(event.entityPlayer.worldObj, event.x, event.y, event.z);
		
		if (!it.canUse(event.entityPlayer.username))
			event.setCanceled(true);
		
		if (ULock.instance.debug)
		{
			event.entityPlayer.sendChatToPlayer("+----DEBUG----+");
			it.printOutStats(event.entityPlayer);
		}
	}
	
	private void handleDestroyBlock(PlayerInteractEvent event)
	{
		if (ProtectedItem.isProtectedBlockAt(event.entityPlayer.worldObj, event.x, event.y, event.z))
		{
			ProtectedItem it = new ProtectedItem(event.entityPlayer.worldObj, event.x, event.y, event.z);
			if (it.hasOwner && !it.isOwner(event.entityPlayer.username) && !Permissions.Instance.hasPermission(event.entityPlayer.username, "ulock.admin.destroy"))
			{
				event.setCanceled(true);
				event.entityPlayer.sendChatToPlayer(ULock.instance.actionPermission);
			}
		}
		else if (event.entityPlayer.worldObj.getBlockId(event.x, event.y, event.z) == Block.signWall.blockID)
		{
			TileEntitySign s = (TileEntitySign)event.entityPlayer.worldObj.getBlockTileEntity(event.x, event.y, event.z);
			if (s != null && (s.signText[0].equalsIgnoreCase(ULock.instance.signPrivate) || s.signText[0].equalsIgnoreCase(ULock.instance.signMore)))
			{
				int md = event.entityPlayer.worldObj.getBlockMetadata(event.x, event.y, event.z);
				ProtectedItem it = ProtectedItem.getObjectFromSign(event.entityPlayer.worldObj, event.x, event.y, event.z, md);
				if (it != null && it.hasOwner && !it.isOwner(event.entityPlayer.username) && !Permissions.Instance.hasPermission(event.entityPlayer.username, "ulock.admin.destroy"))
				{
					event.setCanceled(true);
					event.entityPlayer.sendChatToPlayer(ULock.instance.actionPermission);
					/*
					 * TEST
					if (event.entityPlayer instanceof EntityPlayerMP)
					{
						EntityPlayerMP epm = (EntityPlayerMP)event.entityPlayer;
						event.entityPlayer.sendChatToPlayer("Sending Packet");
						epm.playerNetServerHandler.sendPacketToPlayer(new Packet53BlockChange(event.x, event.y, event.z, epm.worldObj));
						event.entityPlayer.sendChatToPlayer("Packet Sent");
					}
					*/
				}
			}
		}
		else if (isBlockProtected(event.entityPlayer.worldObj, event.x, event.y, event.z, event.entityPlayer.username))
		{
			event.setCanceled(true);
			event.entityPlayer.sendChatToPlayer(ULock.instance.actionPermission);
		}
	}
	
	private boolean isBlockProtected(World w, int x, int y, int z, String player)
	{
		if (Permissions.Instance.hasPermission(player, "ulock.admin.destroy"))
			return false;
		
		if (ProtectedItem.isValidSignPlaceSpace(w, x, y, z, BlockFace.NORTH.getValue()))
		{
			ForgeDirection dir = ForgeDirection.NORTH;
			ProtectedItem it = ProtectedItem.getObjectFromSign(w, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ, BlockFace.NORTH.getValue());
			if (it != null && it.hasOwner && !it.isOwner(player))
				return true;
			return false;
		}
		else if (ProtectedItem.isValidSignPlaceSpace(w, x, y, z, BlockFace.SOUTH.getValue()))
		{
			ForgeDirection dir = ForgeDirection.SOUTH;
			ProtectedItem it = ProtectedItem.getObjectFromSign(w, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ, BlockFace.SOUTH.getValue());
			if (it != null && it.hasOwner && !it.isOwner(player))
				return true;
			return false;
		}
		else if (ProtectedItem.isValidSignPlaceSpace(w, x, y, z, BlockFace.EAST.getValue()))
		{
			ForgeDirection dir = ForgeDirection.EAST;
			ProtectedItem it = ProtectedItem.getObjectFromSign(w, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ, BlockFace.EAST.getValue());
			if (it != null && it.hasOwner && !it.isOwner(player))
				return true;
			return false;
		}
		else if (ProtectedItem.isValidSignPlaceSpace(w, x, y, z, BlockFace.WEST.getValue()))
		{
			ForgeDirection dir = ForgeDirection.WEST;
			ProtectedItem it = ProtectedItem.getObjectFromSign(w, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ, BlockFace.WEST.getValue());
			if (it != null && it.hasOwner && !it.isOwner(player))
				return true;
			return false;
		}
		else
		{
			if (w.getBlockId(x + ForgeDirection.NORTH.offsetX, y + ForgeDirection.NORTH.offsetY, z + ForgeDirection.NORTH.offsetZ) == Block.trapdoor.blockID)
			{
				int md = w.getBlockMetadata(x + ForgeDirection.NORTH.offsetX, y + ForgeDirection.NORTH.offsetY, z + ForgeDirection.NORTH.offsetZ);
				if (BlockFace.fromTrapDoor(md).getValue() == BlockFace.NORTH.getValue())
				{
					ProtectedItem it = new ProtectedItem(w, x + ForgeDirection.NORTH.offsetX, y + ForgeDirection.NORTH.offsetY, z + ForgeDirection.NORTH.offsetZ);
					if (it.hasOwner && !it.isOwner(player))
						return true;
				}
			}
			if (w.getBlockId(x + ForgeDirection.SOUTH.offsetX, y + ForgeDirection.SOUTH.offsetY, z + ForgeDirection.SOUTH.offsetZ) == Block.trapdoor.blockID)
			{
				int md = w.getBlockMetadata(x + ForgeDirection.SOUTH.offsetX, y + ForgeDirection.SOUTH.offsetY, z + ForgeDirection.SOUTH.offsetZ);
				if (BlockFace.fromTrapDoor(md).getValue() == BlockFace.SOUTH.getValue())
				{
					ProtectedItem it = new ProtectedItem(w, x + ForgeDirection.SOUTH.offsetX, y + ForgeDirection.SOUTH.offsetY, z + ForgeDirection.SOUTH.offsetZ);
					if (it.hasOwner && !it.isOwner(player))
						return true;
				}
			}
			if (w.getBlockId(x + ForgeDirection.EAST.offsetX, y + ForgeDirection.EAST.offsetY, z + ForgeDirection.EAST.offsetZ) == Block.trapdoor.blockID)
			{
				int md = w.getBlockMetadata(x + ForgeDirection.EAST.offsetX, y + ForgeDirection.EAST.offsetY, z + ForgeDirection.EAST.offsetZ);
				if (BlockFace.fromTrapDoor(md).getValue() == BlockFace.EAST.getValue())
				{
					ProtectedItem it = new ProtectedItem(w, x + ForgeDirection.EAST.offsetX, y + ForgeDirection.EAST.offsetY, z + ForgeDirection.EAST.offsetZ);
					if (it.hasOwner && !it.isOwner(player))
						return true;
				}
			}
			if (w.getBlockId(x + ForgeDirection.WEST.offsetX, y + ForgeDirection.WEST.offsetY, z + ForgeDirection.WEST.offsetZ) == Block.trapdoor.blockID)
			{
				int md = w.getBlockMetadata(x + ForgeDirection.WEST.offsetX, y + ForgeDirection.WEST.offsetY, z + ForgeDirection.WEST.offsetZ);
				if (BlockFace.fromTrapDoor(md).getValue() == BlockFace.NORTH.getValue())
				{
					ProtectedItem it = new ProtectedItem(w, x + ForgeDirection.WEST.offsetX, y + ForgeDirection.WEST.offsetY, z + ForgeDirection.WEST.offsetZ);
					if (it.hasOwner && !it.isOwner(player))
						return true;
				}
			}
		}
		return false;
	}
}
