package org.hyrical.data.punishment.commands


import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Name
import org.bukkit.entity.Player
import org.hyrical.data.commands.ICommand
import org.hyrical.data.profile.impl.CachedProfile
import org.hyrical.data.profile.service.ProfileService
import org.hyrical.data.punishment.menus.CheckPunishmentsMenu
import org.hyrical.data.punishment.repository.PunishmentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.DependsOn
import org.springframework.stereotype.Component

@DependsOn("rankResolver")
@Component
class CheckPunishmentsCommand @Autowired constructor(val profileService: ProfileService, val punishmentRepository: PunishmentRepository) : ICommand() {

    @CommandAlias("c|cp|checkpunishments")
    @CommandPermission("data.admin.cp")
    fun onCheckPunishments(player: Player, @Name("target") cachedProfile: CachedProfile) {
        CheckPunishmentsMenu(cachedProfile, punishmentRepository, profileService).open(player)
    }
}