package ulthu.ulock.utils;

import net.minecraftforge.common.ForgeDirection;

public enum BlockFace {
	BOTTOM	(0),
	TOP		(1),
	NORTH	(2),
	SOUTH	(3),
	WEST	(4),
	EAST	(5);
	
	private final int value;
	public static final BlockFace[] FACES = {BOTTOM, TOP, NORTH, SOUTH, WEST, EAST};
	public static final BlockFace[] OPPOSITES = {TOP, BOTTOM, SOUTH, NORTH, EAST, WEST};
	
	BlockFace(int val)
	{
		this.value = val;
	}
	
	public int getValue()
	{
		return this.value;
	}
	
	public BlockFace getOpposite()
    {
        return OPPOSITES[ordinal()];
    }
	
	public static BlockFace fromForgeFace(int face)
	{
		return FACES[face];
	}
	
	public static BlockFace fromDoor(int md)
	{
		BlockFace f = BlockFace.NORTH;
		if (md == 0)
			f = BlockFace.WEST;
		else if (md == 1)
			f = BlockFace.NORTH;
		else if (md == 2)
			f = BlockFace.EAST;
		else if (md == 3)
			f = BlockFace.SOUTH;
		return f;
	}
	
	public static BlockFace fromTrapDoor(int md)
	{
		BlockFace f = BlockFace.NORTH;
		if (md == 0)
			f = BlockFace.NORTH;
		else if (md == 1)
			f = BlockFace.SOUTH;
		else if (md == 2)
			f = BlockFace.WEST;
		else if (md == 3)
			f = BlockFace.EAST;
		return f;
	}
	
	public static BlockFace fromFenceGate(int md)
	{
		BlockFace f = BlockFace.NORTH;
		if (md == 0)
			f = BlockFace.SOUTH;
		else if (md == 1)
			f = BlockFace.WEST;
		else if (md == 2)
			f = BlockFace.NORTH;
		else if (md == 3)
			f = BlockFace.EAST;
		return f;
	}
}
