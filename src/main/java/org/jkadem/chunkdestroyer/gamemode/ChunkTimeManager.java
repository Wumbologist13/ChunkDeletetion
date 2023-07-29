package org.jkadem.chunkdestroyer.gamemode;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import java.util.HashMap;
import java.util.Map;

public class ChunkTimeManager {

  private final Map<Chunk, ChunkData> chunkMap = new HashMap<>();
  private int deleteTime = 180; //Default to 3 minutes

  public ChunkTimeManager() {

  }

  // Increment the time on all the chunk data's and remove any that is over the limit
  public void increment() {
    for (ChunkData chunkData : chunkMap.values()) {

      chunkData.incrementTime();
      if (chunkData.getCurrentTime() > deleteTime) {

        // Remove the chunk
        int chunkX = chunkData.getChunk().getX() * 16;
        int chunkZ = chunkData.getChunk().getZ() * 16;
        System.out.println("DELETING CHUNK");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), String.format("fill %d -64 %d %d -1 %d minecraft:air", chunkX, chunkZ, chunkX + 15, chunkZ + 15));
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), String.format("fill %d 0 %d %d 127 %d minecraft:air", chunkX, chunkZ, chunkX + 15, chunkZ + 15));
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), String.format("fill %d 128 %d %d 250 %d minecraft:air", chunkX, chunkZ, chunkX + 15, chunkZ + 15));
        chunkMap.remove(chunkData.getChunk());
        System.out.println("SIZE" + chunkMap.size());
      }
    }
  }

  public void addChunk(Chunk chunk) {

    if (!chunkMap.containsKey(chunk)) {

      System.out.println("INPUT NEW CHUNK INTO MAP");
      chunkMap.put(chunk, new ChunkData(chunk));
    }
  }

  public void setDeleteTime(int seconds) {
    deleteTime = seconds;
    increment();
  }

}

