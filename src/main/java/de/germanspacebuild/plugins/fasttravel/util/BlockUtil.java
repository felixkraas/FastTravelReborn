package de.germanspacebuild.plugins.fasttravel.util;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

/**
 * Created by oneill011990 on 03.03.15.
 * Copyright
 *
 * @author oneill011990
 */
public class BlockUtil {

    /**
     * Blocks that are safe to be traveled to.
     */
    public static Material[] safeBlocks = {Material.AIR, Material.SIGN, Material.SIGN_POST, Material.TORCH, Material.REDSTONE_TORCH_ON,
            Material.REDSTONE_TORCH_OFF, Material.REDSTONE, Material.LONG_GRASS, Material.YELLOW_FLOWER,
            Material.CROPS, Material.DEAD_BUSH};

    /**
     * Blocks that are Signs in Bukkit.
     */
    public static Material[] signBlocks = {Material.SIGN_POST, Material.SIGN, Material.WALL_SIGN};


    /**
     * Gets yaw for a blockface.
     * @param face Face to get yaw from.
     * @return Yaw of the face.
     */
    public static int getYawForFace(BlockFace face) {
        int dir;
        switch (face) {
            case NORTH:
                dir = 0;
                break;
            case NORTH_NORTH_EAST:
            case NORTH_EAST:
            case EAST_NORTH_EAST:
                dir = 45;
                break;
            case EAST:
                dir = 90;
                break;
            case EAST_SOUTH_EAST:
            case SOUTH_EAST:
            case SOUTH_SOUTH_EAST:
                dir = 135;
                break;
            case SOUTH:
                dir = 180;
                break;
            case SOUTH_SOUTH_WEST:
            case SOUTH_WEST:
            case WEST_SOUTH_WEST:
                dir = 225;
                break;
            case WEST:
                dir = 270;
                break;
            case WEST_NORTH_WEST:
            case NORTH_WEST:
            case NORTH_NORTH_WEST:
                dir = 315;
                break;
            default:
                dir = 0;
                break;
        }
        return dir + 90;
    }
}
