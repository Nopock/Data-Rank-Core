package org.hyrical.data.menus

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.hyrical.data.Data
import org.hyrical.data.util.ColorUtils

class Button(
    var item: (Player) -> ItemStack,
    var action: (Player, InventoryClickEvent) -> Unit = { _, _ -> } ,
    var updatingInterval: Long = -1L
) {

    private var taskStarted: Boolean = false

    companion object {
        fun of(itemstack: ItemStack): Button {
            return Button(item = { itemstack })
        }

        fun placeholder(color: ChatColor = ChatColor.BLACK): Button {
            return Button(
                { ItemBuilder {
                    type(Material.STAINED_GLASS_PANE)
                    name("&r")
                    ColorUtils.CHAT_COLOR_TO_WOOL_DATA[color]?.toShort()?.let { it1 -> data(it1) }
                }}
            )
        }

    }

    fun runUpdatingTask(menu: Menu, slot: Int, player: Player) {
        if (updatingInterval <= 0) return
        if (taskStarted) return

        taskStarted = true

        val task = Bukkit.getScheduler().runTaskTimer(Data.instance, {
            // TODO: Cancel task if player is not viewing the menu
            menu.updateItem(this, slot, player)
        }, updatingInterval, updatingInterval)


    }
}