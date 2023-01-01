package me.nelonn.chestvault.api;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface ChestVault {
    boolean open(@NotNull Player player);

    @NotNull
    UUID getOwnerUniqueId();

    @NotNull
    OfflinePlayer getOwner();

    @NotNull
    Location getLocation();

    @NotNull
    Chunk getChunk();

    @NotNull
    Block getBlock();
}
