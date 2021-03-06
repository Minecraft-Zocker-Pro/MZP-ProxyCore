package minecraft.proxycore.zocker.pro.storage.cache.memory;

import minecraft.proxycore.zocker.pro.Main;
import minecraft.proxycore.zocker.pro.workers.JobRunnable;
import minecraft.proxycore.zocker.pro.workers.instances.WorkerPriority;
import minecraft.proxycore.zocker.pro.workers.instances.Workers;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

public class MemoryCacheManager {

	private static final CopyOnWriteArrayList<MemoryCacheEntry> memoryCacheEntryList = new CopyOnWriteArrayList<>();
	private static JobRunnable jobRunnable;

	public static void start() {
		jobRunnable = new JobRunnable() {
			@Override
			public void run() {
				long current = System.currentTimeMillis();

				try {
					memoryCacheEntryList.stream()
						.filter(Objects::nonNull)
						.forEach(memoryCacheEntry -> {
							if (memoryCacheEntry.getExpirationDuration() > 0) {
								if ((memoryCacheEntry.getExpirationDuration() - current) <= 0) {
									memoryCacheEntryList.remove(memoryCacheEntry);
									return;
								}
							}

							if (memoryCacheEntry.isExpiringOnQuit()) {
								if (memoryCacheEntry.getUniqueKey().length() > 35 && memoryCacheEntry.getUniqueKey().length() < 37) { // is player uuid
									String uuid = memoryCacheEntry.getUniqueKey().substring(0, 36);
									ProxiedPlayer player = ProxyServer.getInstance().getPlayer(UUID.fromString(uuid));
									if (player != null && player.isConnected()) return;

									memoryCacheEntryList.remove(memoryCacheEntry);
								} else {
									if ((memoryCacheEntry.getExpirationDuration() - current) <= 0) {
										memoryCacheEntryList.remove(memoryCacheEntry);
									}
								}
							}
						});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		Workers.BACKEND_WORKER.addJob(jobRunnable, 0, Main.CORE_STORAGE.getInt("storage.cache.memory.delay"), TimeUnit.SECONDS, WorkerPriority.EXTREME);
	}

	public static void stop() {
		if (jobRunnable == null) return;
		jobRunnable.cancel();
	}

	public static boolean isRunning() {
		if (jobRunnable == null) return false;
		return !jobRunnable.isCancelled();
	}

	public MemoryCacheEntry get(String uniqueKey) {
		if (memoryCacheEntryList.isEmpty()) return null;

		MemoryCacheEntry cacheEntry = memoryCacheEntryList.stream()
			.filter(Objects::nonNull)
			.filter(memoryCacheEntry -> memoryCacheEntry.getUniqueKey() != null)
			.filter(memoryCacheEntry -> memoryCacheEntry.getUniqueKey().equalsIgnoreCase(uniqueKey))
			.findAny()
			.orElse(null);

		if (cacheEntry != null) {
			if (Main.CORE_STORAGE.getBool("storage.cache.memory.expiration.renew")) {
				cacheEntry.setExpirationDuration(Main.CORE_STORAGE.getLong("storage.cache.memory.expiration.duration"), TimeUnit.SECONDS);
			}

			return cacheEntry;
		}

		return null;
	}

	public int getSize() {
		return memoryCacheEntryList.size();
	}

	public void add(MemoryCacheEntry memoryCacheEntry) {
		memoryCacheEntryList.add(memoryCacheEntry);
	}

	public void remove(MemoryCacheEntry memoryCacheEntry) {
		memoryCacheEntryList.remove(memoryCacheEntry);
	}

	public static CopyOnWriteArrayList<MemoryCacheEntry> getMemoryCacheEntryList() {
		return memoryCacheEntryList;
	}
}
