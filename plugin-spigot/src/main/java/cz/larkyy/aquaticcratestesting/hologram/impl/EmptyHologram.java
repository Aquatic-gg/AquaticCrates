package cz.larkyy.aquaticcratestesting.hologram.impl;

import cz.larkyy.aquaticcratestesting.hologram.Hologram;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public class EmptyHologram extends Hologram {
    public EmptyHologram(Location location, List<String> lines) {
        super(location, lines);
    }

    @Override
    public void teleport(Location location) {

    }

    @Override
    public void move(Location location) {

    }

    @Override
    public void despawn() {

    }

    @Override
    public void spawn(List<Player> visitors) {

    }

    @Override
    public void hide() {

    }

    @Override
    public void show() {

    }

    @Override
    public void update() {

    }
}
