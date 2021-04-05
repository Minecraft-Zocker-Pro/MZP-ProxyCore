package minecraft.proxycore.zocker.pro.command;

import minecraft.proxycore.zocker.pro.network.NetworkServerManager;
import net.md_5.bungee.Util;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;

public class GlobalListCommand extends Command {

	public GlobalListCommand() {
		super("glist", "mzp.core.command.glist", new String[]{""});
	}

	@Override
	public List<String> onTabCompletion(CommandSender user, String[] args) {
		return null;
	}

	@Override
	public void onExecute(CommandSender sender, String[] args) {
		for (ServerInfo server : ProxyServer.getInstance().getServers().values()) {
			if (!server.canAccess(sender))
				continue;
			List<String> players = new ArrayList<>();
			for (ProxiedPlayer player : server.getPlayers())
				players.add(player.getDisplayName());
			players.sort(String.CASE_INSENSITIVE_ORDER);
			sender.sendMessage(new TextComponent(ProxyServer.getInstance().getTranslation("command_list", server.getName(), server.getPlayers().size(), Util.format(players, ChatColor.RESET + ", "))));
		}

		sender.sendMessage(new TextComponent(ProxyServer.getInstance().getTranslation("total_players", new NetworkServerManager().getGlobalOnline().join())));
	}
}
	
	
