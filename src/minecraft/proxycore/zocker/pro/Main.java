package minecraft.proxycore.zocker.pro;

import minecraft.proxycore.zocker.pro.command.GlobalListCommand;
import minecraft.proxycore.zocker.pro.config.Config;
import minecraft.proxycore.zocker.pro.listener.PostLoginListener;
import minecraft.proxycore.zocker.pro.listener.ProxyPingListener;
import minecraft.proxycore.zocker.pro.network.NetworkServerManager;
import minecraft.proxycore.zocker.pro.storage.StorageManager;
import minecraft.proxycore.zocker.pro.storage.cache.memory.MemoryCacheManager;
import minecraft.proxycore.zocker.pro.storage.cache.redis.RedisCacheManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Main extends Plugin {

	public static Configuration PROXY_CONFIG;
	public static Config CORE_CONFIG;
	public static Config CORE_MESSAGE;
	public static Config CORE_STORAGE;

	private static Plugin PLUGIN;
	private static final String PLUGIN_NAME = "MZP-ProxyCore";

	@Override
	public void onEnable() {

		PLUGIN = this;

		try {
			PROXY_CONFIG = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File("config.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.buildConfig();

		StorageManager.initialize();

		this.registerCommand();
		this.registerListener();
	}

	@Override
	public void onDisable() {
		if (StorageManager.isMySQL()) {
			assert StorageManager.getMySQLDatabase() != null;
			StorageManager.getMySQLDatabase().disconnect();
		}

		if (StorageManager.isSQLite()) {
			assert StorageManager.getSQLiteDatabase() != null;
			StorageManager.getSQLiteDatabase().disconnect();
		}

		if (StorageManager.isRedis()) {
			RedisCacheManager.closeConnections();
			NetworkServerManager.stop();
		}

		if (StorageManager.isMemory()) {
			MemoryCacheManager.stop();
		}
	}


	public void registerCommand() {
		if (StorageManager.isRedis()) {
			getProxy().getPluginManager().registerCommand(this, new GlobalListCommand());
		}
	}

	public void registerListener() {
		PluginManager pluginManager = ProxyServer.getInstance().getPluginManager();

		pluginManager.registerListener(this, new PostLoginListener());

		if (StorageManager.isRedis()) {
			pluginManager.registerListener(this, new ProxyPingListener());
		}
	}

	public void buildConfig() {
		// Config		
		CORE_CONFIG = new Config("core.yml", getPluginName());

		CORE_CONFIG.set("core.server.name", "my-server", "0.0.2");

		CORE_CONFIG.setVersion("0.0.1", true);

		// Storage
		CORE_STORAGE = new Config("storage.yml", getPluginName());

		// MySQL
		CORE_STORAGE.set("storage.database.mysql.enabled", false, "0.0.1");
		CORE_STORAGE.set("storage.database.mysql.host", "localhost", "0.0.1");
		CORE_STORAGE.set("storage.database.mysql.port", 3306, "0.0.1");
		CORE_STORAGE.set("storage.database.mysql.database", "mzp_core", "0.0.1");
		CORE_STORAGE.set("storage.database.mysql.username", "mzp_core", "0.0.1");
		CORE_STORAGE.set("storage.database.mysql.password", "!default", "0.0.1");

		// SQLite
		CORE_STORAGE.set("storage.database.sql.enabled", true, "0.0.1");

		// Memory
		CORE_STORAGE.set("storage.cache.memory.enabled", true, "0.0.11");
		CORE_STORAGE.set("storage.cache.memory.delay", 1, "0.0.11");
		CORE_STORAGE.set("storage.cache.memory.limit", 100000, "0.0.17");
		CORE_STORAGE.set("storage.cache.memory.expiration.duration", 60, "0.0.11");
		CORE_STORAGE.set("storage.cache.memory.expiration.renew", true, "0.0.11");

		// Redis
		CORE_STORAGE.set("storage.cache.redis.enabled", false, "0.0.1");
		CORE_STORAGE.set("storage.cache.redis.host", "localhost", "0.0.1");
		CORE_STORAGE.set("storage.cache.redis.port", 6379, "0.0.1");
		CORE_STORAGE.set("storage.cache.redis.password", "!default", "0.0.1");

		CORE_STORAGE.setVersion("0.0.17", true);

		// Message
		CORE_MESSAGE = new Config("message.yml", "MZP-ProxyCore");

		CORE_MESSAGE.set("message.prefix", "&6&l[MZP] ", "0.0.1");
		CORE_MESSAGE.set("message.permission.deny", "&3You dont have permission!", "0.0.1");
		CORE_MESSAGE.set("message.command.permission.deny", "&3You dont have permission to execute this command!", "0.0.1");

		CORE_MESSAGE.set("message.command.sub.wrong", "&3Wrong sub command for this command!", "0.0.2");
		CORE_MESSAGE.set("message.command.arg.length", "&3Too many or to less arguments for this command!", "0.0.3");

		CORE_MESSAGE.setVersion("0.0.9", true);
	}

	public static Plugin getPlugin() {
		return PLUGIN;
	}

	public static String getPluginName() {
		return PLUGIN_NAME;
	}
}
