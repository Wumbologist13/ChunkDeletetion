package org.jkadem.test1.commands;

import org.jkadem.test1.ChunkDestroyer;
import org.jkadem.test1.commands.CDCommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Objects;

public class CDCommands implements CommandExecutor{

  private final ChunkDestroyer cd;

  public CDCommands(ChunkDestroyer cd) {this.cd = cd;}

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    System.out.println("Command Received");

    if (command.getName().equalsIgnoreCase("CDStartChunk")) {
      return handleStartChunk(sender, command);
    }
    else if (command.getName().equalsIgnoreCase("CDStopChunk")) {
      return handleStopChunk(sender, command);
    }
    else if (command.getName().equalsIgnoreCase("CDSetTimerDuration")) {
      return handleSetTimerDuration(sender, command, args);
    }
    else if (command.getName().equalsIgnoreCase("CDResetTimers")) {
      return handleResetTimerDuration(sender, command);
    }
    //... Continue adding commands here as needed
    System.out.println("Error, command not handled");
    return false;
  }

  private boolean handleStartChunk(CommandSender sender, Command command) {
    if (cd.cdManager().isCheckerRunning()) {
      cd.cdManager().startChunkChecker();
      sender.sendMessage("Chunk checker started.");
    }
    else {
      sender.sendMessage("Chunk checker is already running.");
    }
    return true;
  }

  private boolean handleStopChunk(CommandSender sender, Command command) {
    if (!cd.cdManager().isCheckerRunning()) {
      cd.cdManager().stopChunkChecker();
      sender.sendMessage("Chunk checker stopped.");
    }
    else {
      sender.sendMessage("Chunk checker is not running.");
    }
    return true;
  }

  private boolean handleSetTimerDuration(CommandSender sender, Command command, String[] args) {
    cd.cdManager().setTimerDuration(Integer.parseInt(args[0]));
    return true;
  }

  private boolean handleResetTimerDuration(CommandSender sender, Command command) {

    return true;
  }

}
