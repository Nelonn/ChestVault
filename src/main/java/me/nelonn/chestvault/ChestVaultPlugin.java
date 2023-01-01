package me.nelonn.chestvault;

import io.th0rgal.protectionlib.ProtectionLib;
import me.nelonn.chestvault.commands.MainCommand;
import me.nelonn.chestvault.managers.ChunkManager;
import me.nelonn.chestvault.managers.VaultManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class ChestVaultPlugin extends JavaPlugin {
    private static ChestVaultPlugin instance;

    private ChunkManager chunkManager;
    private VaultManager vaultManager;

    @Override
    public void onEnable() {
        instance = this;

        chunkManager = new ChunkManager();
        Bukkit.getPluginManager().registerEvents(chunkManager, this);

        vaultManager = new VaultManager(this);

        new MainCommand().register(this);

        ProtectionLib.init(this);
    }

    @Override
    public void onDisable() {
        chunkManager.unloadAllChunks();
        chunkManager = null;
        vaultManager.save();
        vaultManager = null;
        instance = null;
    }

    public ChunkManager getChunkManager() {
        return chunkManager;
    }

    public VaultManager getVaultManager() {
        return vaultManager;
    }

    public static ChestVaultPlugin getInstance() {
        return instance;
    }
}
