package u.mods.essentials.command;

import u.mods.essentials.UEssentials;
import u.mods.essentials.permissions.Permissions;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

public class CommandSetspawn extends CommandBase
{
	@Override
	public String getCommandName()
	{
		return "setspawn";
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
		NBTTagCompound	spawn = new NBTTagCompound();
		spawn.setInteger("Dimension", player.dimension);
		spawn.setDouble("PosX", player.posX);
		spawn.setDouble("PosY", player.posY);
		spawn.setDouble("PosZ", player.posZ);
		spawn.setFloat("Yaw", player.rotationYaw);
		spawn.setFloat("Pitch", player.rotationPitch);
		UEssentials.instance.dataEssentials.setCompoundTag("[Spawn]", spawn);
		player.worldObj.provider.setSpawnPoint((int)player.posX, (int)player.posY, (int)player.posZ);
		UEssentials.instance.saveData();
		player.sendChatToPlayer("\u00a7aChanged spawn to current location.");
	}
	
	@Override
	public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
	{
		return Permissions.Instance.hasPermission(par1ICommandSender.getCommandSenderName(), "uessentials.setspawn");
	}
}
