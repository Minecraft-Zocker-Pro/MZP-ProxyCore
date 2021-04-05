package minecraft.proxycore.zocker.pro.command;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import minecraft.proxycore.zocker.pro.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.List;

public abstract class Command extends net.md_5.bungee.api.plugin.Command implements TabExecutor {

	private String permission;

	public Command(String name) {
		this(name, Lists.newArrayList(), null);
	}

	public Command(String name, String... aliases) {
		this(name, Lists.newArrayList(aliases), null);
	}

	public Command(String name, List<String> aliases) {
		this(name, aliases, null);
	}

	public Command(String name, List<String> aliases, String permission) {
		super(name, null, aliases.toArray(new String[]{}));
		this.permission = permission;

		ProxyServer.getInstance().getPluginManager().registerCommand(Main.getPlugin(), this);
	}

	public Command(String name, String permission, String[] aliases) {
		super(name, permission, aliases);
		this.permission = permission;

		ProxyServer.getInstance().getPluginManager().registerCommand(Main.getPlugin(), this);
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (permission != null) {
			if (!sender.hasPermission(permission)) {
				sender.sendMessage(TextComponent.fromLegacyText(Main.CORE_MESSAGE.getString("message.prefix") + Main.CORE_MESSAGE.getString("message.command.permission.deny")));
				return;
			}
		}

		try {
			ProxyServer.getInstance().getScheduler().runAsync(Main.getPlugin(), () -> {
				onExecute(sender, args);
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
		if (permission != null) {
			if (!sender.hasPermission(permission)) {
				return null;
			}
		}

		if (sender instanceof ProxiedPlayer) {
			List<String> tabCompletion = onTabCompletion(sender, args);

			if (tabCompletion == null) {
				if (args.length == 0) {
					List<String> list = Lists.newArrayList();
					for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
						list.add(p.getName());
					}
					return list;
				} else {
					String lastWord = args[args.length - 1];
					List<String> list = Lists.newArrayList();

					for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
						if (p.getName().toLowerCase().startsWith(lastWord.toLowerCase())) {
							list.add(p.getName());
						}
					}

					return list;
				}
			}

			return tabCompletion;
		} else {
			return ImmutableList.of();
		}
	}

	public abstract List<String> onTabCompletion(CommandSender sender, String[] args);

	public abstract void onExecute(CommandSender commandSender, String[] args);

	public void unload() {
		ProxyServer.getInstance().getPluginManager().unregisterCommand(this);
	}
}