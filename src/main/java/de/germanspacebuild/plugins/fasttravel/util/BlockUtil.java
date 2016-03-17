/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2011-2016 CraftyCreeper, minebot.net, oneill011990
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package de.germanspacebuild.plugins.fasttravel.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.Arrays;

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
     * Checks if location is safe.
     *
     * @param loc Location to check.
     * @return Is the location safe?
     */
    public static boolean safeLocation(Location loc) {
        double y = loc.getY();
        loc.setY(y + 1);
        Block block1 = loc.getWorld().getBlockAt(loc);
        loc.setY(y + 2);
        Block block2 = loc.getWorld().getBlockAt(loc);
        loc.setY(y);
        Material mat1 = block1.getType();
        Material mat2 = block2.getType();
        if ((Arrays.asList(safeBlocks).contains(mat1)) && (Arrays.asList(safeBlocks).contains(mat2)))
            return true;
        return false;
    }


    /**
     * Gets yaw for a blockface.
     *
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
