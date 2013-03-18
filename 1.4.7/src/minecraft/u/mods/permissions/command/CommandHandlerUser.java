package u.mods.permissions.command;

import java.util.ArrayList;
import java.util.List;

import u.mods.permissions.UPermissions;

public class CommandHandlerUser
{
	private	List<String>	result = new ArrayList();
	
	public CommandHandlerUser(String user, List<String> params)
	{
		if (params.size() == 0)
		{
			if (!UPermissions.getController().hasPermission(user, "uperm.manage.users"))
				this.result.add("\u00a7cYou do not have permission to use this command.");
			else
				this.result = UPermissions.getController().listUsers();
		}
		else
		{
			String	paramuser	= params.remove(0);
			if (params.size() == 0 || (params.size() == 1 && params.get(0).equalsIgnoreCase("list")))
				handleList(user, paramuser);
			else if (params.size() == 1 && params.get(0).equalsIgnoreCase("delete"))
				handleDelete(user, paramuser);
			else if (params.size() == 2 && params.get(0).equalsIgnoreCase("add"))
				handleAdd(user, paramuser, params.get(1));
			else if (params.size() == 2 && params.get(0).equalsIgnoreCase("remove"))
				handleRemove(user, paramuser, params.get(1));
			else if (params.size() == 1 && params.get(0).equalsIgnoreCase("group"))
				handleGetGroup(user, paramuser);
			else if (params.size() == 2 && params.get(0).equalsIgnoreCase("group"))
				handleSetGroup(user, paramuser, params.get(1));
			else if (params.size() == 2 && params.get(0).equalsIgnoreCase("check"))
				handleSetGroup(user, paramuser, params.get(1));
			else
				this.result.add("\u00a7cUnknown command format. Type /upm for usage.");;
		}
	}

	public List<String>	getResult()
	{
		return this.result;
	}
	
	private void	handleList(String user, String paramuser)
	{
		//if (UPermissions.getController().hasPermission(user, "uperm.manage.users.permissions." + paramuser))
		//	this.result = UPermissions.getController().listUserPerms(paramuser);
		//else
		//	this.result.add("\u00a7cYou do not have permission to use this command.");
	}
	
	private void	handleDelete(String user, String paramuser)
	{
		if (UPermissions.getController().hasPermission(user, "uperm.manage.users." + paramuser))
			this.result.add(UPermissions.getController().delUser(paramuser));
		else
			this.result.add("\u00a7cYou do not have permission to use this command.");
	}
	
	private void	handleAdd(String user, String paramuser, String permission)
	{
		if (UPermissions.getController().hasPermission(user, "uperm.manage.users.permissions." + paramuser))
			this.result.add(UPermissions.getController().addUserPerm(paramuser, permission));
		else
			this.result.add("\u00a7cYou do not have permission to use this command.");
	}
	
	private void	handleRemove(String user, String paramuser, String permission)
	{
		if (UPermissions.getController().hasPermission(user, "uperm.manage.users.permissions." + paramuser))
			this.result.add(UPermissions.getController().delUserPerm(paramuser, permission));
		else
			this.result.add("\u00a7cYou do not have permission to use this command.");
	}
	
	private void	handleGetGroup(String user, String paramuser)
	{
		if (UPermissions.getController().hasPermission(user, "uperm.manage.users"))
			this.result.add(paramuser + " is a member of " + UPermissions.getController().getUserGroup(paramuser));
		else
			this.result.add("\u00a7cYou do not have permission to use this command.");
	}
	
	private void	handleSetGroup(String user, String paramuser, String group)
	{
		if (UPermissions.getController().hasPermission(user, "uperm.manage.membership." + group))
			this.result.add(UPermissions.getController().setUserGroup(paramuser, group));
		else
			this.result.add("\u00a7cYou do not have permission to use this command.");
	}
	
	private void	handleCheck(String user, String paramuser, String group)
	{
		if (UPermissions.getController().hasPermission(user, "uperm.manage." + paramuser))
			this.result.add(UPermissions.getController().setUserGroup(paramuser, group));
		else
			this.result.add("\u00a7cYou do not have permission to use this command.");
	}
}
