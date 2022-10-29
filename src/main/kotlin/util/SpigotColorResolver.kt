package org.hyrical.data.util

import co.aikar.commands.BukkitCommandExecutionContext
import co.aikar.commands.InvalidCommandArgument
import co.aikar.commands.contexts.ContextResolver
import org.bukkit.World.Spigot
import org.hyrical.data.Data
import org.hyrical.data.translate
import org.springframework.stereotype.Component

@Component("colorResolver")
class SpigotColorResolver : ContextResolver<SpigotColor, BukkitCommandExecutionContext> {

    init {
        Data.instance.manager.commandContexts.registerContext(SpigotColor::class.java, this)
    }

    override fun getContext(c: BukkitCommandExecutionContext?): SpigotColor {
        if (ColorUtils.COLOR_TO_CHAT_COLOR.containsKey(c!!.popFirstArg())) {
            return SpigotColor(c.popFirstArg())
        }

        throw InvalidCommandArgument(translate("&cInvalid color!"))
    }

}