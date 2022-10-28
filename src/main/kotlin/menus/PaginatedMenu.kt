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
        val buttons = mutableMapOf<Int, Button>()

        org.hyrical.data.log("Getting buttons for page $currentPage")

        buttons[getPageButtonPositions().first] = getPreviousPageButton(player)
        buttons[getPageButtonPositions().second] = getNextPageButton(player)

        val paginatedButtons = getPaginatedButtons()

        var current = getButtonsStartAt()

        maxPages = (getButtonsPerPage().toDouble() / paginatedButtons.size.toDouble()).roundToInt() + 1

        org.hyrical.data.log("Max pages: $maxPages")

        for ((slot, button) in getGlobalButtons()) {
            buttons[slot] = button
        }

        var i = 0
        for (button in paginatedButtons) {
            org.hyrical.data.log("Adding button $i to slot $current")
            if (current >= size) return buttons.also {
                org.hyrical.data.log("Buttons: ${buttons.size} returned on line 39")
            }


            // TODO: Make system to filter pages
            val minIndex = ((currentPage - 1) * getButtonsPerPage().toDouble()).toInt()
            val maxIndex = (currentPage * getButtonsPerPage().toDouble()).toInt()

            if (i !in minIndex until maxIndex) continue

            if (getButtonPositions().isNotEmpty() && !getButtonPositions().contains(current)) {

                // TODO: Fix this so it just goes to the next index instead of ignoring button
                Bukkit.broadcastMessage("Skipping button $i line 49")
                continue
            }

            buttons[current] = button

            org.hyrical.data.log("Added button $i to slot $current")

            current++
            i++
        }

        for (button in buttons.values) {
            Bukkit.broadcastMessage("Button: ${button.item.invoke(player).itemMeta.displayName}")
        }

        return buttons
    }

    open fun getButtonPositions(): List<Int> {
        return emptyList()
    }

    open fun getNextPageButton(player: Player): Button {
        return Button.of(ItemBuilder(TexturedButton.PAGINATED_NEXT_PAGE.construct()) {
            name("&eNext Page")
            amount(currentPage)
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
            amount(currentPage)
            lore("&7Click to navigate to the previous page!")
        }
        ).apply {
            action = { _, _ ->
                Bukkit.broadcastMessage("Amount: $currentPage")
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
        if (getButtonPositions().isEmpty()) {
            return size - 9
        }

        return getButtonPositions().size
    }

    fun nextPage(player: Player) {
        if (currentPage + 1 == maxPages)  {
            //playSound(MenuSound.ERROR)
            return
        }

        currentPage++
        updateItems(getButtons(player), player, clear = true)
    }

    fun previousPage(player: Player) {
        if (currentPage - 1 < 1)  {
            //playSound(MenuSound.ERROR)
            return
        }

        currentPage--
        updateItems(getButtons(player), player, clear = true)
    }

    open fun getGlobalButtons(): Map<Int, Button> {
        return mutableMapOf<Int, Button>()
    }
}