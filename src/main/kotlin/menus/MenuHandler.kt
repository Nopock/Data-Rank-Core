package org.hyrical.data.menus

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

object MenuHandler {
    val openedMenusMap = mutableMapOf<UUID, Menu>()

    fun setup(plugin: JavaPlugin) {
        Bukkit.getPluginManager().registerEvents(MenuListeners(), plugin)
    }
}