package org.jkadem.chunkdestroyer.gamemode;

import org.bukkit.Chunk;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChunkData {
  private final Chunk chunk;
  private final Map<UUID, Integer> playerTimes = new HashMap<>();

  public ChunkData(Chunk chunk) {this.chunk = chunk;}

  public void addTime(UUID playerUUID, int time) {
    playerTimes.compute(playerUUID, (uuid, oldTime) -> oldTime == null ? time : oldTime + time);
  }

  public int getTotalTime() { return playerTimes.values().stream().mapToInt(Integer::intValue).sum();}

}
