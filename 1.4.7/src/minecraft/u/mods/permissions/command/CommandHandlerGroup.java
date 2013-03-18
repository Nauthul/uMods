package u.mods.permissions.command;

import java.util.ArrayList;
import java.util.List;

import u.mods.permissions.UPermissions;

public class CommandHandlerGroup
{
	private	List<String>	result = new ArrayList();
	
	public	CommandHandlerGroup(String user, List<String> params)
	{
		if (params.size() == 0)
		{
			if (!UPermissions.getController().hasPermission(user, "uperm.manage.groups"))
				this.result.add("\u00a7cYou do not have permission to use this command.");
			else
				this.result = UPermissions.getController().listGroups();
		}
		else if (params.size() == 1 && params.get(0).equalsIgnoreCase(""))
		{}
		else
			this.result.add("\u00a7cUsage: /uperm users OR /uperm users list");
	
	}
	
	public List<String>	getResult()
	{
		return this.result;
	}
}
