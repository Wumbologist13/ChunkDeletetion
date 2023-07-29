package org.jkadem.chunkdestroyer.gamemode;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChunkTimeManager {

  private final Map<Chunk, ChunkData> chunkMap = new HashMap<>();
  private int deleteTime = 180; //Default to 3 minutes

  public ChunkTimeManager() {

  }

  // Increment the time on all the chunk datas and remove any that is over the limit
  public void increment() {
    for (ChunkData chunkData : chunkMap.values()) {
      System.out.println("SIZE" + chunkMap.size());
      chunkData.incrementTime();
      if (chunkData.getCurrentTime() > deleteTime) {

        // Remove the chunk
        int chunkX = chunkData.getChunk().getX() * 16;
        int chunkZ = chunkData.getChunk().getZ() * 16;

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), String.format("fill %d -64 %d %d -1 %d minecraft:air", chunkX, chunkZ, chunkX + 15, chunkZ + 15));
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), String.format("fill %d 0 %d %d 127 %d minecraft:air", chunkX, chunkZ, chunkX + 15, chunkZ + 15));
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), String.format("fill %d 128 %d %d 250 %d minecraft:air", chunkX, chunkZ, chunkX + 15, chunkZ + 15));
        chunkMap.remove(chunkData.getChunk());
      }
    }
  }

  public boolean addChunk(Chunk chunk) {

    if (chunkMap.containsKey(chunk)) {
      return false;
    }
    else {
      System.out.println("INPUT NEW CHUNK INTO MAP");
      chunkMap.put(chunk, new ChunkData(chunk));
    }
    return true;
  }

  public void setDeleteTime(int seconds) {
    deleteTime = seconds;
    increment();
  }

}

