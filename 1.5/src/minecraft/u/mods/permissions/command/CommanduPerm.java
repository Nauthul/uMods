package u.mods.permissions.command;

import java.util.List;

import u.mods.permissions.UPermissions;
import cpw.mods.fml.common.ModContainer;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class CommanduPerm extends CommandBase
{

	@Override
	public String getCommandName()
	{
		return "uperm";
	}
	
	@Override
	public int getRequiredPermissionLevel()
    {
        return 0;
    }
	
	@Override
	public void processCommand(ICommandSender var1, String[] var2)
	{
		if (var2.length > 0)
		{
			// Misc Commands
			if (var2[0].equalsIgnoreCase("reload"))
			{
				if (UPermissions.getController().hasPermission(var1.getCommandSenderName(), "uperm.manage.reload"))
				{
					var1.sendChatToPlayer("\u00a72Reloading Permissions.");
					UPermissions.instance.permController.reload();
					var1.sendChatToPlayer("\u00a72Permissions Reloaded.");
				}
				else
					var1.sendChatToPlayer("\u00a7cYou do not have permission to use this command.");
			}
			// Listing Commands
			else if (var2[0].equalsIgnoreCase("listusers"))
			{
				if (UPermissions.getController().hasPermission(var1.getCommandSenderName(), "uperm.manage.users.list"))
				{
					List<String>	users = UPermissions.getController().listUsers();
					for (String line : users)
						var1.sendChatToPlayer(line);
				}
				else
					var1.sendChatToPlayer("\u00a7cYou do not have permission to use this command.");
			}
			else if (var2[0].equalsIgnoreCase("listgroups"))
			{
				if (UPermissions.getController().hasPermission(var1.getCommandSenderName(), "uperm.manage.groups.list"))
				{
					List<String>	groups = UPermissions.getController().listGroups();
					for (String line : groups)
						var1.sendChatToPlayer(line);
				}
				else
					var1.sendChatToPlayer("\u00a7cYou do not have permission to use this command.");
			}
/*			
			//TODO
			// Specific Listing Commands
			else if (var2[0].equalsIgnoreCase("listuserperms"))
			{
				if (var2.length == 2)
				{
					if (UPermissions.getController().hasPermission(var1.getCommandSenderName(), "uperm.manage.users.permissions.list"))
					{}
					else
						var1.sendChatToPlayer("\u00a7cYou do not have permission to use this command.");
				}
				else
					var1.sendChatToPlayer("\u00a7cWrong arguments, usage: /" + getCommandName() + " listuserperms <user>");

			}
			else if (var2[0].equalsIgnoreCase("listgroupperms"))
			{}
			else if (var2[0].equalsIgnoreCase("listgroupsusers"))
			{}
			// User Management Commands
			else if (var2[0].equalsIgnoreCase("deluser"))
			{}
			else if (var2[0].equalsIgnoreCase("adduserperm"))
			{}
			else if (var2[0].equalsIgnoreCase("deluserperm"))
			{}
*/
			else if (var2[0].equalsIgnoreCase("checkuserperm"))
			{
				if (var2.length == 3)
				{
					if (UPermissions.getController().hasPermission(var1.getCommandSenderName(), "uperm.manage.users.permissions.check"))
						var1.sendChatToPlayer(UPermissions.getController().checkUserPerm(var2[1], var2[2]));
					else
						var1.sendChatToPlayer("\u00a7cYou do not have permission to use this command.");
				}
				else
					var1.sendChatToPlayer("\u00a7cWrong arguments, usage: /" + getCommandName() + " checkuserperm <user> <permission>");
			}
/*
			else if (var2[0].equalsIgnoreCase("getusergroup"))
			{}
*/
			else if (var2[0].equalsIgnoreCase("setusergroup"))
			{
				if (var2.length == 3)
				{
					if (UPermissions.getController().hasPermission(var1.getCommandSenderName(), "uperm.manage.users.group"))
						var1.sendChatToPlayer(UPermissions.getController().setUserGroup(var2[1], var2[2]));
					else
						var1.sendChatToPlayer("\u00a7cYou do not have permission to use this command.");
				}
				else
					var1.sendChatToPlayer("\u00a7cWrong arguments, usage: /" + getCommandName() + " setusergroup <user> <group>");
			}
/*
			// Group Management Commands
			else if (var2[0].equalsIgnoreCase("addgroup"))
			{}
			else if (var2[0].equalsIgnoreCase("delgroup"))
			{}
			else if (var2[0].equalsIgnoreCase("addgroupperm"))
			{}
			else if (var2[0].equalsIgnoreCase("delgroupperm"))
			{}
			else if (var2[0].equalsIgnoreCase("checkgroupperm"))
			{}
			else if (var2[0].equalsIgnoreCase("getgroupprefix"))
			{}
			else if (var2[0].equalsIgnoreCase("setgroupprefix"))
			{}
			else if (var2[0].equalsIgnoreCase("getdefaultgroup"))
			{}
			else if (var2[0].equalsIgnoreCase("setdefaultgroup"))
			{}
			else if (var2[0].equalsIgnoreCase("getgrouprank"))
			{}
			else if (var2[0].equalsIgnoreCase("setgrouprank"))
			{}
*/
			else if (var2[0].equalsIgnoreCase("promote"))
			{
				if (var2.length == 2)
				{
					if (UPermissions.getController().hasPermission(var1.getCommandSenderName(), "uperm.manage.users.promote"))
						var1.sendChatToPlayer(UPermissions.getController().promote(var2[1]));
					else
						var1.sendChatToPlayer("\u00a7cYou do not have permission to use this command.");
				}
				else
					var1.sendChatToPlayer("\u00a7cWrong arguments, usage: /" + getCommandName() + " promote <user>");
			}
			else if (var2[0].equalsIgnoreCase("demote"))
			{
				if (var2.length == 2)
				{
					if (UPermissions.getController().hasPermission(var1.getCommandSenderName(), "uperm.manage.users.demote"))
						var1.sendChatToPlayer(UPermissions.getController().demote(var2[1]));
					else
						var1.sendChatToPlayer("\u00a7cYou do not have permission to use this command.");
				}
				else
					var1.sendChatToPlayer("\u00a7cWrong arguments, usage: /" + getCommandName() + " demote <user>");
			}
			else if (var2[0].equalsIgnoreCase("ladder"))
			{
				if (UPermissions.getController().hasPermission(var1.getCommandSenderName(), "uperm.manage"))
				{
					List<String>	ladder = UPermissions.getController().getLadder();
					for (String line : ladder)
						var1.sendChatToPlayer(line);
				}
				else
					var1.sendChatToPlayer("\u00a7cYou do not have permission to use this command.");
			}
			else if (var2[0].equalsIgnoreCase("hierarchy"))
			{
				if (UPermissions.getController().hasPermission(var1.getCommandSenderName(), "uperm.manage"))
				{
					List<String>	hierarchy = UPermissions.getController().getHierarchy();
					for (String line : hierarchy)
						var1.sendChatToPlayer(line);
				}
				else
					var1.sendChatToPlayer("\u00a7cYou do not have permission to use this command.");
			}

		}
		else
			displayHelp(var1);

	}
	
	public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
	{
		if (UPermissions.getController().hasPermission(par1ICommandSender.getCommandSenderName(), "uperm.manage"))
			return true;
		return false;
	}
	
	public void	displayHelp(ICommandSender var1)
	{
		var1.sendChatToPlayer("\u00a72--- uPermissions Help Page ---");
		if (UPermissions.getController().hasPermission(var1.getCommandSenderName(), "uperm.manage.reload"))
			var1.sendChatToPlayer("/uperm reload");
		if (UPermissions.getController().hasPermission(var1.getCommandSenderName(), "uperm.manage.users.list"))
			var1.sendChatToPlayer("/uperm listusers");
		if (UPermissions.getController().hasPermission(var1.getCommandSenderName(), "uperm.manage.groups.list"))
			var1.sendChatToPlayer("/uperm listgroups");
		if (UPermissions.getController().hasPermission(var1.getCommandSenderName(), "uperm.manage.users.permissions.check"))
			var1.sendChatToPlayer("/uperm checkuserperm <user> <permission>");
		if (UPermissions.getController().hasPermission(var1.getCommandSenderName(), "uperm.manage.users.group"))
			var1.sendChatToPlayer("/uperm setusergroup <user> <group>");
		if (UPermissions.getController().hasPermission(var1.getCommandSenderName(), "uperm.manage.users.promote"))
			var1.sendChatToPlayer("/uperm promote <user>");
		if (UPermissions.getController().hasPermission(var1.getCommandSenderName(), "uperm.manage.users.demote"))
			var1.sendChatToPlayer("/uperm promote <user>");
		if (UPermissions.getController().hasPermission(var1.getCommandSenderName(), ""))
			var1.sendChatToPlayer("/uperm demote <user>");
		if (UPermissions.getController().hasPermission(var1.getCommandSenderName(), "uperm.manage"))
			var1.sendChatToPlayer("/uperm ladder");
		if (UPermissions.getController().hasPermission(var1.getCommandSenderName(), "uperm.manage"))
			var1.sendChatToPlayer("/uperm hierarchy");
/*
		var1.sendChatToPlayer("");
		var1.sendChatToPlayer("");
		var1.sendChatToPlayer("");
		var1.sendChatToPlayer("");
		var1.sendChatToPlayer("");
		var1.sendChatToPlayer("");
		var1.sendChatToPlayer("");
		var1.sendChatToPlayer("");
		var1.sendChatToPlayer("");
		var1.sendChatToPlayer("");
		var1.sendChatToPlayer("");
		var1.sendChatToPlayer("");
		var1.sendChatToPlayer("");
		var1.sendChatToPlayer("");
		var1.sendChatToPlayer("");
		var1.sendChatToPlayer("");
		var1.sendChatToPlayer("");
		var1.sendChatToPlayer("");
		*/
	}
}
