package me.nelonn.chestvault.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LocationSerializer {
    private LocationSerializer() {}

    @NotNull
    public static String serialize(String world, double x, double y, double z, float yaw, float pitch) {
        StringBuilder result = new StringBuilder();
        if (world != null && !world.isEmpty()) {
            result.append(world).append(";");
        }
        result.append(x).append(";").append(y).append(";").append(z);
        if (yaw != 0.0F || pitch != 0.0F) {
            result.append(";").append(yaw).append(";").append(pitch);
        }
        return result.toString();
    }

    @NotNull
    public static String serialize(World world, double x, double y, double z, float yaw, float pitch) {
        return serialize(world != null ? world.getName() : null, x, y, z, yaw, pitch);
    }

    @NotNull
    public static String serialize(@NotNull Location location, boolean blockPos) {
        return serialize(location.isWorldLoaded() ? location.getWorld() : null,
                blockPos ? location.getBlockX() : location.getX(),
                blockPos ? location.getBlockY() : location.getY(),
                blockPos ? location.getBlockZ() : location.getZ(),
                location.getYaw(),
                location.getPitch());
    }

    @NotNull
    public static String serialize(@NotNull Location location) {
        return serialize(location, false);
    }

    @Nullable
    public static Location deserialize(@NotNull String string) {
        String[] parts = string.split(";");
        if (parts.length < 3 || parts.length > 6) return null;

        boolean useWorld = parts.length == 4 || parts.length == 6;
        boolean useYawPitch = parts.length >= 5;

        int offset = useWorld ? 1 : 0;

        World world = null;
        if (useWorld) {
            world = Bukkit.getWorld(parts[0]);
        }

        double x, y, z;
        float yaw = 0,
                pitch = 0;

        try {
            x = parseDouble(parts[offset]);
            y = parseDouble(parts[offset + 1]);
            z = parseDouble(parts[offset + 2]);

            if (useYawPitch) {
                yaw = parseFloat(parts[offset + 3]);
                pitch = parseFloat(parts[offset + 4]);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return new Location(world, x, y, z, yaw, pitch);
    }

    private static double parseDouble(@NotNull String string) throws NumberFormatException {
        try {
            return Double.parseDouble(string);
        } catch (Exception e) {
            return Integer.parseInt(string);
        }
    }

    private static float parseFloat(@NotNull String string) throws NumberFormatException {
        try {
            return Float.parseFloat(string);
        } catch (Exception e) {
            return Integer.parseInt(string);
        }
    }
}
