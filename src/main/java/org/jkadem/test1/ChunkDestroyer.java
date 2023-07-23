package org.jkadem.test1;

import org.bukkit.plugin.java.JavaPlugin;
import org.jkadem.test1.commands.CDCommands;
import org.jkadem.test1.gamemode.CDManager;

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
