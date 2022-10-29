package org.hyrical.data.util

import org.bukkit.ChatColor

object ColorUtils {

    val CHAT_COLOR_TO_DYE_DATA = mapOf(
        ChatColor.WHITE to 15,
        ChatColor.GOLD to 14,
        ChatColor.AQUA to 12,
        ChatColor.YELLOW to 11,
        ChatColor.GREEN to 10,
        ChatColor.LIGHT_PURPLE to 9,
        ChatColor.GRAY to 8,
        ChatColor.DARK_GRAY to 7,
        ChatColor.DARK_AQUA to 6,
        ChatColor.DARK_PURPLE to 5,
        ChatColor.BLUE to 4,
        ChatColor.DARK_GREEN to 2,
        ChatColor.RED to 1,
        ChatColor.DARK_RED to 1,
        ChatColor.BLACK to 0
    )

    val CHAT_COLOR_TO_WOOL_DATA = mapOf(
        ChatColor.DARK_RED to 14,
        ChatColor.RED to 14,
        ChatColor.GOLD to 1,
        ChatColor.YELLOW to 4,
        ChatColor.GREEN to 5,
        ChatColor.DARK_GREEN to 13,
        ChatColor.DARK_AQUA to 9,
        ChatColor.AQUA to 3,
        ChatColor.BLUE to 11,
        ChatColor.DARK_PURPLE to 10,
        ChatColor.LIGHT_PURPLE to 2,
        ChatColor.WHITE to 0,
        ChatColor.GRAY to 8,
        ChatColor.DARK_GRAY to 7,
        ChatColor.BLACK to 15
    )

    val COLOR_TO_CHAT_COLOR = mapOf(
        "&f" to ChatColor.WHITE,
        "&6" to ChatColor.GOLD,
        "&b" to ChatColor.AQUA,
        "&e" to ChatColor.YELLOW,
        "&a" to ChatColor.GREEN,
        "&d" to ChatColor.LIGHT_PURPLE,
        "&7" to ChatColor.GRAY,
        "&8" to ChatColor.DARK_GRAY,
        "&3" to ChatColor.DARK_AQUA,
        "&5" to ChatColor.DARK_PURPLE,
        "&1" to ChatColor.BLUE,
        "&2" to ChatColor.DARK_GREEN,
        "&c" to ChatColor.RED,
        "&4" to ChatColor.DARK_RED,
        "&0" to ChatColor.BLACK
    )
}