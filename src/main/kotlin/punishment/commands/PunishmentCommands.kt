package org.hyrical.data.punishment.commands

import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Name
import co.aikar.commands.annotation.Optional
import co.aikar.commands.annotation.Single
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.hyrical.data.commands.ICommand
import org.hyrical.data.profile.impl.CachedProfile
import org.hyrical.data.profile.service.ProfileService
import org.hyrical.data.punishment.Punishment
import org.hyrical.data.punishment.PunishmentType
import org.hyrical.data.punishment.repository.PunishmentRepository
import org.hyrical.data.translate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.DependsOn
import org.springframework.stereotype.Component
import java.util.*
import kotlin.time.Duration

@Component
@DependsOn("rankResolver", "durationResolver")
class PunishmentCommands @Autowired constructor(val profileService: ProfileService, val punishmentRepo: PunishmentRepository) : ICommand() {


    @CommandAlias("ban")
    @CommandPermission("data.ban")
    fun banCommand(player: Player, @Name("target") profile: CachedProfile, @Name("duration") @Optional duration: Duration?, @Name("reason") reason: String){
        val playerProfile = profileService.search(player.uniqueId)!!

        punish(PunishmentType.BAN, profile, playerProfile, reason, duration, player)
    }

    @CommandAlias("mute")
    @CommandPermission("data.mute")
    fun muteCommand(player: Player, @Name("target") profile: CachedProfile, @Name("duration") @Optional duration: Duration?, @Name("reason") reason: String){
        val playerProfile = profileService.search(player.uniqueId)!!

        punish(PunishmentType.MUTE, profile, playerProfile, reason, duration, player)
    }

    @CommandAlias("blacklist")
    @CommandPermission("data.blacklist")
    fun blacklistCommand(player: Player, @Name("target") profile: CachedProfile, @Name("duration") @Optional duration: Duration?, @Name("reason") reason: String){
        val playerProfile = profileService.search(player.uniqueId)!!

        punish(PunishmentType.BLACKLIST, profile, playerProfile, reason, duration, player)
    }

    @CommandAlias("warn")
    @CommandPermission("data.warn")
    fun warnCommand(player: Player, @Name("target") profile: CachedProfile, @Name("reason") reason: String){
        if (!profile.isOnline) {
            player.sendMessage(translate("&cThat player is not online!"))
            return
        }

        val playerProfile = profileService.search(player.uniqueId)!!

        punish(PunishmentType.WARN, profile, playerProfile, reason, null, player)

        Bukkit.getPlayer(profile.uuid)?.sendMessage(translate("&cYou have been warned for &f${reason.replace("-s", "")}"))
    }

    fun execute(punishment: Punishment, silent: Boolean, targetProfile: CachedProfile, playerProfile: CachedProfile) {
        val msg = (if (silent) "&7[Silent] " else "") + "&r" + profileService.getChatColor(targetProfile) + "${targetProfile.name} &chas been ${punishment.punishmentType.punishmentMessage} by &r${profileService.getChatColor(playerProfile) + playerProfile.name}&r&c."
            //  TODO: Add hover event to show reason and duration
        if (silent) {
            Bukkit.getOnlinePlayers().filter { it.hasPermission("data.staff") }.forEach {
                it.sendMessage(translate(msg))
            }

            return
        }

        Bukkit.broadcastMessage(translate(msg))
    }

    fun punish(type: PunishmentType, targetProfile: CachedProfile, playerProfile: CachedProfile, reason: String, duration: Duration?, player: Player) {
        if (type != PunishmentType.WARN && targetProfile.punishments.any { it.active && it.punishmentType == type }) {
            player.sendMessage(translate("&r" + profileService.getHighestRank(targetProfile)!!.color + targetProfile.name + " &calready has an active punishment of that type."))
            return
        }

        if (profileService.getHighestRank(targetProfile)!!.weight > profileService.getHighestRank(playerProfile)!!.weight){
            player.sendMessage(translate("&cYou cannot punish someone higher than you!"))
            return
        }

        val punishmentLength = duration?.inWholeMilliseconds ?: -1L

        Punishment(
            UUID.randomUUID(),
            targetProfile.id,
            issuedBy = player.uniqueId,
            issuedAt = System.currentTimeMillis(),
            punishmentType = type,
            issuedReason = reason,
            duration = punishmentLength
        ).also { punishment ->
            punishmentRepo.save(punishment).subscribe {
                targetProfile.punishments.add(punishment)
            }

            this.execute(punishment, reason.contains("-s"), targetProfile, playerProfile)

            if (type == PunishmentType.BLACKLIST || type == PunishmentType.BAN) {
                if (Bukkit.getPlayer(targetProfile.id) != null) {
                    Bukkit.getPlayer(targetProfile.id)!!
                        .kickPlayer(translate("&cYou have been banned for ${reason.replace("-s", "")}"))
                }
            }
        }
    }
}