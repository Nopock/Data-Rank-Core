package org.hyrical.data.cache.expiry

import org.hyrical.data.cache.CacheManager
import org.hyrical.data.log
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class CacheExpiry @Autowired constructor(val cacheManager: CacheManager) : Thread("Profile-Cache-Expiry") {

    init {
        log("Initializing cache expiry...")
    }

    override fun run() {
        while (true) {
            sleep(1000)
            tick()
        }
    }

    private fun tick() {
        for (cachedProfile in cacheManager.cache.values) {
            if (!cachedProfile.isOnline) {
                cacheManager.uncache(cachedProfile.uuid)
                continue
            }

            if (cachedProfile.shouldRecache()) {
                cacheManager.recache(cachedProfile)
            }
        }
    }
}