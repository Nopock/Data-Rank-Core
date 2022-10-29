package org.hyrical.data.cache

import org.bukkit.Bukkit
import org.hyrical.data.cache.event.CacheExpireEvent
import org.hyrical.data.cache.expiry.CacheExpiry
import org.hyrical.data.grant.repository.GrantRepository
import org.hyrical.data.grant.service.GrantService
import org.hyrical.data.log
import org.hyrical.data.profile.Profile
import org.hyrical.data.profile.impl.CachedProfile
import org.hyrical.data.profile.repository.ProfileRepository
import org.hyrical.data.punishment.repository.PunishmentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.DependsOn
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.util.UUID
import java.util.concurrent.ScheduledExecutorService

@Service
@DependsOn("grantService")
class CacheManager {

    @Autowired lateinit var profileRepository: ProfileRepository
    @Autowired lateinit var grantRepository: GrantRepository
    @Autowired lateinit var punishmentRepository: PunishmentRepository
    @Autowired lateinit var grantService: GrantService

    init {
        log("Initializing cache manager...")
    }

    val cache = mutableMapOf<String, CachedProfile>()

    fun cache(profile: Profile): CachedProfile {
        return CachedProfile(profile.id, profile.registeredAt, name = profile.username).apply {
            log("Caching profile ${profile.username}...")
            cacheExpiry = System.currentTimeMillis() + 1000 * 30

            punishments = punishmentRepository.findByUserId(this.id).collectList().block().orEmpty().toMutableList()
            grants = grantRepository.findByUserId(this.uuid).collectList().block().orEmpty().toMutableList()
        }.also {
            cache[profile.id.toString()] = it
        }
    }

    fun cache(uuid: UUID, username: String): CachedProfile {
        val profile = profileRepository.findById(uuid.toString()).block()

        return if (profileRepository.existsById(uuid.toString()).block() == true) {
            cache(profile!!)
        } else {
            cache(
                Profile.create(uuid, username).also {
                    profileRepository.save(it).subscribe {
                        log("Saved profile ${it.username} to database.")
                    }

                    // TODO: Fix this being granted if they aren't in the cache i believe
                    grantService.grantDefault(it)
                }
            )
        }
    }

    fun uncache(uuid: UUID) {
        log("Uncaching profile ${uuid}...")
        Bukkit.getPluginManager().callEvent(CacheExpireEvent(cache[uuid.toString()]!!))
        cache.remove(uuid.toString())
    }

    fun recache(cachedProfile: CachedProfile) {
        log("Recaching profile ${cachedProfile.uuid}...")
        profileRepository.findById(cachedProfile.id.toString()).subscribe {
            cache(it)
        }
    }

    fun isCached(uuid: UUID): Boolean {
        return cache.containsKey(uuid.toString())
    }
}