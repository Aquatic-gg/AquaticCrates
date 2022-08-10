package cz.larkyy.aquaticcratestesting.model;


import org.bukkit.entity.Player;

public interface IModel {

    void playAnimation(String animation);

    void show();

    void hide();

    void show(Player player);

    void hide(Player player);

    void remove();
}
