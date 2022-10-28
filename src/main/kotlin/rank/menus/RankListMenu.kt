package org.hyrical.data.rank.menus

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.hyrical.data.menus.*
import org.hyrical.data.rank.Rank
import org.hyrical.data.rank.service.RankService
import reactor.core.publisher.toMono

class RankListMenu(private val rankService: RankService) : PaginatedMenu({"Rank List" }, 27) {
    override fun getPaginatedButtons(): List<Button> {
        val time = System.currentTimeMillis()
        val buttons = mutableListOf<Button>()

        for (rank in rankService.all(true)) {
            buttons.add(rankButton(rank))
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

    override fun getGlobalButtons(): Map<Int, Button> {
        return super.getGlobalButtons()
    }

    override fun getButtonPositions(): List<Int> {
        return listOf(

        )
    }
}