package me.nelonn.chestvault.managers;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.BlockInventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChunkManager implements Listener {
    private final Map<Chunk, List<Player>> chunks = new HashMap<>();

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().getHolder() == null || !(event.getInventory().getHolder() instanceof BlockInventoryHolder block)) return;
        Chunk chunk = block.getBlock().getChunk();
        List<Player> players = chunks.get(chunk);
        if (players == null || !players.contains((Player) event.getPlayer())) return;
        players.remove((Player) event.getPlayer());
        if (players.size() != 0) return;
        chunk.setForceLoaded(false);
        chunks.remove(chunk);
    }

    public void loadChunk(@NotNull Player player, @NotNull Chunk chunk) {
        List<Player> players = chunks.get(chunk);
        if (players == null) {
            players = new ArrayList<>();
            chunks.put(chunk, players);
            chunk.setForceLoaded(true);
            chunk.load();
        } else if (players.contains(player)) return;
        players.add(player);
    }

    public void unloadChunk(@NotNull Chunk chunk) {
        if (chunks.remove(chunk) == null) return;
        chunk.getWorld().setChunkForceLoaded(chunk.getX(), chunk.getZ(), false);
    }

    public void unloadAllChunks() {
        for (Chunk chunk : chunks.keySet()) {
            chunk.getWorld().setChunkForceLoaded(chunk.getX(), chunk.getZ(), false);
        }
        chunks.clear();
    }
}
