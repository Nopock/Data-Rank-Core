package org.hyrical.data.util

import co.aikar.commands.BukkitCommandExecutionContext
import co.aikar.commands.contexts.ContextResolver
import org.bukkit.ChatColor

class Duration(private var time: Long) {

    fun getDuration(): Long {
        return time
    }

    fun isPermanent(): Boolean {
        return time == Long.MAX_VALUE
    }

    companion object {
        @JvmStatic
        val NOT_PROVIDED = Duration(-1L)

        @JvmStatic
        fun parse(input: String): Duration {
            if (input == "NOT_PROVIDED") {
                return NOT_PROVIDED
            }

            if (input.equals("perm", ignoreCase = true) || input.equals("permanent", ignoreCase = true)) {
                return Duration(Long.MAX_VALUE)
            }

            return Duration(
                DateUtil.parseDuration(
                    input
                )
            )
        }
    }

    class DurationParameterType : ContextResolver<Duration, BukkitCommandExecutionContext> {
        override fun getContext(c: BukkitCommandExecutionContext?): Duration? {
            return try {
                parse(input = c!!.popFirstArg())
            } catch (e: Exception) {
                c!!.player.sendMessage("${ChatColor.RED}Invalid duration.")
                null
            }
        }
    }

}