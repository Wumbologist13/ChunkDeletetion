package org.jkadem.chunkdestroyer;

import org.bukkit.plugin.java.JavaPlugin;
import org.jkadem.chunkdestroyer.commands.CDCommands;
import org.jkadem.chunkdestroyer.gamemode.CDManager;

import java.util.Objects;

public final class ChunkDestroyer extends JavaPlugin {

  private CDManager cdManager;

  @Override
  public void onEnable() {
    System.out.println("Hi!");

    Objects.requireNonNull(getCommand("CDStartChunk")).setExecutor(new CDCommands(this));
    Objects.requireNonNull(getCommand("CDStopChunk")).setExecutor(new CDCommands(this));
    Objects.requireNonNull(getCommand("CDSetTimerDuration")).setExecutor(new CDCommands(this));

    cdManager = new CDManager(this);

    cdManager.startChunkChecker();

  }

  @Override
  public void onDisable() {
    //Cleanup
    System.out.println("Bye!");
  }

  public CDManager cdManager() {
    return cdManager;
  }

}
