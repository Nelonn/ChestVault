package me.nelonn.chestvault.commands;

import com.google.common.collect.Sets;
import io.th0rgal.protectionlib.ProtectionLib;
import me.nelonn.chestvault.ChestVaultPlugin;
import me.nelonn.chestvault.utils.Command;
import me.nelonn.chestvault.utils.TextUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetCommand extends Command {
    public SetCommand() {
        super("set");
        setPermission("chestvault.set");
    }

    @Override
    protected void onCommand(@NotNull CommandSender sender, @NotNull String command, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return;

        Block block = player.getTargetBlock(Sets.newHashSet(Material.AIR, Material.CAVE_AIR, Material.VOID_AIR, Material.WATER, Material.LAVA), 5);
        if (block.getType() != Material.CHEST && block.getType() != Material.BARREL) {
            TextUtils.send(player, "&cНужно смотреть на сундук или бочку");
            return;
        }
        if (!ProtectionLib.canBuild(player, block.getLocation()) || !ProtectionLib.canBreak(player, block.getLocation())) {
            TextUtils.send(player, "&cВам запрещён доступ к этому блоку");
            return;
        }
        ChestVaultPlugin.getInstance().getVaultManager().setVault(player, block.getLocation());
        TextUtils.send(player, "&aХранилище успешно установлено");
    }
}
