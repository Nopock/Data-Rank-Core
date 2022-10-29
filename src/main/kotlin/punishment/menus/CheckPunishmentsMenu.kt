package org.hyrical.data.punishment.menus

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.hyrical.data.CONSOLE_UUID
import org.hyrical.data.menus.*
import org.hyrical.data.profile.impl.CachedProfile
import org.hyrical.data.profile.service.ProfileService
import org.hyrical.data.punishment.Punishment
import org.hyrical.data.punishment.PunishmentType
import org.hyrical.data.punishment.repository.PunishmentRepository
import org.hyrical.data.rank.Rank
import org.hyrical.data.rank.service.RankService
import org.hyrical.data.util.ColorUtils
import org.hyrical.data.util.TimeUtil
import java.util.*

class CheckPunishmentsMenu(val profile: CachedProfile, private val punishmentRepository: PunishmentRepository, val profileService: ProfileService) : PaginatedMenu({"Punishments >> ${profile.name}" }, 45) {

    private var type: PunishmentType? = null

    override fun getPaginatedButtons(): List<Button> {
        val buttons = mutableListOf<Button>()

        when(type) {
            null -> profile.punishments.forEach {
                buttons.add(punishmentButton(it))
            }

            PunishmentType.WARN -> profile.punishments.filter { it.punishmentType == PunishmentType.WARN }.forEach {
                buttons.add(punishmentButton(it))
            }

            PunishmentType.MUTE -> profile.punishments.filter { it.punishmentType == PunishmentType.MUTE }.forEach {
                buttons.add(punishmentButton(it))
            }

            PunishmentType.BAN -> profile.punishments.filter { it.punishmentType == PunishmentType.BAN}.forEach {
                buttons.add(punishmentButton(it))
            }

            PunishmentType.BLACKLIST -> profile.punishments.filter { it.punishmentType == PunishmentType.BLACKLIST}.forEach {
                buttons.add(punishmentButton(it))
            }
        }

        return buttons
    }


    private fun punishmentButton(punishment: Punishment): Button {
        val lore = mutableListOf(
            BAR,
            "&eBy: &f${profileService.search(punishment.issuedBy)!!.username}",
            "&eReason: &f${punishment.issuedReason}",
            "&eDuration: &f${if (punishment.duration == -1L) "Permanent" else TimeUtil.formatIntoDetailedString((punishment.duration / 1000).toInt())}",
            "&eType: &f${punishment.punishmentType.name}",
            BAR,
        )

        if (punishment.active) {
            lore.add("&eClick to &c&lREVOKE&e this punishment.")
        } else {
            lore.addAll(
                listOf(
                    "&eRemoved By: &f${
                        if (punishment.removedBy == CONSOLE_UUID) "Console" else profileService.search(
                            punishment.removedBy!!
                        )!!.username
                    }",
                    "&eRemoved At: &f${TimeUtil.formatIntoFullCalendarString(Date(punishment.removedAt!!))}",
                    "&eRemoved Reason: &f${punishment.removedReason}",
                    BAR
                )
            )
        }

        return Button.of(ItemBuilder {
            type(Material.WOOL)
            data(ColorUtils.CHAT_COLOR_TO_WOOL_DATA[if (punishment.active) ChatColor.GREEN else ChatColor.RED]!!.toShort())
            name("&6" + TimeUtil.formatIntoFullCalendarString(Date(punishment.issuedAt)))

            lore(lore)

            // TODO: Click to remove and add lore
        }).apply {
            this.action = { p, _ ->
            }
        }
    }

    private fun filterButton(): Button {
        return Button.of(TexturedButton.FIRST_FILTER.construct().apply {
            ItemBuilder(this) {
                name("&eType")
                lore(
                    "&e",
                    if (this@CheckPunishmentsMenu.type == null) "&a&l➥ &r&7All" else "&7All",
                    if (this@CheckPunishmentsMenu.type == PunishmentType.WARN) "&a&l➥ &r&7Warns" else "&7Warns",
                    if (this@CheckPunishmentsMenu.type == PunishmentType.MUTE) "&a&l➥ &r&7Mutes" else "&7Mutes",
                    if (this@CheckPunishmentsMenu.type == PunishmentType.BAN) "&a&l➥ &r&7Bans" else "&7Bans",
                    if (this@CheckPunishmentsMenu.type == PunishmentType.BLACKLIST) "&a&l➥ &r&7Blacklists" else "&7Blacklists",
                    "&e",
                    "&7Click to filter the punishment type.",
                )
            }
        }).apply {
            this.action = { p, _ ->
                this@CheckPunishmentsMenu.type = when (type) {
                    null -> PunishmentType.WARN
                    PunishmentType.WARN -> PunishmentType.MUTE
                    PunishmentType.MUTE -> PunishmentType.BAN
                    PunishmentType.BAN -> PunishmentType.BLACKLIST
                    PunishmentType.BLACKLIST -> null
                }

                playSound(player = p, MenuSound.CLICK)
                updateItems(getButtons(p), p, true)
            }
        }
    }

    override fun getGlobalButtons(): Map<Int, Button> {
        return mapOf(
            1 to Button.placeholder(),
            2 to Button.placeholder(),
            3 to Button.placeholder(),
            4 to Button.placeholder(),
            5 to Button.placeholder(),
            6 to Button.placeholder(),
            7 to Button.placeholder(),
            17 to Button.placeholder(),
            18 to Button.placeholder(),
            26 to Button.placeholder(),
            35 to Button.placeholder(),
            36 to Button.placeholder(),
            37 to Button.placeholder(),
            38 to Button.placeholder(),
            39 to Button.placeholder(),
            40 to Button.placeholder(),
            41 to Button.placeholder(),
            42 to Button.placeholder(),
            43 to Button.placeholder(),
            44 to Button.placeholder(),
            9 to filterButton(),
        )
    }

    override fun getButtonPositions(): List<Int> {
        return listOf(
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34
        )
    }

    override fun getButtonsPerPage(): Int {
        return 21
    }
}