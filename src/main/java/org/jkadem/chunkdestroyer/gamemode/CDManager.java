package org.jkadem.chunkdestroyer.gamemode;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jkadem.chunkdestroyer.ChunkDestroyer;

import java.util.HashMap;
import java.util.Map;

public class CDManager {

  private ChunkDestroyer cd;
  private BukkitTask bt;
  private ChunkTimeManager ctm;

  private final Map<Chunk, ChunkData> chunkDataMap = new HashMap<>();

  public CDManager(ChunkDestroyer cd) {
    this.cd = cd;
    ctm = new ChunkTimeManager();
  }

  public void startChunkChecker() {
    bt = new BukkitRunnable() {
      @Override
      public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
          Chunk standingChunk = player.getLocation().getChunk();

          ctm.addChunk(standingChunk);
        }
        ctm.increment();
      }
    }.runTaskTimer(cd, 0, 20);

  }

  public void stopChunkChecker() {
    if (bt != null) {
      bt.cancel();
      bt = null;
    }
  }

  /**
   * @param seconds
   */
  public void setTimerDuration(int seconds) {
    // Default to 10 minutes for now
    ctm.setDeleteTime(seconds);
  }

  /**
   * @return true if running
   */
  public boolean isCheckerRunning() {
    return bt != null;
  }
}
