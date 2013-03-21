package u.mods.chat;

import u.mods.core.utils.ChatColor;
import u.mods.chat.permissions.Permissions;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.ServerChatEvent;

public class ChatEventHandler
{
	@ForgeSubscribe
	public void	onChatEvent(ServerChatEvent event)
	{
		String	msg = UChatManager.instance.messageFormat;
		String	usrgrp = Permissions.Instance.getUserGroup(event.username);
		String	prefix = ChatColor.colorizeAll(Permissions.Instance.getGroupPrefix(usrgrp));
		msg = msg.replace("%prefix", prefix);
		msg = msg.replace("%username", event.username);
		msg = msg.replace("%message", event.message);
		event.line = msg;
	}
}
