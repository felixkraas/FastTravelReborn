package de.germanspacebuild.plugins.fasttravel.data;

import de.germanspacebuild.plugins.fasttravel.util.BlockUtil;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.material.Sign;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by oneill011990 on 03.03.2015.
 */
public class FastTravelSign implements Comparable<FastTravelSign> {

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
    public FastTravelSign(String name, UUID creator, double price, Location location, Location tpLoc,
                          boolean automatic, int range, List<UUID> players) {
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

    public void setTpLoc(Location tpLoc) {
        this.tpLoc = tpLoc;
        FastTravelDB.save();
    }

    public Location getSignLocation() {
        return signLoc;
    }

    public void setSignLoc(Location signLoc) {
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
}
