package org.jkadem.test1;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class Test1 extends JavaPlugin implements Listener, CommandExecutor {

    private final Map<Chunk, ChunkData> chunkDataMap = new HashMap<>();
    private final Random random = new Random();

    private BukkitTask chunkChecker;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        getCommand("startchunk").setExecutor(this);
        getCommand("stopchunk").setExecutor(this);

        startChunkChecker();
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        players.remove(event.getPlayer()); // Remove the respawning player from the list

        if (!players.isEmpty()) {
            Player randomPlayer = players.get(random.nextInt(players.size()));
            event.setRespawnLocation(randomPlayer.getLocation());
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("startchunk")) {
            if (chunkChecker == null) {
                startChunkChecker();
                sender.sendMessage("Chunk checker started.");
            } else {
                sender.sendMessage("Chunk checker is already running.");
            }
        } else if (command.getName().equalsIgnoreCase("stopchunk")) {
            if (chunkChecker != null) {
                chunkChecker.cancel();
                chunkChecker = null;
                sender.sendMessage("Chunk checker stopped.");
            } else {
                sender.sendMessage("Chunk checker is not running.");
            }
        }

        return true;
    }

    private void startChunkChecker() {
        chunkChecker = new BukkitRunnable() {
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

                    if (chunkData.getTotalTime() >= 600) { // 600 seconds = 10 minutes
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
                        }.runTaskTimer(Test1.this, 0, 20); // Run every second
                    }
                }
            }
        }.runTaskTimer(Test1.this, 0, 20); // Run every second
    }

    private static class ChunkData {
        private final Chunk chunk;
        private final Map<UUID, Integer> playerTimes = new HashMap<>();

        public ChunkData(Chunk chunk) {
            this.chunk = chunk;
        }

        public void addTime(UUID playerUUID, int time) {
            playerTimes.compute(playerUUID, (uuid, oldTime) -> oldTime == null ? time : oldTime + time);
        }

        public int getTotalTime() {
            return playerTimes.values().stream().mapToInt(Integer::intValue).sum();
        }
    }
}
