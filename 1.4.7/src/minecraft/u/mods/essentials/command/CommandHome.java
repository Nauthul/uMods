package u.mods.essentials.command;

import java.util.Collection;
import java.util.Iterator;

import u.mods.essentials.UEssentials;
import u.mods.essentials.permissions.Permissions;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

public class CommandHome extends CommandBase
{

	@Override
	public String getCommandName()
	{
		return "home";
	}
	
	@Override
	public String getCommandUsage(ICommandSender par1ICommandSender)
	{
		return "/" + getCommandName() + " help";
	}
	
	@Override
	public void processCommand(ICommandSender var1, String[] var2)
	{
		if (var2.length == 0)
			teleportToHome(var1, "home");
		else if (var2.length == 1)
		{
			if (var2[0].equalsIgnoreCase("help"))
				showHelp(var1);
			else if (var2[0].equalsIgnoreCase("list"))
				listHome(var1);
			else if (var2[0].equalsIgnoreCase("limit"))
				var1.sendChatToPlayer("Maximum Homes: " + UEssentials.instance.maxHouses);
			else if (var2[0].equalsIgnoreCase("set"))
				setHome(var1, "home");
			else if (var2[0].equalsIgnoreCase("delete"))
				delHome(var1, "home");
			else
				teleportToHome(var1, var2[0]);
		}
		else if (var2.length == 2)
		{
			if (var2[0].equalsIgnoreCase("set"))
				setHome(var1, var2[1]);
			if (var2[0].equalsIgnoreCase("delete"))
				delHome(var1, var2[1]);
		}
		else
			var1.sendChatToPlayer("\u00a7cUsage: " + this.getCommandUsage(var1));
	}
	
	@Override
	public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
	{
		return true;
	}
	
	private	void	teleportToHome(ICommandSender sender, String home)
	{
		String	uname = sender.getCommandSenderName();
		NBTTagCompound	playerTag = UEssentials.instance.dataEssentials.getCompoundTag(uname);
		NBTTagCompound	homes = playerTag.getCompoundTag("[Homes]");
		if (homes.hasKey(home))
		{
			EntityPlayerMP	player = getCommandSenderAsPlayer(sender);
			NBTTagCompound	h = homes.getCompoundTag(home);
			int		dimension = h.getInteger("Dimension");
			if (player.dimension != dimension && !UEssentials.instance.homeTroughDimension && !Permissions.Instance.hasPermission(uname, "uhome.bypass"))
			{
				sender.sendChatToPlayer("\u00a7cThis home is in another dimension.");
				return;
			}
			double	x = h.getDouble("PosX");
			double	y = h.getDouble("PosY");
			double	z = h.getDouble("PosZ");
			float	yaw = h.getFloat("Yaw");
			float	pitch = h.getFloat("Pitch");
			UEssentials.instance.teleportPlayerTroughDimension(player, dimension, x, y, z, yaw, pitch);
		}
		else
			sender.sendChatToPlayer("\u00a7cYou don't have a home called " + home);
	}

	private void	listHome(ICommandSender sender)
	{
		String	uname = sender.getCommandSenderName();
		EntityPlayerMP	player = getCommandSenderAsPlayer(sender);
		NBTTagCompound	playerTag = UEssentials.instance.dataEssentials.getCompoundTag(uname);
		NBTTagCompound	homes = playerTag.getCompoundTag("[Homes]");
		
		Collection c = homes.getTags();
		if (c.isEmpty())
		{
			sender.sendChatToPlayer("\u00a7cYou don't have any home yet.");
			return;
		}
		String res = "Homes: ";
		for (Iterator i = c.iterator(); i.hasNext(); )
		{
			NBTTagCompound h = (NBTTagCompound)i.next();
			res += h.getName() + " ";
		}
		sender.sendChatToPlayer(res);
	}
	
	private void	setHome(ICommandSender sender, String home)
	{
		if (Permissions.Instance.hasPermission(sender.getCommandSenderName(), "uhome.own.set"))
		{
			String	uname = sender.getCommandSenderName();
			EntityPlayerMP	player = getCommandSenderAsPlayer(sender);
			NBTTagCompound	playerTag = UEssentials.instance.dataEssentials.getCompoundTag(uname);
			NBTTagCompound	homes = playerTag.getCompoundTag("[Homes]");
			int count = homes.getTags().size();
			if (count < UEssentials.instance.maxHouses || Permissions.Instance.hasPermission(uname, "uhome.bypass"))
			{
				NBTTagCompound	h = homes.getCompoundTag(home);
				h.setInteger("Dimension", player.dimension);
				h.setDouble("PosX", player.posX);
				h.setDouble("PosY", player.posY);
				h.setDouble("PosZ", player.posZ);
				h.setFloat("Yaw", player.rotationYaw);
				h.setFloat("Pitch", player.rotationPitch);
				homes.setCompoundTag(home, h);
				playerTag.setCompoundTag("[Homes]", homes);
				UEssentials.instance.dataEssentials.setCompoundTag(uname, playerTag);
				UEssentials.instance.saveData();
				sender.sendChatToPlayer("\u00a79Created home: " + home + ".");
			}
			else
				sender.sendChatToPlayer("\u00a7cYou already have the maximum number of homes.");
		}
		else
			sender.sendChatToPlayer("\u00a7cYou do not have permission to use this command.");
	}
	
	private void	delHome(ICommandSender sender, String home)
	{
		if (Permissions.Instance.hasPermission(sender.getCommandSenderName(), "uhome.own.delete"))
		{
			String	uname = sender.getCommandSenderName();
			NBTTagCompound	playerTag = UEssentials.instance.dataEssentials.getCompoundTag(uname);
			NBTTagCompound	homes = playerTag.getCompoundTag("[Homes]");
			homes.removeTag(home);
			playerTag.setCompoundTag("[Homes]", homes);
			UEssentials.instance.dataEssentials.setCompoundTag(uname, playerTag);
			UEssentials.instance.saveData();
			sender.sendChatToPlayer("\u00a79Deleteded home: " + home + ".");
		}
		else
			sender.sendChatToPlayer("\u00a7cYou do not have permission to use this command.");
	}
	
	private void	showHelp(ICommandSender sender)
	{
		sender.sendChatToPlayer("\u00a72--- uPermissions Help Page ---");
		sender.sendChatToPlayer("/home [<home>] - Teleport to home <home> or to the default one if none indicated.");
		sender.sendChatToPlayer("/home set [<home>] - Create a home named <home> or set te default one where you stand.");
		sender.sendChatToPlayer("/home delete [<home>] = Delete yout home named <home> or the default one if none indicated.");
		sender.sendChatToPlayer("/home list - List all your homes.");
		sender.sendChatToPlayer("/home limit - Display the current number of homes allowed.");
		sender.sendChatToPlayer("/home help - Display this help.");
	}
}
