package org.jkadem.chunkdestroyer.gamemode;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jkadem.chunkdestroyer.ChunkDestroyer;

import java.util.*;

public class CDManager {

  private final ChunkDestroyer cd;
  private BukkitTask cdTask;
  private BukkitTask swapTask;
  private BukkitTask jumpTask;
  private final ChunkTimeManager ctm;
  private final Random random = new Random();

  private final Map<Chunk, ChunkData> chunkDataMap = new HashMap<>();

  public CDManager(ChunkDestroyer cd) {
    this.cd = cd;
    ctm = new ChunkTimeManager();
  }

  public void startScheduleSwap() {
    scheduleSwap();
  }

  public void startRandomJump() {
    int delay = 20 * 60 * (random.nextInt(3) + 1);
    System.out.println("JUMPING!");
    jumpTask = new BukkitRunnable() {
      @Override
      public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
          player.setVelocity(player.getVelocity().setY(10.0));
        }
      }
    }.runTaskLater(cd, delay);
  }

  public void stopRandomJump() {
    if (jumpTask != null) {
      jumpTask.cancel();
      jumpTask = null;
    }
  }

  public void stopScheduleSwap() {
    if (swapTask != null) {
      swapTask.cancel();
      swapTask = null;
    }
  }

  private void scheduleSwap() {
    int delay = 20 * 60 * (random.nextInt(8) + 3);
    swapTask = new BukkitRunnable() {

      @Override
      public void run() {
        scheduleSwap();

        swapInventories();
        scheduleSwap();

      }
    }.runTaskLater(cd, delay);
  }

  public void swapInventories() {
    Player[] players = Bukkit.getOnlinePlayers().toArray(new Player[0]);

    // No need to swap if only one player or no players
    if(players.length < 2) {
      return;
    }
    System.out.println("Swapping Inventories");
    // Shuffle players array to get random swapping
    List<Player> list = Arrays.asList(players);
    Collections.shuffle(list);
    players = list.toArray(new Player[0]);

    // Get first player's inventory
    ItemStack[] firstInventory = players[0].getInventory().getContents();

    // Iterate over all players, swapping inventories
    for (int i = 0; i < players.length - 1; i++) {
      ItemStack[] nextInventory = players[i+1].getInventory().getContents();
      players[i].getInventory().setContents(nextInventory);
    }
    // Set first player's inventory to last player
    players[players.length - 1].getInventory().setContents(firstInventory);
  }

  public void startChunkChecker() {

    cdTask = new BukkitRunnable() {
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
    if (cdTask != null) {
      cdTask.cancel();
      cdTask = null;
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
    return cdTask != null;
  }
}
