package org.hyrical.data.rank.service

import org.hyrical.data.log
import org.hyrical.data.rank.Rank
import org.hyrical.data.rank.repository.RankRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RankService @Autowired constructor(private final val rankRepository: RankRepository) {

    private val ranks = mutableMapOf<String, Rank>()

    init {
        rankRepository.findAll().subscribe {
            ranks[it.id] = it
        }

        log("The rank cache was initialized with a size of ${ranks.size}")

        if (!ranks.containsKey("default")) {
            Rank(
               "default",
               "Default",
                default = true
            ).also {
                ranks["default"] = it
                rankRepository.save(it).subscribe {
                    log("The default rank was created")
                }
            }
        }
    }

    fun search(name: String): Rank? {
        return ranks[name]
    }

    fun all(sorted: Boolean = false): List<Rank> {
        return if (!sorted) {
            ranks.values.toList()
        } else {
            ranks.values.sortedByDescending { it.weight }
        }
    }

    fun save(rank: Rank) {
        ranks[rank.id] = rank
    }

    fun delete(rank: Rank) {
        ranks.remove(rank.id)
    }
}