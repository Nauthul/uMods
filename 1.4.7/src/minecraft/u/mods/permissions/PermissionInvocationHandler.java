package u.mods.permissions;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class PermissionInvocationHandler implements InvocationHandler
{
	public Object invoke(Object arg0, Method arg1, Object[] arg2) throws Throwable
	{
		if (arg1 == null)
			return null;
		if (arg1.getName().equals("hasPermission"))
			return Boolean.valueOf(hasPermission(arg2));
		if (arg1.getName().equals("getGroups"))
			return getGroups();
		if (arg1.getName().equals("getUserGroup"))
			return getUserGroup(arg2);
		if (arg1.getName().equals("getGroupPrefix"))
			return getGroupPrefix(arg2);
		return Boolean.valueOf(true);
	}
	
	private boolean	hasPermission(Object[] arg2)
	{
		if (arg2.length != 2 || !(arg2[0] instanceof String) || !(arg2[1] instanceof String))
			return true;
		return UPermissions.getController().hasPermission(arg2[0].toString().toLowerCase(), arg2[1].toString().toLowerCase());
	}
	
	private Object	getGroups()
	{ return UPermissions.getController().getGroups(); }
	
	private Object	getUserGroup(Object[] arg2)
	{
		if (arg2.length != 1 || !(arg2[0] instanceof String))
			return "";
		return UPermissions.getController().getUserGroup(arg2[0].toString().toLowerCase());
	}
	
	private Object	getGroupPrefix(Object[] arg2)
	{
		if (arg2.length != 1 || !(arg2[0] instanceof String))
			return "";
		return UPermissions.getController().getGroupPrefix(arg2[0].toString());
	}
}
