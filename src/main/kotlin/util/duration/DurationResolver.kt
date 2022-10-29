package org.hyrical.data.util.duration

import co.aikar.commands.BukkitCommandExecutionContext
import co.aikar.commands.InvalidCommandArgument
import co.aikar.commands.contexts.ContextResolver
import org.hyrical.data.Data
import org.springframework.stereotype.Component
import kotlin.time.Duration


@Component("durationResolver")
class DurationResolver : ContextResolver<Duration, BukkitCommandExecutionContext> {

    init {
        Data.instance.manager.commandContexts.registerContext(Duration::class.java, this)
    }

    override fun getContext(c: BukkitCommandExecutionContext?): Duration? {
        val arg = c?.popFirstArg() ?: return null

        return try {
            Duration.parse(arg)
        } catch (e: IllegalArgumentException) {
            throw InvalidCommandArgument("Invalid duration.")
        }
    }
}