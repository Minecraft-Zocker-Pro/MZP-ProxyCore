package minecraft.proxycore.zocker.pro.storage.cache.redis.packet.server;

import minecraft.proxycore.zocker.pro.storage.cache.redis.RedisPacketAbstract;
import minecraft.proxycore.zocker.pro.storage.cache.redis.RedisPacketIdentifyType;
import org.json.JSONException;
import org.json.JSONObject;

public class RedisServerSoundPacket extends RedisPacketAbstract {

	private final String compatibleSound;

	public RedisServerSoundPacket(String compatibleSound) {
		this.compatibleSound = compatibleSound;
	}

	@Override
	public String getIdentify() {
		return RedisPacketIdentifyType.SERVER_SOUND.name().toUpperCase();
	}

	@Override
	public JSONObject toJSON() {
		try {
			return new JSONObject()
				.put("identify", getIdentify())
				.put("sound", this.compatibleSound.toUpperCase());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}
}
