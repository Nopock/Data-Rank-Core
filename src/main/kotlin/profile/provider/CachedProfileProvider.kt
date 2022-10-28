package org.hyrical.data.profile.provider

import co.aikar.commands.BukkitCommandExecutionContext
import co.aikar.commands.InvalidCommandArgument
import co.aikar.commands.contexts.ContextResolver
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.hyrical.data.Data
import org.hyrical.data.cache.CacheManager
import org.hyrical.data.profile.impl.CachedProfile
import org.hyrical.data.profile.service.ProfileService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class CachedProfileProvider @Autowired constructor(val profileService: ProfileService) : ContextResolver<CachedProfile, BukkitCommandExecutionContext> {

    init {
        Data.instance.manager.commandContexts.registerContext(CachedProfile::class.java, this)
    }

    override fun getContext(c: BukkitCommandExecutionContext?): CachedProfile {
        val sender = c?.player
            ?: throw IllegalStateException("Sender is not a player!")

        val player = Bukkit.getOfflinePlayer(c.popFirstArg() ?: sender.name)
            ?: throw InvalidCommandArgument(ChatColor.RED.toString() + "The player you specified is not registered with our services.")

        return profileService.search(player.uniqueId)
            ?: throw InvalidCommandArgument(ChatColor.RED.toString() + "The player you specified is not registered with our services.")
    }
}