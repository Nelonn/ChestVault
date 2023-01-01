package me.nelonn.chestvault.commands;

import me.nelonn.chestvault.ChestVaultPlugin;
import me.nelonn.chestvault.api.ChestVault;
import me.nelonn.chestvault.utils.Command;
import me.nelonn.chestvault.utils.TextUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MainCommand extends Command {
    public MainCommand() {
        super("chestvault", "cv");
        setPermission("chestvault.open");
        addChildren(new SetCommand());
    }

    @Override
    protected void onCommand(@NotNull CommandSender sender, @NotNull String command, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return;

        ChestVault chestVault = ChestVaultPlugin.getInstance().getVaultManager().getVault(player);
        if (chestVault == null) {
            TextUtils.send(player, "&cУ вас нет хранилища");
            return;
        }
        if (!chestVault.open(player)) {
            TextUtils.send(player, "&cУ вас нет хранилища, либо доступ к нему затруднён");
        }
        /*block.getChunk().load();
        block.getWorld().loadChunk(block.getChunk());
        ChestVaultPlugin.getInstance().getVaultManager().loadChunk(player, block.getChunk());
        Inventory inventory = null;
        BlockState blockState = block.getState();
        if (blockState instanceof Container container) {
            inventory = container.getInventory();
        }
        if (inventory == null) {
            TextUtils.send(player, "&cПроизошла ошибка");
            return;
        }
        player.openInventory(inventory);*/
    }
}
