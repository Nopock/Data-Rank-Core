package org.hyrical.data.rank.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.CommandHelp
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.HelpCommand
import co.aikar.commands.annotation.Subcommand
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.hyrical.data.Data
import org.hyrical.data.profile.service.ProfileService
import org.hyrical.data.rank.Rank
import org.hyrical.data.rank.menus.RankListMenu
import org.hyrical.data.rank.repository.RankRepository
import org.hyrical.data.rank.service.RankService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.DependsOn
import org.springframework.stereotype.Component


@Component
@DependsOn("rankResolver")
@CommandAlias("rank")
@CommandPermission("data.ranks.admin")
class RankEditCommands @Autowired constructor(val rankRepository: RankRepository, val profileService: ProfileService, val rankService: RankService) : BaseCommand() {
// note to self to update all update all people will dat ranks PERMS!

    init {
        Data.instance.manager.registerCommand(this)
    }

    @HelpCommand
    fun onHelp(sender: CommandSender, help: CommandHelp) {
        help.showHelp()
    }

    @Subcommand("list")
    fun onRankList(player: Player) {
        val sortedRanks = rankService.all(sorted = true)

        player.sendMessage("§a§lRanks:")
        sortedRanks.forEach {
            player.sendMessage("§7- §f${it.name} §7(§f${it.weight}§7)")
        }

        RankListMenu(rankService).open(player)
    }

    @Subcommand("create")
    fun onRankCreate(player: Player, name: String) {
        if (rankService.search(name) != null) {
            player.sendMessage("§cA rank with that name already exists!")
            return
        }

        val rank = Rank(
            name.lowercase(),
            name
        )

        rankRepository.save(rank).subscribe {
            rankService.save(it)
            player.sendMessage("§a§lRank created!")
        }
    }

    @Subcommand("delete")
    fun onRankDelete(player: Player, rank: Rank) {
        rankRepository.deleteById(rank.id).subscribe {
            rankService.delete(rank)
            player.sendMessage("§a§lRank deleted!")

            // Remove ppl's grants XD
        }
    }

    @Subcommand("setweight")
    fun onRankSetWeight(player: Player, rank: Rank, weight: Int) {
        rank.weight = weight
        rankRepository.save(rank).subscribe {
            rankService.save(it)
            player.sendMessage("§a§lRank weight updated!")
        }
    }

    @Subcommand("setprefix")
    fun onRankSetPrefix(player: Player, rank: Rank, prefix: String) {
        rank.prefix = prefix
        rankRepository.save(rank).subscribe {
            rankService.save(it)
            player.sendMessage("§a§lRank prefix updated!")
        }
    }

    @Subcommand("setstaff")
    fun onRankSetStaff(player: Player, rank: Rank, staff: Boolean) {
        rank.staff = staff
        rankRepository.save(rank).subscribe {
            rankService.save(it)
            player.sendMessage("§a§lRank staff updated!")
        }
    }

    @Subcommand("setdefault")
    fun onRankSetDefault(player: Player, rank: Rank, default: Boolean) {
        rank.default = default
        rankRepository.save(rank).subscribe {
            rankService.save(it)
            player.sendMessage("§a§lRank default updated!")
        }
    }

    @Subcommand("setdisplayname")
    fun onRankSetDisplayName(player: Player, rank: Rank, displayName: String) {
        rank.displayName = displayName
        rankRepository.save(rank).subscribe {
            rankService.save(it)
            player.sendMessage("§a§lRank display name updated!")
        }
    }

    @Subcommand("addperm|addpermission")
    fun onRankAddPerm(player: Player, rank: Rank, permission: String) {

        // refresh perms
        rank.permissions.add(permission)
        rankRepository.save(rank).subscribe {
            rankService.save(it)
            player.sendMessage("§a§lRank permission added!")
        }
    }

    @Subcommand("removeperm|removepermission")
    fun onRankRemovePerm(player: Player, rank: Rank, permission: String) {

        // refresh perms
        rank.permissions.remove(permission)
        rankRepository.save(rank).subscribe {
            rankService.save(it)
            player.sendMessage("§a§lRank permission removed!")
        }
    }

    @Subcommand("addparent|addinheritance")
    fun onRankAddParent(player: Player, rank: Rank, parent: Rank) {
        // refresh perms
        rank.parents.add(parent.id)
        rankRepository.save(rank).subscribe {
            rankService.save(it)
            player.sendMessage("§a§lRank parent added!")
        }
    }

    @Subcommand("removeparent|removeinheritance")
    fun onRankRemoveParent(player: Player, rank: Rank, parent: Rank) {
        // refresh perms
        rank.parents.remove(parent.id)
        rankRepository.save(rank).subscribe {
            rankService.save(it)
            player.sendMessage("§a§lRank parent removed!")
        }
    }
}