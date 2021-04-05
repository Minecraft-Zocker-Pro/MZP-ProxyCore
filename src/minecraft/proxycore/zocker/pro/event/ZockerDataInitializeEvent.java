package minecraft.proxycore.zocker.pro.event;


import minecraft.proxycore.zocker.pro.Zocker;
import net.md_5.bungee.api.plugin.Event;

public class ZockerDataInitializeEvent extends Event {

	private final Zocker zocker;

	public ZockerDataInitializeEvent(Zocker zocker) {
		this.zocker = zocker;
	}

	public Zocker getZocker() {
		return zocker;
	}
}
