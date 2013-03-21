package u.mods.essentials.command;

import u.mods.essentials.UEssentials;
import u.mods.essentials.permissions.Permissions;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.ModLoader;

public class CommandSeen extends CommandBase
{
	@Override
	public String getCommandName()
	{
		return "seen";
	}
	
	@Override
	public String getCommandUsage(ICommandSender par1ICommandSender)
	{
		return "/" + getCommandName() + "<username>";
	}
	
	@Override
	public void processCommand(ICommandSender var1, String[] var2)
	{
		if (var2.length != 1)
		{
			var1.sendChatToPlayer("\u00a7cUsage: " + this.getCommandUsage(var1));
			return;
		}
		
		MinecraftServer	server = ModLoader.getMinecraftServerInstance();
		for (String user : server.getAllUsernames())
		{
			if (user.equalsIgnoreCase(var2[0]))
			{
				var1.sendChatToPlayer("Player " + user + " is currently online.");
				return;
			}
		}
		
		if (UEssentials.instance.dataEssentials.hasKey(var2[0]))
		{
			NBTTagCompound	player = UEssentials.instance.dataEssentials.getCompoundTag(var2[0]);
			if (player.hasKey("[Seen]"))
			{
				long	seen = player.getLong("[Seen]");
				long	diffsecs = (System.currentTimeMillis() - seen) / 1000L;
				int		days = (int)(diffsecs / 86400L);
				diffsecs -= (long)days * 86400L;
				int		hours = (int)(diffsecs / 3600L);
				diffsecs -= (long)hours * 3600L;
				int		mins = (int)(diffsecs / 60L);
				diffsecs -= (long)mins * 60L;
				String	msg = "User " + var2[0] + " was last seen " + days + " days, " + hours + " hours, " + mins + " minutes and " + (int)diffsecs + " seconds ago.";
				var1.sendChatToPlayer(msg);
				return;
			}
		}
		var1.sendChatToPlayer("Player " + var2[0] + " was never seen on this server.");
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
	{
		return Permissions.Instance.hasPermission(par1ICommandSender.getCommandSenderName(), "uessentials.seen");
	}
}
