package u.mods.lock.protect;

import java.security.acl.Permission;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ForgeDummyContainer;

import u.mods.lock.ULock;
import u.mods.lock.permissions.Permissions;
import u.mods.lock.utils.BlockFace;

public class ProtectedItem
{
	public boolean			hasOwner = false;
	public boolean			allowEveryone = false;

	private String			owner = "";
	private Set<String>		users = new HashSet<String>();
	private static int[]	protectedIDs = {	Block.dispenser.blockID,
												Block.chest.blockID,
												Block.stoneOvenIdle.blockID,
												Block.stoneOvenActive.blockID,
												Block.doorWood.blockID,
												Block.trapdoor.blockID,
												Block.fenceGate.blockID,
												Block.brewingStand.blockID,
												Block.anvil.blockID
												};
	
	public ProtectedItem(World w, int x, int y, int z)
	{
		int id = w.getBlockId(x, y, z);

		if (	id == Block.dispenser.blockID ||
				id == Block.stoneOvenIdle.blockID ||
				id == Block.stoneOvenActive.blockID ||
				id == Block.brewingStand.blockID ||
				id == Block.anvil.blockID)
		{
			analyzeBlock(w, x, y, z, BlockFace.NORTH);
			analyzeBlock(w, x, y, z, BlockFace.EAST);
			analyzeBlock(w, x, y, z, BlockFace.SOUTH);
			analyzeBlock(w, x, y, z, BlockFace.WEST);
		}
		else if (id == Block.chest.blockID)
		{
			analyzeBlock(w, x, y, z, BlockFace.NORTH);
			analyzeBlock(w, x, y, z, BlockFace.EAST);
			analyzeBlock(w, x, y, z, BlockFace.SOUTH);
			analyzeBlock(w, x, y, z, BlockFace.WEST);
			
			TileEntityChest te = (TileEntityChest)w.getBlockTileEntity(x, y, z);
			boolean redo = false;
			if (te.adjacentChestXPos != null)
			{ x += 1; redo = true; }
			else if (te.adjacentChestXNeg != null)
			{ x -= 1; redo = true; }
			else if (te.adjacentChestZPosition != null)
			{ z += 1; redo = true; }
			else if (te.adjacentChestZNeg != null)
			{ z -= 1; redo = true; }
			
			if (redo)
			{
				analyzeBlock(w, x, y, z, BlockFace.NORTH);
				analyzeBlock(w, x, y, z, BlockFace.EAST);
				analyzeBlock(w, x, y, z, BlockFace.SOUTH);
				analyzeBlock(w, x, y, z, BlockFace.WEST);
			}
		}
		else if (id == Block.doorWood.blockID)
		{
			if ((w.getBlockMetadata(x, y, z) & 0x8) != 0)
				y -= 1;
			
			int md = w.getBlockMetadata(x, y, z) & 0x3;
			
			BlockFace face = BlockFace.fromDoor(md);
			
			analyzeDoor(w, x, y, z, face);
		}
		else if (id == Block.fenceGate.blockID)
		{
			int md = w.getBlockMetadata(x, y, z) & 0x3;
			
			BlockFace face = BlockFace.fromFenceGate(md);
			
			analyzeTrapDoor(w, x, y, z, face);
		}
		else if (id == Block.trapdoor.blockID)
		{
			int md = w.getBlockMetadata(x, y, z) & 0x3;
			BlockFace face = BlockFace.fromTrapDoor(md);

			ForgeDirection dir = ForgeDirection.getOrientation(face.getValue());
			analyzeBlock(w, x - dir.offsetX , y - dir.offsetY - 1, z - dir.offsetZ, face);
			analyzeBlock(w, x - dir.offsetX , y - dir.offsetY, z - dir.offsetZ, face.getOpposite());
			analyzeBlock(w, x - dir.offsetX , y - dir.offsetY + 1, z - dir.offsetZ, face);
		}
	}

	public static boolean isProtectedBlock(int id)
	{
		for (int b : protectedIDs)
			if (b == id)
				return true;
		return false;
	}
	
	public static boolean isProtectedBlockAt(World w, int x, int y, int z)
	{
		return isProtectedBlock(w.getBlockId(x, y, z));
	}

	// Take Block
	public static boolean isValidSignPlaceSpace(World w, int x, int y, int z, int face)
	{
		if (w.getBlockId(x, y - 2, z) == Block.doorWood.blockID)
		{
			int md = w.getBlockMetadata(x, y - 2, z) & 0x3;
			BlockFace bf = BlockFace.fromDoor(md);
			
			if (bf.getValue() == face || bf.getOpposite().getValue() == face)
				return true;
		}
		else if (w.getBlockId(x, y - 2, z) == Block.fenceGate.blockID)
		{
			int md = w.getBlockMetadata(x, y - 2, z) & 0x3;
			BlockFace bf = BlockFace.fromFenceGate(md);
			
			if (bf.getValue() == face || bf.getOpposite().getValue() == face)
				return true;
		}
		else
		{
			ForgeDirection dir = ForgeDirection.getOrientation(face);
			if (w.getBlockId(x + dir.offsetX, y + dir.offsetY - 1, z + dir.offsetZ) == Block.trapdoor.blockID ||
						w.getBlockId(x + dir.offsetX, y + dir.offsetY + 1, z + dir.offsetZ) == Block.trapdoor.blockID)
				 return true;
		}
		
		return false;
	}

