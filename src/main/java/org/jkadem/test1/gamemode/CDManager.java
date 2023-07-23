package org.jkadem.test1.gamemode;

import org.jkadem.test1.gamemode.ChunkData;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jkadem.test1.ChunkDestroyer;

import java.util.HashMap;
import java.util.Map;

public class CDManager {

  private ChunkDestroyer cd;
  private BukkitTask bt;

  private final Map<Chunk, ChunkData> chunkDataMap = new HashMap<>();

  // Default to 10 minutes for now
  private int timerDuration = 600;

  public CDManager(ChunkDestroyer cd) {this.cd = cd;}

  public boolean startChunkChecker() {
    bt = new BukkitRunnable() {
      @Override
      public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
          Chunk chunk = player.getLocation().getChunk();
          ChunkData chunkData = chunkDataMap.get(chunk);

          if (chunkData == null) {
            chunkData = new ChunkData(chunk);
            chunkDataMap.put(chunk, chunkData);
          }

          chunkData.addTime(player.getUniqueId(), 1);

          if (chunkData.getTotalTime() >= timerDuration) { // 600 seconds = 10 minutes
            new BukkitRunnable() {
              int countdown = 10; // 10 second countdown

              @Override
              public void run() {
                if (countdown > 0) {
                  Bukkit.broadcastMessage(String.format("Chunk at (%d, %d) will be cleared in %d seconds!", chunk.getX(), chunk.getZ(), countdown));
                  countdown--;
                } else {
                  int chunkX = chunk.getX() * 16;
                  int chunkZ = chunk.getZ() * 16;

                  Bukkit.dispatchCommand(Bukkit.getConsoleSender(), String.format("fill %d -64 %d %d -1 %d minecraft:air", chunkX, chunkZ, chunkX + 15, chunkZ + 15));
                  Bukkit.dispatchCommand(Bukkit.getConsoleSender(), String.format("fill %d 0 %d %d 127 %d minecraft:air", chunkX, chunkZ, chunkX + 15, chunkZ + 15));
                  Bukkit.dispatchCommand(Bukkit.getConsoleSender(), String.format("fill %d 128 %d %d 250 %d minecraft:air", chunkX, chunkZ, chunkX + 15, chunkZ + 15));

                  chunkDataMap.remove(chunk);
                  this.cancel(); // Stop the countdown
                }
              }
            }.runTaskTimer(cd, 0, 20); // Run every second
          }
        }
      }
    }.runTaskTimer(cd, 0, 20);

    return true;
  }

  public boolean stopChunkChecker() {
    if (bt != null) {
      bt.cancel();
      bt = null;
      return true;
    }
    return false;
  }

  /**
   * @param seconds
   * @return true if no error
   */
  public boolean setTimerDuration(int seconds) {
    timerDuration = seconds;
    stopChunkChecker();
    startChunkChecker();
    return true;
  }

  /**
   * @return true if running
   */
  public boolean isCheckerRunning() {
    return bt != null;
  }
}
