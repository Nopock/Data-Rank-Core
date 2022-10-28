package org.hyrical.data.rank.menus

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.hyrical.data.menus.*
import org.hyrical.data.rank.Rank
import org.hyrical.data.rank.service.RankService
import reactor.core.publisher.toMono

class RankListMenu(val rankService: RankService) : PaginatedMenu({"Rank List" }, 27) {
    override fun getPaginatedButtons(): List<Button> {
        val buttons = mutableListOf<Button>()

        for (rank in rankService.all(true)) {
            buttons.add(rankButton(rank))

            Bukkit.broadcastMessage(rank.name)
        }

        return buttons
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

    override fun getButtonsPerPage(): Int {
        return 14
    }
}