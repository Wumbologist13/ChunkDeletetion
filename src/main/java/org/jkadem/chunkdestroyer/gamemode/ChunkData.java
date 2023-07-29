package org.jkadem.chunkdestroyer.gamemode;

import org.bukkit.Chunk;

import java.util.UUID;

public class ChunkData {

  private final Chunk chunk;
  private int currentTime; // Number of seconds sinch chunk was entered first

  public ChunkData(Chunk chunk) {
    this.chunk = chunk;
    currentTime = 0;
  }

  // Increment the currentTime, should be called every second.
  public void incrementTime() {
    currentTime += 1;
  }

  public void setTime(int seconds) {
    currentTime = seconds;
  }

  public int getCurrentTime() {
    return currentTime;
  }

  public Chunk getChunk() {
    return chunk;
  }
}
