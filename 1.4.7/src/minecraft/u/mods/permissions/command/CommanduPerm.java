package u.mods.permissions.command;

import java.util.ArrayList;
import java.util.Arrays;
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
			String			uname = var1.getCommandSenderName();
			List<String>	params = Arrays.asList(var2);
			String			command = params.remove(0);
			
			if (command.equalsIgnoreCase("users"))
			{
				CommandHandlerUsers handler = new CommandHandlerUsers(uname, params);
				sendResult(var1, handler.getResult());
				
			}
			else if (command.equalsIgnoreCase("user"))
			{
				CommandHandlerUser	handler = new CommandHandlerUser(uname, params);
				sendResult(var1, handler.getResult());
			}
			else if (command.equalsIgnoreCase("groups"))
			{
				CommandHandlerGroups	handler = new CommandHandlerGroups(uname, params);
				sendResult(var1, handler.getResult());
			}
			else if (command.equalsIgnoreCase("group"))
			{}
			//TODO
			else if (var2[0].equalsIgnoreCase("reload"))
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
			else
				displayHelp(var1);
		}
		
		if (var2.length > 0)
		{


		}
		

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
		var1.sendChatToPlayer("TODO");
	}
	
	public void	sendResult(ICommandSender var1, List<String> result)
	{
		for (String line : result)
			var1.sendChatToPlayer(line);
	}
}
