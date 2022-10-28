package org.hyrical.data.rank.menus

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.hyrical.data.menus.*
import org.hyrical.data.rank.Rank
import org.hyrical.data.rank.service.RankService
import reactor.core.publisher.toMono

class RankListMenu(private val rankService: RankService) : PaginatedMenu({"Rank List" }, 45) {

    var filter: String? = null

    override fun getPaginatedButtons(): List<Button> {
        val time = System.currentTimeMillis()
        val buttons = mutableListOf<Button>()

        when (filter) {
            null -> rankService.all(true).forEach { rank ->
                buttons.add(rankButton(rank))
            }
            "staff" -> rankService.all(true).filter { it.staff }.forEach { rank ->
                buttons.add(rankButton(rank))
            }
            "donator" -> rankService.all(true).filter { it.donator }.forEach { rank ->
                buttons.add(rankButton(rank))
            }
        }

        return buttons.also {
            println("Took ${System.currentTimeMillis() - time}ms to generate buttons")
        }
    }


    private fun rankButton(rank: Rank): Button {
        return Button.of(TexturedButton.MAIL.construct().apply {
            this.itemMeta = this.itemMeta.apply {
                this.displayName = rank.displayName
                this.lore = listOf("§7Click to view this rank's information.")
            }
        }).apply {
            this.action = { _, _ ->
                Bukkit.broadcastMessage("Clicked ${rank.name}")
            }
        }
    }

    private fun filterButton(): Button {
        return Button.of(TexturedButton.RANK_ICON.construct().apply {
            ItemBuilder(this) {
                name("&eRank Filter")
                lore(
                    "&e",
                    if (filter == null) "&a&l➥ &r&7None" else "&7None",
                    if (filter == "staff") "&a&l➥ &r&7Staff" else "&7Staff",
                    if (filter == "donator") "&a&l➥ &r&7Donator" else "&7Donator",
                    "&e",
                    "&7Click to filter the rank list.",
                )
            }
        }).apply {
            this.action = { p, _ ->
                Bukkit.broadcastMessage("Clicked filter")
                this@RankListMenu.filter = when (filter) {
                    null -> "staff"
                    "staff" -> "donator"
                    "donator" -> null
                    else -> null
                }

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