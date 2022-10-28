package org.hyrical.data.rank.service.resolver

import co.aikar.commands.BukkitCommandExecutionContext
import co.aikar.commands.InvalidCommandArgument
import co.aikar.commands.contexts.ContextResolver
import org.bukkit.ChatColor
import org.hyrical.data.Data
import org.hyrical.data.rank.Rank
import org.hyrical.data.rank.service.RankService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component("rankResolver")
class RankResolver @Autowired constructor(val rankService: RankService) : ContextResolver<Rank, BukkitCommandExecutionContext> {

    init {
        Data.instance.manager.commandContexts.registerContext(Rank::class.java, this)
    }

    override fun getContext(c: BukkitCommandExecutionContext?): Rank {
        return rankService.search(c?.popFirstArg()!!) ?: throw InvalidCommandArgument(ChatColor.RED.toString() + "The rank you specified was not found.")
    }
}