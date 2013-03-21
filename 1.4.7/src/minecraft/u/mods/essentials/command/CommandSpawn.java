package u.mods.essentials.command;

import u.mods.essentials.UEssentials;
import u.mods.essentials.permissions.Permissions;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

public class CommandSpawn extends CommandBase
{
	@Override
	public String getCommandName()
	{
		return "spawn";
	}
	
	@Override
	public String getCommandUsage(ICommandSender par1ICommandSender)
	{
		return "/" + getCommandName();
	}
	
	@Override
	public void processCommand(ICommandSender var1, String[] var2)
	{
		if (!UEssentials.instance.dataEssentials.hasKey("[Spawn]"))
		{
			var1.sendChatToPlayer("\u00a7cSpawn point has not been set.");
			return;
		}
		EntityPlayerMP	player = getCommandSenderAsPlayer(var1);
		NBTTagCompound	spawn = UEssentials.instance.dataEssentials.getCompoundTag("[Spawn]");
		int				dimension = spawn.getInteger("Dimension");
		double			x = spawn.getDouble("PosX");
		double			y = spawn.getDouble("PosY");
		double			z = spawn.getDouble("PosZ");
		float			yaw = spawn.getFloat("Yaw");
		float			pitch = spawn.getFloat("Pitch");
		UEssentials.instance.setPlayerBack(player);
		UEssentials.instance.teleportPlayerTroughDimension(player, dimension, x, y, z, yaw, pitch);
		player.sendChatToPlayer("\u00a7aYou've been teleported to the spawn.");
	}
	
	@Override
	public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
	{
		return Permissions.Instance.hasPermission(par1ICommandSender.getCommandSenderName(), "uessentials.spawn");
	}
}
