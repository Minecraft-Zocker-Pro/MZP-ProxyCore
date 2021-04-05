package minecraft.proxycore.zocker.pro.listener;

import minecraft.proxycore.zocker.pro.Zocker;
import minecraft.proxycore.zocker.pro.event.ZockerDataInitializeEvent;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class PostLoginListener implements Listener {

	@EventHandler(priority = EventPriority.LOW)
	public void onPostLogin(PostLoginEvent event) {
		Zocker zocker = Zocker.getZocker(event.getPlayer().getUniqueId());
		if (zocker != null) return;

		ProxyServer.getInstance().getPluginManager().callEvent(new ZockerDataInitializeEvent(new Zocker(event.getPlayer())));

//		Zocker finalZocker = zocker;

//		finalZocker.hasValueAsync("player", "uuid", "uuid", finalZocker.getPlayer().getUniqueId().toString()).thenApplyAsync(aBoolean -> {
//			if (aBoolean) {
//				finalZocker.set("player",
//					new String[]{"name", "server", "online"},
//					new Object[]{finalZocker.getPlayer().getName(), event.getPlayer().getServer().getInfo().getName(), 1});
//				ProxyServer.getInstance().getPluginManager().callEvent(new ZockerDataInitializeEvent(finalZocker));
//				return true;
//			}
//
//			finalZocker.insert(
//				"player",
//				new String[]{"uuid", "name", "server", "online"},
//				new Object[]{finalZocker.getPlayer().getUniqueId().toString(), finalZocker.getPlayer().getName(), event.getPlayer().getServer().getInfo().getName(), 1});
//			ProxyServer.getInstance().getPluginManager().callEvent(new ZockerDataInitializeEvent(finalZocker));
//
//			return false;
//		});
	}
}
