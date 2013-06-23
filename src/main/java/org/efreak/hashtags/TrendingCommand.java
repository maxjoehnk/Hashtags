package org.efreak.hashtags;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class TrendingCommand implements CommandExecutor {

	private static Configuration config;
	private static Database db;
	private static IOManager io;
	
	static {
		config = Hashtags.getConfiguration();
		db = Hashtags.getDb();
		io = Hashtags.getIOManager();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = null;
		ConsoleCommandSender c = null;
		if (sender instanceof Player) p = (Player) sender;
		if (sender instanceof ConsoleCommandSender) c = (ConsoleCommandSender) sender;
		if (p == c) return false;
		if (args.length == 0) {
			try {
				HashMap<Integer, String> trendings = new HashMap<Integer, String>();
				ResultSet tags = db.query("SELECT DISTINCT `hashtag` FROM `messages`");
				if (tags == null) {
					io.send(sender, "Nothing was tagged yet");
					return true;
				}
				while (tags.next()) trendings.put(db.queryInt("SELECT COUNT(*) FROM `messages` WHERE `hashtag`='" + tags.getString("hashtag") + "'"), tags.getString("hashtag"));
				System.out.println(trendings.size());
				Integer[] trendingCount = trendings.keySet().toArray(new Integer[trendings.size()]);
				Arrays.sort(trendingCount);
				io.sendHeader(sender, "TRENDINGS");
				for (int i = 0; i < 10 && i < trendingCount.length; i++) io.send(sender, "#" + trendings.get(trendingCount[i]) + " (" + trendingCount[i] + ")");
			}catch (SQLException e) {
				if (config.getDebug()) e.printStackTrace();
				io.sendError(sender, "Error retrieving trendings");
				io.sendConsoleError("Error retrieving trendings: " + e.getMessage());			
			}
		}else if (args.length == 1) {
			if (args[0].contains("#")) {
				io.sendWarning(sender, "Please enter trendings without a #");
				return true;
			}
			try {
				ResultSet trendings = db.query("SELECT * FROM `messages` WHERE `hashtag`='" + args[0] + "' LIMIT 0, 10");
				if (trendings == null) io.send(sender, "No Messages found");
				else {
					io.sendHeader(sender, "TRENDINGS: #" + args[0]);
					io.send(sender, trendings.getString("player") + ": " + trendings.getString("msg"), false);
					while (trendings.next()) io.send(sender, trendings.getString("player") + ": " + trendings.getString("msg"), false);
				}
			}catch (SQLException e) {
				if (config.getDebug()) e.printStackTrace();
				io.sendError(sender, "Error retrieving trendings");
				io.sendConsoleError("Error retrieving trendings: " + e.getMessage());
			}
		}else return false;
		return true;
	}

}
