package me.nelonn.chestvault;

import io.th0rgal.protectionlib.ProtectionLib;
import me.nelonn.chestvault.api.ChestVault;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class BlockChestVault implements ChestVault {
    private final UUID ownerUniqueId;
    private final Location location;

    public BlockChestVault(@NotNull UUID ownerUniqueId, @NotNull Location location) {
        this.ownerUniqueId = ownerUniqueId;
        this.location = location;
    }

    @Override
    public boolean open(@NotNull Player player) {
        Block block = getBlock();
        if ((block.getType() != Material.CHEST && block.getType() != Material.BARREL) || !ProtectionLib.canBreak(player, location) || !ProtectionLib.canBuild(player, location)) {
            ChestVaultPlugin.getInstance().getVaultManager().removeVault(ownerUniqueId);
            return false;
        }
        BlockState blockState = block.getState();
        if (!(blockState instanceof Container container)) return false;
        ChestVaultPlugin.getInstance().getChunkManager().loadChunk(player, getChunk());
        player.openInventory(container.getInventory());
        return true;
    }

    @NotNull
    public UUID getOwnerUniqueId() {
        return ownerUniqueId;
    }

    @NotNull
    public OfflinePlayer getOwner() {
        return Bukkit.getOfflinePlayer(getOwnerUniqueId());
    }

    @NotNull
    public Location getLocation() {
        return location;
    }

    @NotNull
    public Chunk getChunk() {
        return getLocation().getChunk();
    }

    @NotNull
    public Block getBlock() {
        return getLocation().getBlock();
    }
}
