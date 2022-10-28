package org.hyrical.data.profile.service

import org.hyrical.data.cache.CacheManager
import org.hyrical.data.grant.repository.GrantRepository
import org.hyrical.data.grant.service.GrantService
import org.hyrical.data.profile.impl.CachedProfile
import org.hyrical.data.profile.repository.ProfileRepository
import org.hyrical.data.rank.Rank
import org.hyrical.data.rank.repository.RankRepository
import org.hyrical.data.rank.service.RankService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ProfileService @Autowired constructor(val profileRepository: ProfileRepository, val cacheManager: CacheManager, val grantService: GrantService, val grantRepository: GrantRepository, val rankService: RankService) {

    fun search(uuid: UUID): CachedProfile? {
        return if (cacheManager.isCached(uuid)) {
            cacheManager.cache[uuid.toString()]!!
        } else {
            if (profileRepository.existsById(uuid.toString()).block()!!) {
                cacheManager.cache(profileRepository.findById(uuid.toString()).block()!!)
            } else {
                null
            }
        }
    }

    fun exists(uuid: UUID): Boolean {
        return if (cacheManager.isCached(uuid)) {
            true
        } else {
            profileRepository.existsById(uuid.toString()).block()!!
        }
    }

    fun getHighestRank(profile: CachedProfile): Rank? {
        return rankService.search(profile.grants.filter { it.active }
            .maxByOrNull { rankService.search(it.rankId)!!.weight }!!.rankId)
    }

    fun getActiveRanks(cachedProfile: CachedProfile): MutableList<Rank> {
        return cachedProfile.grants.map {
            rankService.search(it.rankId)!!
        }.toMutableList()
    }

    fun collectPermissions(uuid: UUID): HashMap<String, Boolean> {
        val perms = mutableListOf<String>()

        val profile = search(uuid)!!

        val ranks = mutableListOf<String>()

        getActiveRanks(profile).forEach {
            ranks.addAll(it.parents)
            ranks.add(it.id)
        }

        ranks.forEach {
            val rank = rankService.search(it)!!

            perms.addAll(rank.permissions)
        }

        val returned = hashMapOf<String, Boolean>()

        perms.forEach {
            returned[it] = true
        }

        return returned
    }

    fun isStaff(cachedProfile: CachedProfile): Boolean {
        return this.getActiveRanks(cachedProfile).any { it.staff }
    }
}