package org.hyrical.data.menus

import org.apache.commons.io.filefilter.TrueFileFilter
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

        val buttons = mutableMapOf<Int, Button>()

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

        val time = System.currentTimeMillis()

        val minIndex = ((currentPage - 1) * getButtonsPerPage())
        val maxIndex = (currentPage * getButtonsPerPage())

        var i = 0

        val positions = getButtonPositions()
        var lastPos = positions.first()
        var lastIndex = 0

        for (button in paginatedButtons) {
            if (current >= size) return buttons

            if (lastIndex - 1 >= positions.size) return buttons


            if (i !in minIndex until maxIndex) {
                i++
                continue
            }

            if (positions.isNotEmpty()) {
                buttons[lastPos] = button
                try {
                    lastPos = positions[lastIndex + 1]
                } catch (e: IndexOutOfBoundsException) {
                    return buttons
                }
                lastIndex++
                continue
            }

            if (!buttons.containsKey(current)) {
                buttons[current] = button
            }

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