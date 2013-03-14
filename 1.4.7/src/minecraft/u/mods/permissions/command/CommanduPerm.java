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
				UPermissions.instance.permController.reload();
				var1.sendChatToPlayer("");
			}
			// Listing Commands
			else if (var2[0].equalsIgnoreCase("listusers"))
			{
				//List<String>	users = UPermissions.instance.permController.getFormatedUsers();
				//for (String line : users)
				//	var1.sendChatToPlayer(line);
			}
			else if (var2[0].equalsIgnoreCase("listgroups"))
			{
				//List<String>	groups = UPermissions.instance.permController.getFormatedGroups();
				//for (String line : groups)
				//	var1.sendChatToPlayer(line);
			}
			// Specific Listing Commands
			else if (var2[0].equalsIgnoreCase("listuserperms"))
			{}
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
			else if (var2[0].equalsIgnoreCase("checkuserperm"))
			{
				if (var2.length == 3)
				{
					var1.sendChatToPlayer(String.valueOf(UPermissions.getController().hasPermission(var2[1], var2[2])));
				}
				else
					var1.sendChatToPlayer("Wrong arguments");
			}
			else if (var2[0].equalsIgnoreCase("getusergroup"))
			{}
			else if (var2[0].equalsIgnoreCase("setusergroup"))
			{}
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
			{
				
			}
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
			else if (var2[0].equalsIgnoreCase("promote"))
			{}
			else if (var2[0].equalsIgnoreCase("demote"))
			{}
			else if (var2[0].equalsIgnoreCase("ladder"))
			{}
			else if (var2[0].equalsIgnoreCase("hierarchy"))
			{
				//List<String>	hierarchy = UPermissions.instance.permController.getFormatedHierarchy();
				//for (String line : hierarchy)
				//	var1.sendChatToPlayer(line);
			}
		}
		else
			displayHelp(var1);

	}
	
	public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
	{
		return true;
	}
	
	public void	displayHelp(ICommandSender var1)
	{}
}
