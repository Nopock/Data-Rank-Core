package org.hyrical.data.menus

import org.bukkit.Sound

enum class MenuSound(val sound: Sound, val volume: Float, val pitch: Float) {
    FAIL(Sound.DIG_GRASS, 20.0F, 0.1F),
    SUCCESS(Sound.NOTE_PIANO, 20.0F, 15.0F),
    CLICK(Sound.CLICK, 20.0F, 1.0F),
}