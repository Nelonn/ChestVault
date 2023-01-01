package me.nelonn.chestvault.managers;

import me.nelonn.chestvault.BlockChestVault;
import me.nelonn.chestvault.api.ChestVault;
import me.nelonn.chestvault.utils.AutoSaver;
import me.nelonn.chestvault.utils.LocationSerializer;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class VaultManager implements AutoSaver.Saveable {
    private final AutoSaver saver;
    private final Map<UUID, ChestVault> vaults = new ConcurrentHashMap<>();
    private final File file;

    public VaultManager(@NotNull Plugin plugin) {
        file = new File(plugin.getDataFolder(), "data.yml");
        if (file.exists()) {
            FileConfiguration data = YamlConfiguration.loadConfiguration(file);
            for (String uniqueIdString : data.getKeys(false)) {
                if (!data.isString(uniqueIdString)) continue;
                UUID uniqueId = UUID.fromString(uniqueIdString);

                String locationString = data.getString(uniqueIdString);
                assert locationString != null;
                Location location = LocationSerializer.deserialize(locationString);
                if (location == null) continue;

                vaults.put(uniqueId, new BlockChestVault(uniqueId, location));
            }
        }
        saver = new AutoSaver(plugin, this);
    }

    @Nullable
    public ChestVault getVault(@NotNull UUID uniqueId) {
        return vaults.get(uniqueId);
    }

    @Nullable
    public ChestVault getVault(@NotNull OfflinePlayer player) {
        return getVault(player.getUniqueId());
    }

    public void setVault(@NotNull UUID uniqueId, @NotNull Location location) {
        vaults.put(uniqueId, new BlockChestVault(uniqueId, location));
        saver.scheduleSave();
    }

    public void setVault(@NotNull OfflinePlayer player, @NotNull Location location) {
        setVault(player.getUniqueId(), location);
    }

    @Nullable
    public ChestVault removeVault(@NotNull UUID uniqueId) {
        return saver.scheduleSaveIfNull(vaults.remove(uniqueId));
    }

    @Nullable
    public ChestVault removeVault(@NotNull OfflinePlayer player) {
        return removeVault(player.getUniqueId());
    }

    @Override
    public void save() {
        FileConfiguration data = new YamlConfiguration();
        for (Map.Entry<UUID, ChestVault> entry : vaults.entrySet()) {
            Location location = entry.getValue().getLocation();
            if (!location.isWorldLoaded()) continue;
            data.set(entry.getKey().toString(), LocationSerializer.serialize(location, true));
        }
        try {
            data.save(file);
        } catch (Exception e) {
            Bukkit.getLogger().severe("Error while saving chest vault data:");
            e.printStackTrace();
        }
    }

}
