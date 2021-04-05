package minecraft.proxycore.zocker.pro.listener;

import minecraft.proxycore.zocker.pro.network.NetworkServerManager;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ProxyPingListener implements Listener {

	private static final NetworkServerManager networkServerManager = new NetworkServerManager();

	@EventHandler()
	public void onProxyPing(ProxyPingEvent event) {
		event.getResponse().getPlayers().setOnline(networkServerManager.getGlobalOnline().join());
	}
}
