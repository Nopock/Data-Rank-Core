package org.hyrical.data.menus

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.hyrical.data.Data

class Button(
    var item: (Player) -> ItemStack,
    var action: (Player, InventoryClickEvent) -> Unit = { _, _ -> } ,
    var updatingInterval: Long = -1L
) {

    private var taskStarted: Boolean = false

    companion object {
        fun of(itemstack: ItemStack): Button {
            return Button({ itemstack })
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