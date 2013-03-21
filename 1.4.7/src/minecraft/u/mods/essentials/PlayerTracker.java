package u.mods.essentials;

import java.io.File;
import java.io.FileInputStream;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.ModLoader;
import net.minecraft.util.ChunkCoordinates;
import cpw.mods.fml.common.IPlayerTracker;

public class PlayerTracker implements IPlayerTracker
{

	@Override
	public void onPlayerLogin(EntityPlayer player)
	{
		if (player.worldObj.isRemote)
			return;
		String	fname = ModLoader.getMinecraftServerInstance().getFolderName();
		fname += "/players/" + player.username + ".dat";
		if (!new File(fname).exists() && UEssentials.instance.dataEssentials.hasKey("[Spawn]"))
		{
			NBTTagCompound	spawn = UEssentials.instance.dataEssentials.getCompoundTag("[Spawn]");
			int				dimension = spawn.getInteger("Dimension");
			double			x = spawn.getDouble("PosX");
			double			y = spawn.getDouble("PosY");
			double			z = spawn.getDouble("PosZ");
			float			yaw = spawn.getFloat("Yaw");
			float			pitch = spawn.getFloat("Pitch");
			UEssentials.instance.teleportPlayerTroughDimension((EntityPlayerMP)player, dimension, x, y, z, yaw, pitch);
		}
	}

	@Override
	public void onPlayerLogout(EntityPlayer player)
	{
		NBTTagCompound	playerTag = UEssentials.instance.dataEssentials.getCompoundTag(player.username);		
		long	time = System.currentTimeMillis();
		playerTag.setLong("[Seen]", time);
		UEssentials.instance.dataEssentials.setCompoundTag(player.username, playerTag);
	}

	@Override
	public void onPlayerChangedDimension(EntityPlayer player)
	{}

	@Override
	public void onPlayerRespawn(EntityPlayer player)
	{
		if (UEssentials.instance.dataEssentials.hasKey("[Spawn]"))
		{
			NBTTagCompound	spawn = UEssentials.instance.dataEssentials.getCompoundTag("[Spawn]");
			int				dimension = spawn.getInteger("Dimension");
			double			x = spawn.getDouble("PosX");
			double			y = spawn.getDouble("PosY");
			double			z = spawn.getDouble("PosZ");
			float			yaw = spawn.getFloat("Yaw");
			float			pitch = spawn.getFloat("Pitch");
			UEssentials.instance.teleportPlayerTroughDimension((EntityPlayerMP)player, dimension, x, y, z, yaw, pitch);
		}
	}

}
