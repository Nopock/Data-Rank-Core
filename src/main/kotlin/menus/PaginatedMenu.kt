package org.hyrical.data.menus

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import kotlin.math.log
import kotlin.math.roundToInt

abstract class PaginatedMenu(title: (Player) -> String, val size: Int) : Menu(title, size) {

    private var currentPage = 1
    private var maxPages = 1

    // TODO: Maybe we can cache buttons so we don't end up fetching the buttons so much
    abstract fun getPaginatedButtons(): List<Button>

    override fun getButtons(player: Player): Map<Int, Button> {

        val time = System.currentTimeMillis()

        val buttons = mutableMapOf<Int, Button>()

        Bukkit.broadcastMessage("Current page: $currentPage")

        buttons[getPageButtonPositions().first] = getPreviousPageButton(player)
        buttons[getPageButtonPositions().second] = getNextPageButton(player)

        val paginatedButtons = getPaginatedButtons()

        var current = getButtonsStartAt()

        maxPages = (paginatedButtons.size / (size - 2)).toDouble().let {
            if (it % 1 == 0.0) it.toInt() else it.roundToInt() + 1
        }

        Bukkit.broadcastMessage("Max pages: $maxPages")

        for ((slot, button) in getGlobalButtons()) {
            buttons[slot] = button
        }

        var i = 0

        for (button in paginatedButtons) {
            if (current >= size) return buttons


            // TODO: Make system to filter pages
            val minIndex = ((currentPage - 1) * getButtonsPerPage())
            val maxIndex = (currentPage * getButtonsPerPage())


            if (i !in minIndex until maxIndex) {
                i++
                continue
            }

            if (getButtonPositions().isNotEmpty() && !getButtonPositions().contains(current)) {
                // TODO: Fix this so it just goes to the next index instead of ignoring button
                continue
            }

            buttons[current] = button

            current++
            i++
        }

        return buttons.also {
            Bukkit.broadcastMessage("Took ${System.currentTimeMillis() - time}ms to get buttons")
        }
    }

    open fun getButtonPositions(): List<Int> {
        return emptyList()
    }

    open fun getNextPageButton(player: Player): Button {
        return Button.of(ItemBuilder(TexturedButton.PAGINATED_NEXT_PAGE.construct()) {
            name("&eNext Page")
            amount(if (currentPage >= 64) 64 else currentPage + 1)
            lore("&7Click to navigate to the next page!")
        }).apply {
            action = { _, _ ->
                Bukkit.broadcastMessage("Amount: $currentPage")
                nextPage(player)
            }
        }
    }

    open fun getPreviousPageButton(player: Player): Button {
        return Button.of(
            ItemBuilder(TexturedButton.PAGINATED_PREVIOUS_PAGE.construct()) {
                name("&ePrevious Page")
                amount(if (currentPage <= 1) 1 else currentPage - 1)
                lore("&7Click to navigate to the previous page!")
            }
        ).apply {
            action = { _, _ ->
                previousPage(player)
            }
        }
    }

    open fun getPageButtonPositions(): Pair<Int, Int> {
        return Pair(0, 8)
    }

    open fun getButtonsStartAt(): Int {
        return 9
    }

    open fun getButtonsPerPage(): Int {
        return 18
    }

    fun nextPage(player: Player) {
        if (currentPage + 1 == maxPages)  {
            playSound(player, MenuSound.FAIL)
            return
        }

        currentPage++
        updateItems(getButtons(player), player, clear = true)
        playSound(player, MenuSound.SUCCESS)
    }

    fun previousPage(player: Player) {
        if (currentPage - 1 < 1)  {
            playSound(player, MenuSound.FAIL)
            return
        }

        currentPage--
        updateItems(getButtons(player), player, clear = true)
        playSound(player, MenuSound.SUCCESS)
    }

    open fun getGlobalButtons(): Map<Int, Button> {
        return mutableMapOf<Int, Button>()
    }
}