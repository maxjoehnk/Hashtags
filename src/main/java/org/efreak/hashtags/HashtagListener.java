package org.efreak.hashtags;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class HashtagListener implements Listener {

	private static Database db;
	
	static {
		db = Hashtags.getDb();
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onHashtag(AsyncPlayerChatEvent event) {
		String msg = event.getMessage();
		while (msg.contains("#")) {
			msg = msg.substring(msg.indexOf("#"));
			String hashtag;
			if (msg.indexOf(" ") == -1) hashtag = msg.substring(1);
			else hashtag = msg.substring(msg.indexOf("#") + 1, msg.indexOf(" ", msg.indexOf("#")));
			event.setMessage(event.getMessage().replace(hashtag, ChatColor.ITALIC + hashtag + ChatColor.RESET));
			if (msg.indexOf(" ") != -1) msg = msg.substring(msg.indexOf(" "));
			else msg = "";
			db.update("INSERT INTO `messages` VALUES ('" + hashtag + "', '" + event.getMessage() + "', '" + event.getPlayer().getName() + "')");
		}
	}
	
}
