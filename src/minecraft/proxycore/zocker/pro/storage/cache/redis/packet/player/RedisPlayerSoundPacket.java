package minecraft.proxycore.zocker.pro.storage.cache.redis.packet.player;

import minecraft.proxycore.zocker.pro.storage.cache.redis.RedisPacketAbstract;
import minecraft.proxycore.zocker.pro.storage.cache.redis.RedisPacketIdentifyType;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

public class RedisPlayerSoundPacket extends RedisPacketAbstract {

	private final UUID uuid;
	private final String compatibleSound;

	public RedisPlayerSoundPacket(UUID uuid, String compatibleSound) {
		this.uuid = uuid;
		this.compatibleSound = compatibleSound;
	}

	@Override
	public String getIdentify() {
		return RedisPacketIdentifyType.PLAYER_SOUND.name().toUpperCase();
	}

	@Override
	public JSONObject toJSON() {
		try {
			return new JSONObject()
				.put("identify", getIdentify())
				.put("uuid", this.uuid.toString())
				.put("sound", this.compatibleSound.toUpperCase());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}
}
