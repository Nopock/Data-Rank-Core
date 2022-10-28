package org.hyrical.data.punishment.listeners

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.hyrical.data.CONSOLE_UUID
import org.hyrical.data.Data
import org.hyrical.data.profile.service.ProfileService
import org.hyrical.data.punishment.PunishmentType
import org.hyrical.data.punishment.repository.PunishmentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class PunishmentListener @Autowired constructor(val profileService: ProfileService, val punishmentRepository: PunishmentRepository) : Listener {

    init {
        Bukkit.getPluginManager().registerEvents(this, Data.instance)
    }

    @EventHandler(priority = EventPriority.LOW)
    fun onAsyncJoin(player: AsyncPlayerPreLoginEvent) {
        val punishments = profileService.search(player.uniqueId)?.punishments ?: return

        for (punishment in punishments) {
            if (!punishment.active && punishment.removedAt == null) {
                punishment.removedAt = System.currentTimeMillis()
                punishment.removedBy = CONSOLE_UUID
                punishment.removedReason = "Expired"
                punishmentRepository.save(punishment).block()
            }
        }

        if (!punishments.none { it.punishmentType == PunishmentType.BLACKLIST && it.active}) {
            player.kickMessage = ChatColor.RED.toString() + "Your account is currently blacklisted from our services..."
            player.loginResult = AsyncPlayerPreLoginEvent.Result.KICK_BANNED
            return
        }

        if (!punishments.none { it.punishmentType == PunishmentType.BAN && it.active }) {
            player.kickMessage = ChatColor.RED.toString() + "Your account is currently banned from our services..."
            player.loginResult = AsyncPlayerPreLoginEvent.Result.KICK_BANNED
            return
        }
    }

}