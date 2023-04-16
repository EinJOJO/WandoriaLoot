package me.einjojo.wandorialoot.util;

import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

public enum Sounds {
    GUI_CLICK(Sound.ENTITY_CHICKEN_EGG),
    GUI_PAGE_SWITCH(Sound.ITEM_BOOK_PAGE_TURN);

    final Sound sound;
    final float volume;
    final float pitch;
    Sounds(Sound sound) {
        this.sound = sound;
        this.volume = 1;
        this.pitch = 1;
    }
    Sounds(Sound sound, float volume, float pitch) {
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }
    public void play(Player player) {
        player.playSound(player.getLocation(), sound, volume, pitch);
    }
    public void play(HumanEntity entity) {
        if (entity instanceof Player) {
            play((Player) entity);
        }
    }

    public static void playSound(Player player, Sound sound) {
        player.playSound(player.getLocation(), sound, 1, 1);
    }
}
