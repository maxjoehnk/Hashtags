package org.efreak.hashtags.langs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.efreak.hashtags.Hashtags;

public class en extends Language {
	
	@Override
	public void createLanguageFile() {
		langFile = new File(Hashtags.getInstance().getDataFolder(), "lang/en.lang");
		if (!langFile.exists()) {
			try {
				langFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void updateLanguage() {
		lang = new YamlConfiguration();
		try {
			lang.load(langFile);
			
			set("Plugin.Done", "&aDone!");
			set("Plugin.Debug", "Hashtags runs in Debug Mode");

			set("Permissions.ForceSuper", "&2Superperms forced!");
			set("Permissions.Found", "&a%perms% support enabled!");
			set("Permissions.NoPerms", "&eNo Permissions System Found!");
			set("Permissions.OP", "&aUsing OP-Rights for Commands!");
			save();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
}
