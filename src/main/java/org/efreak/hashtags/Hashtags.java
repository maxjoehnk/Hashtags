package org.efreak.hashtags;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;

import org.efreak.hashtags.databases.*;

public class Hashtags extends JavaPlugin {

	private static JavaPlugin instance;
	private static Configuration config;
	private static Database db;
	private static IOManager io;
	
	@Override
	public void onLoad() {
		instance = this;
	}
	
	@Override
	public void onEnable() {
		new File(getDataFolder(), "lang").mkdirs();
		config = new Configuration();
		io = new IOManager();
		config.init();
		io.init();
		if (config.getDebug()) io.sendConsoleWarning(io.translate("Plugin.Debug"));
		Database.registerDatabaseSystem("MySQL", new MySQL());
		Database.registerDatabaseSystem("SQLite", new SQLite());
		db = Database.getDatabaseBySystem(config.getString("Database.System"));
		if (db == null) {
			io.sendConsoleWarning("Unknown Database System. Falling back to SQLite");
			db = Database.getDatabaseBySystem("SQLite");
		}
		db.init();
		getServer().getPluginManager().registerEvents(new HashtagListener(), this);
		getCommand("trending").setExecutor(new TrendingCommand());
		io.sendConsole(getDescription().getFullName() + " successfully loaded");
	}
	
	@Override
	public void onDisable() {
		db.shutdown();
	}
	
	public static JavaPlugin getInstance() {
		return instance;
	}
	
	public static Configuration getConfiguration() {
		return config;
	}
	
	public static Database getDb() {
		return db;
	}
	
	public static IOManager getIOManager() {
		return io;
	}
	
}
