package org.hyrical.data.menus

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.hyrical.data.log
import java.util.concurrent.CompletableFuture

abstract class Menu(private val title: (Player) -> String, private val size: Int) {

    val lastButtons = mutableMapOf<Int, Button>()

    fun open(player: Player) {
        player.closeInventory()
        val inventory = Bukkit.createInventory(null, size, title.invoke(player))

        player.openInventory(inventory)

        updateItems(getButtons(player).also {
            lastButtons.clear()
            lastButtons.putAll(it)
        }, player)

        MenuHandler.openedMenusMap[player.uniqueId] = this
    }

    fun redirect(player: Player, menu: Menu) {
        if (menu.size != size) {
            menu.open(player)
            return
        }

        val inventory = player.openInventory.topInventory
        inventory.clear()
        updateItems(menu.getButtons(player).also {
            lastButtons.clear()
            lastButtons.putAll(it)
        }, player)

        MenuHandler.openedMenusMap[player.uniqueId] = menu
    }

    fun updateItems(buttons: Map<Int, Button>, player: Player, clear: Boolean = false) {
        val inventory = player.openInventory.topInventory

        log("Updating items for ${player.name} in ${this.title.invoke(player)}")

        if (clear) inventory.clear() 

        CompletableFuture.runAsync {
            buttons.forEach { button ->
                val item = button.value.item.invoke(player)
                
                if (inventory.getItem(button.key) != item) {

                    inventory.setItem(button.key, button.value.item.invoke(player)).also {
                        log("Set item ${item.type} at slot ${button.key} for ${player.name}")
                        button.value.runUpdatingTask(this, button.key, player)
                    }
                }
            }
        }
    }

    fun updateItem(button: Button, slot: Int, player: Player) {
        player.openInventory.topInventory.setItem(slot, button.item.invoke(player))
    }

    fun updateItem(itemStack: ItemStack, slot: Int, player: Player) {
        player.openInventory.topInventory.setItem(slot, itemStack)
    }

    abstract fun getButtons(player: Player): Map<Int, Button>

    open fun onClose(player: Player) {

    }

    fun playSound(player: Player, sound: MenuSound) {
        player.playSound(player.getLocation(), sound.sound, sound.volume, sound.pitch)
    }

}