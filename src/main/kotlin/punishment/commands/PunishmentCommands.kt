package org.hyrical.data.punishment.commands

import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Name
import co.aikar.commands.annotation.Single
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.hyrical.data.commands.ICommand
import org.hyrical.data.profile.impl.CachedProfile
import org.hyrical.data.profile.service.ProfileService
import org.hyrical.data.punishment.PunishmentType
import org.hyrical.data.punishment.repository.PunishmentRepository
import org.hyrical.data.translate
import org.hyrical.data.util.Duration
import org.springframework.beans.factory.annotation.Autowired

class PunishmentCommands @Autowired constructor(val profileService: ProfileService, val punishmentRepo: PunishmentRepository) : ICommand() {

    @CommandAlias("ban")
    @CommandPermission("data.ban")
    fun banCommand(player: Player, @Name("target") profile: CachedProfile, @Name("duration") @Single duration: Duration, @Name("reason") reason: String){
        val playerProfile = profileService.search(player.uniqueId)!!

        if (profile.punishments.any { it.active && it.punishmentType == PunishmentType.BAN }){
            player.sendMessage(translate("&r" + profileService.getHighestRank(profile)!!.color + profile.name + " &calready has an active punishment of that type."))
            return
        }

        if (duration.getDuration() <= -1){
            player.sendMessage(translate("&cThis time is invalid."))
            return
        }

        if (profileService.getHighestRank(profile)!!.weight > profileService.getHighestRank(playerProfile)!!.weight){
            player.sendMessage(translate("&cYou cannot punish someone higher than you!"))
            return
        }

        
    }
}