package org.hyrical.data.menus

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class MenuListeners : Listener {

    @EventHandler
    fun onInventoryClose(event: InventoryCloseEvent) {
        if (MenuHandler.openedMenusMap.containsKey(event.player.uniqueId)) {
            MenuHandler.openedMenusMap[event.player.uniqueId]?.onClose(event.player as Player)
            MenuHandler.openedMenusMap.remove(event.player.uniqueId)
        }
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        if (MenuHandler.openedMenusMap.containsKey(event.whoClicked.uniqueId)) {
            event.isCancelled = true
            MenuHandler.openedMenusMap[event.whoClicked.uniqueId]?.lastButtons?.forEach { button ->
                if (button.key == event.slot) {
                    (event.whoClicked as Player).openInventory.topInventory.setItem(event.slot, button.value.item.invoke(event.whoClicked as Player))
                    button.value.action.invoke(event.whoClicked as Player, event)
                }
            }
        }
    }
}