	// Take Sign
	public static ProtectedItem getObjectFromSign(World w, int x, int y, int z, int face)
	{
		if (w.getBlockId(x, y - 1, z) == Block.trapdoor.blockID)
			return new ProtectedItem(w, x, y - 1, z);
		else if (w.getBlockId(x, y + 1, z) == Block.trapdoor.blockID)
			return new ProtectedItem(w, x, y + 1, z);
		else
		{
			ForgeDirection dir = ForgeDirection.getOrientation(face);
			if (isProtectedBlockAt(w, x - dir.offsetX, y - dir.offsetY, z - dir.offsetZ))
				return new ProtectedItem(w, x - dir.offsetX, y - dir.offsetY, z - dir.offsetZ);
			else if (w.getBlockId(x - dir.offsetX, y - dir.offsetY - 2, z - dir.offsetZ) == Block.doorWood.blockID)
			{
				int md = w.getBlockMetadata(x - dir.offsetX, y - dir.offsetY - 2, z - dir.offsetZ) & 0x3;
				
				BlockFace bf = BlockFace.fromDoor(md);
				
				if (bf.getValue() == face || bf.getOpposite().getValue() == face)
					return new ProtectedItem(w, x - dir.offsetX, y - dir.offsetY - 2, z - dir.offsetZ);
			}
			else if (w.getBlockId(x - dir.offsetX, y - dir.offsetY - 2, z - dir.offsetZ) == Block.fenceGate.blockID)
			{
				int md = w.getBlockMetadata(x - dir.offsetX, y - dir.offsetY - 2, z - dir.offsetZ) & 0x3;
				
				BlockFace bf = BlockFace.fromFenceGate(md);
				
				if (bf.getValue() == face || bf.getOpposite().getValue() == face)
					return new ProtectedItem(w, x - dir.offsetX, y - dir.offsetY - 2, z - dir.offsetZ);
			}
		}
		return null;
	}
	
	public boolean canUse(String user)
	{
		if (!this.hasOwner)
			return true;
		
		if (this.allowEveryone)
			return true;
		
		if (user.equalsIgnoreCase(owner))
			return true;
		
		for (String s : users)
			if (s.equalsIgnoreCase(user))
				return true;

		if (Permissions.Instance.hasPermission(user, "ulock.admin.bypass"))
			return true;
				
		return false;
	}

	public boolean isOwner(String user)
	{
		if (this.hasOwner && this.owner.equalsIgnoreCase(user))
			return true;
		return false;
	}
	
	public void printOutStats(EntityPlayer player)
	{
		player.sendChatToPlayer("hasOwner = " + String.valueOf(this.hasOwner));
		player.sendChatToPlayer("allowEveryone = " + String.valueOf(this.allowEveryone));
		String o = "Owner = ";
		String u = "Users = ";
		if (this.hasOwner)
			o += this.owner;
		int i = 0;
		for (String s : this.users)
		{
			if (i != 0)
				u += ", ";
			u += s;
			i += 1;
		}
		player.sendChatToPlayer(o);
		player.sendChatToPlayer(u);
	}
	
	private void analyzeDoor(World w, int x, int y, int z, BlockFace face)
	{
		ForgeDirection dir;
		if (face.getValue() == BlockFace.NORTH.getValue() || face.getValue() == BlockFace.SOUTH.getValue())
			dir = ForgeDirection.getOrientation(BlockFace.EAST.getValue());
		else
			dir = ForgeDirection.getOrientation(BlockFace.NORTH.getValue());
		
		analyzeBlock(w, x, y, z, face);
		analyzeBlock(w, x, y, z, face.getOpposite());
		analyzeBlock(w, x, y + 1, z, face);
		analyzeBlock(w, x, y + 1, z, face.getOpposite());
		analyzeBlock(w, x, y + 2, z, face);
		analyzeBlock(w, x, y + 2, z, face.getOpposite());
	}
	
	private void analyzeTrapDoor(World w, int x, int y, int z, BlockFace face)
	{
		ForgeDirection dir;
		if (face.getValue() == BlockFace.NORTH.getValue() || face.getValue() == BlockFace.SOUTH.getValue())
			dir = ForgeDirection.getOrientation(BlockFace.EAST.getValue());
		else
			dir = ForgeDirection.getOrientation(BlockFace.NORTH.getValue());
		
		analyzeBlock(w, x, y, z, face);
		analyzeBlock(w, x, y, z, face.getOpposite());
		analyzeBlock(w, x, y + 2, z, face);
		analyzeBlock(w, x, y + 2, z, face.getOpposite());
	}
	
	private void analyzeBlock(World w, int x, int y, int z, BlockFace face)
	{
		ForgeDirection	d = ForgeDirection.getOrientation(face.getValue());

		x += d.offsetX;
		y += d.offsetY;
		z += d.offsetZ;
		
		if (w.getBlockId(x, y, z) == Block.signWall.blockID)
		{
			int	md = w.getBlockMetadata(x, y, z);
			
			if (md == face.getValue())
				analyzeSign(w, x, y, z);
		}
	}
	
	private void analyzeSign(World w, int x, int y, int z)
	{
		TileEntitySign	te = (TileEntitySign)w.getBlockTileEntity(x, y, z);

		if (te.signText[0].equalsIgnoreCase(ULock.instance.signPrivate))
		{
			if (!this.hasOwner)
			{
				this.hasOwner = true;
				this.owner = te.signText[1];
			}
			addUser(te.signText[1]);
			addUser(te.signText[2]);
			addUser(te.signText[3]);
		}
		else if (te.signText[0].equalsIgnoreCase(ULock.instance.signMore))
		{
			addUser(te.signText[1]);
			addUser(te.signText[2]);
			addUser(te.signText[3]);
		}
	}
	
	private void addUser(String user)
	{
		if (user.equalsIgnoreCase(""))
			return;
		if (user.equalsIgnoreCase(ULock.instance.signEveryone))
		{
			this.allowEveryone = true;
			return;
		}
		this.users.add(user);
	}
}
