package u.mods.essentials.command;

import u.mods.essentials.UEssentials;
import u.mods.essentials.permissions.Permissions;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

public class CommandBack extends CommandBase
{

	@Override
	public String getCommandName()
	{
		return "back";
	}
	
	@Override
	public String getCommandUsage(ICommandSender par1ICommandSender)
	{
		return "/" + getCommandName();
	}
	
	@Override
	public void processCommand(ICommandSender var1, String[] var2)
	{
		EntityPlayerMP	player = getCommandSenderAsPlayer(var1);
		NBTTagCompound	playerTag = UEssentials.instance.dataEssentials.getCompoundTag(player.username);
		if (!playerTag.hasKey("[Back]"))
		{
			var1.sendChatToPlayer("\u00a7cYou've got nowhere to return to.");
			return;
		}
		NBTTagCompound	back = playerTag.getCompoundTag("[Back]");
		int		dimension = back.getInteger("Dimension");
		if (player.dimension != dimension && !UEssentials.instance.backTroughDimension)
		{
			var1.sendChatToPlayer("\u00a7cYour /back location is in another dimension.");
			return;
		}
		double	x = back.getDouble("PosX");
		double	y = back.getDouble("PosY");
		double	z = back.getDouble("PosZ");
		float	yaw = back.getFloat("Yaw");
		float	pitch = back.getFloat("Pitch");
		UEssentials.instance.teleportPlayerTroughDimension(player, dimension, x, y, z, yaw, pitch);
	}
	
	@Override
	public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
	{
		return Permissions.Instance.hasPermission(par1ICommandSender.getCommandSenderName(), "uessentials.back");
	}
	
}
