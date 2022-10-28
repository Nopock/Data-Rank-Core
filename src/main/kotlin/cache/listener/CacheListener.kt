package org.hyrical.data.cache.listener

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.hyrical.data.Data
import org.hyrical.data.cache.CacheManager
import org.hyrical.data.log
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class CacheListener @Autowired constructor(val cacheManager: CacheManager) : Listener {

    init {
        log("CacheListener initialized")
        Bukkit.getPluginManager().registerEvents(this, Data.instance)
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onAsyncJoin(player: AsyncPlayerPreLoginEvent) {
        if (cacheManager.cache.containsKey(player.uniqueId.toString())) return

        log("Registering ${player.name} to cache")
        cacheManager.cache(player.uniqueId, player.name).also {
            TODO("Set their permissions")
        }
    }
}