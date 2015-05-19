/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2011-2015 CraftyCreeper, minebot.net, oneill011990
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

package de.germanspacebuild.plugins.fasttravel.menu;

import de.germanspacebuild.plugins.fasttravel.data.FastTravelDB;
import de.germanspacebuild.plugins.fasttravel.data.FastTravelSign;
import de.germanspacebuild.plugins.fasttravel.events.FastTravelEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oneill011990 on 13.12.2014.
 */
public class SignMenu {

    public static List<SignMenu> menus = new ArrayList<>();

    private Player player;
    private List<FastTravelSign> signs;
    private List<ItemStack> items;
    private int sites;
    private List<Inventory> inventories;
    private boolean multisited;
    private int currentSite;

    public SignMenu(Player player) {
        this.player = player;

        signs = new ArrayList<FastTravelSign>();
        inventories = new ArrayList<Inventory>();

        signs.addAll(FastTravelDB.getSignsFor(player.getUniqueId()));

        items = new ArrayList<ItemStack>();

        sites = 1;

        createItemStacks();
        createInventories();
        fillInventories();

    }

    public void createItemStacks(){
        for (FastTravelSign sign : signs) {
            ItemStack item = new ItemStack(Material.BEACON, 1);
            ItemMeta meta = item.getItemMeta();

            meta.setDisplayName(ChatColor.YELLOW + sign.getName());

            List<String> lore = new ArrayList<String>();

            if (sign.getPrice() != 0.0){
                lore.add("Price: " + sign.getPrice());
            } else if (sign.getRange() > 0){
                lore.add("Range: " + sign.getRange());
            }

            meta.setLore(lore);

            item.setItemMeta(meta);

            items.add(item);
        }
    }

    public void createInventories(){

        int signCount = signs.size();
        multisited = false;

        //maximum of 44 signs per page
        if (signCount/44 > 1){
            sites = floorDiv(signCount, 44);
            multisited = true;
        }

        if (multisited){
            for (int i = 0; i < sites; i++){
                inventories.add(Bukkit.getServer().createInventory(player, 54,  ChatColor.DARK_AQUA + "FastTravels " +
                        i + "/" + sites));
            }
        } else {
            inventories.add(Bukkit.getServer().createInventory(player, 54, ChatColor.DARK_AQUA + "FastTravels"));
        }

    }


    public void fillInventories(){
        for (int i = 0; i < sites; i++){
            for (int j = 0; j < 44 && j < signs.size(); j++) {
                if (multisited){
                    inventories.get(i).setItem(j, items.get((i * 44) + j));
                } else {
                    inventories.get(0).setItem(j, items.get(j));
                }
            }

            if (multisited){

                ItemStack back = new ItemStack(Material.CARROT_ITEM, 1);
                ItemMeta backMeta = back.getItemMeta();
                backMeta.setDisplayName(ChatColor.YELLOW + "Back");
                back.setItemMeta(backMeta);
                inventories.get(i).setItem(45, back);

                ItemStack next = new ItemStack(Material.CARROT_ITEM, 1);
                ItemMeta nextMeta = next.getItemMeta();
                nextMeta.setDisplayName(ChatColor.YELLOW + "Next");
                next.setItemMeta(nextMeta);
                inventories.get(i).setItem(53, next);

            }
        }


    }

    public List<Inventory> getInventories() {
        return inventories;
    }

    public boolean isMultisited() {
        return multisited;
    }

    public int getSites() {
        return sites;
    }

    public void open(int page) {
        player.openInventory(inventories.get(page-1));
        currentSite = 1;
    }

    public void goNext(){
        if (currentSite < sites){
            player.openInventory(inventories.get(currentSite+1));
            currentSite++;
        }
    }

    public void goBack(){
        if (currentSite > sites){
            player.openInventory(inventories.get(currentSite-1));
            currentSite--;
        }
    }

    public int getCurrentSite(){
        return currentSite;
    }

    public void travel(String displayName) {
        if (FastTravelDB.getSign(displayName.substring(2)) == null){
            return;
        }
        player.closeInventory();
        Bukkit.getServer().getPluginManager().callEvent(new FastTravelEvent(player,
                FastTravelDB.getSign(displayName.substring(2))));
    }

    public static List<SignMenu> getMenus(){
        return menus;
    }

    private static int floorDiv(int x, int y) {
        int r = x / y;
        // if the signs are different and modulo not zero, round down
        if ((x ^ y) < 0 && (r * y != x)) {
            r--;
        }
        return r;
    }
}
