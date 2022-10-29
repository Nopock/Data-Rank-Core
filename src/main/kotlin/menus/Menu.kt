package org.hyrical.data.menus

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.hyrical.data.log
import java.util.concurrent.CompletableFuture

abstract class Menu(private val title: (Player) -> String, private val size: Int) {

    var lastButtons = mutableMapOf<Int, Button>()

    val BAR = "&7&m---------------------"

    fun open(player: Player) {
        player.closeInventory()
        val inventory = Bukkit.createInventory(null, size, centerMenuTitle(title.invoke(player)))

        player.openInventory(inventory)

        updateItems(getButtons(player).also {
            lastButtons.clear()
            lastButtons.putAll(it)
        }, player, clear = true)

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
        }, player, clear = true)

        MenuHandler.openedMenusMap[player.uniqueId] = menu
    }

    fun updateItems(buttons: Map<Int, Button>, player: Player, clear: Boolean = false) {
        val inventory = player.openInventory.topInventory

        if (clear) inventory.clear()

        lastButtons = buttons.toMutableMap()

        buttons.forEach { button ->
            val item = button.value.item.invoke(player)

            if (inventory.getItem(button.key) != item) {

                inventory.setItem(button.key, button.value.item.invoke(player)).also {
                    button.value.runUpdatingTask(this, button.key, player)
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

    fun centerMenuTitle(t: String): String {
        val builder = StringBuilder()

        val spaces: Int = 27 - ChatColor.stripColor(t).length

        for (i in 0 until spaces) {
            builder.append(" ")
        }
        return builder.toString() + t
    }
}