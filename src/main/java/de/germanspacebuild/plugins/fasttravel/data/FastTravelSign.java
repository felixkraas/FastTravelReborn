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

package de.germanspacebuild.plugins.fasttravel.data;

import de.germanspacebuild.plugins.fasttravel.util.BlockUtil;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.material.Sign;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by oneill011990 on 03.03.2015.
 */
public class FastTravelSign implements Comparable<FastTravelSign> {

    private int id;
    private String name;
    private Location tpLoc;
    private Location signLoc;
    private Boolean automatic;
    private double price;
    private int range;
    private UUID creator;
    private List<UUID> players;
    private Boolean marker;

    /**
     * Constructor for sign without price.
     *
     * @param name    Name of sign.
     * @param creator Lame of creator.
     * @param block   Location of sign.
     */
    public FastTravelSign(String name, UUID creator, Block block) {

        if (FastTravelDB.getAllSigns().size() == 0) {
            this.id = FastTravelDB.getAllSigns().size();
        } else {
            this.id = FastTravelDB.getAllSigns().size() + 1;
        }

        this.name = name;
        this.creator = creator;

        this.price = 0;
        this.range = 0;
        this.players = new ArrayList<>();
        this.setAutomatic(false);

        this.signLoc = block.getLocation();
        Sign s = (Sign) block.getState().getData();
        this.signLoc.setYaw((float) BlockUtil.getYawForFace(s.getFacing()));
        this.tpLoc = signLoc;
    }

    /**
     * Constructor for sign with price.
     *
     * @param name      Name of sign.
     * @param creator   Name of creator.
     * @param price     Price for travel.
     * @param location  Location of sign.
     * @param tpLoc     Travel destination.
     * @param automatic Accessible for all players?
     * @param players   Players that can use this sign.
     */
    public FastTravelSign(int id, String name, UUID creator, double price, Location location, Location tpLoc,
                          boolean automatic, int range, List<UUID> players) {
        this.id = id;
        this.name = name;
        this.creator = creator;
        this.price = price;
        this.range = range;
        this.players = players;
        this.setAutomatic(automatic);
        this.signLoc = location;
        this.tpLoc = tpLoc;
    }




    public void addPlayer(UUID player){
        players.add(player);
        FastTravelDB.save();
    }

    public void clearPlayers(){
        players.clear();
        FastTravelDB.save();
    }

    public void removePlayer(UUID player){
        players.remove(player);
        FastTravelDB.save();
    }

    public boolean foundBy(UUID player) {
        return players.contains(player);
    }

    @Override
    public int compareTo(FastTravelSign sign) {
        return this.name.toLowerCase().compareTo(sign.getName().toLowerCase());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        FastTravelDB.save();
    }

    public Location getTPLocation() {
        return tpLoc;
    }

    public void setTPLocation(Location tpLoc) {
        this.tpLoc = tpLoc;
        FastTravelDB.save();
    }

    public Location getSignLocation() {
        return signLoc;
    }

    public void setSignLocation(Location signLoc) {
        this.signLoc = signLoc;
        FastTravelDB.save();
    }

    public Boolean isAutomatic() {
        return automatic;
    }

    public void setAutomatic(Boolean automatic) {
        this.automatic = automatic;
        FastTravelDB.save();
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
        FastTravelDB.save();
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
        FastTravelDB.save();
    }

    public UUID getCreator() {
        return creator;
    }

    public void setCreator(UUID creator) {
        this.creator = creator;
        FastTravelDB.save();
    }

    public List<UUID> getPlayers() {
        return players;
    }

    public Boolean hasMarker() {
        return marker;
    }

    public void setMarker(Boolean marker) {
        this.marker = marker;
        FastTravelDB.save();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString(){
        return getName();
    }

}
