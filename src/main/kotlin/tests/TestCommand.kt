package org.hyrical.data.tests

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.HelpCommand
import org.bukkit.Material
import org.bukkit.entity.Player
import org.hyrical.data.Data
import org.hyrical.data.cache.CacheManager
import org.hyrical.data.menus.ItemBuilder
import org.hyrical.data.profile.repository.ProfileRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class TestCommand @Autowired constructor(val cacheManager: CacheManager, val profileRepository: ProfileRepository) : BaseCommand() {

    init {
        Data.instance.manager.registerCommand(this)
    }

    @CommandAlias("test")
    fun onTest(player: Player) {
        player.sendMessage(cacheManager.cache[player.uniqueId.toString()]!!.isOnline.toString())

        val item = ItemBuilder {
            type(Material.DIAMOND_PICKAXE)
            print(this.itemStack.type)
            name("&a&lTest")
        }

        player.inventory.addItem(item)
        player.updateInventory()
    }
}