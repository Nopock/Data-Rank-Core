package org.hyrical.data.cache.event

import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.hyrical.data.profile.impl.CachedProfile

class CacheExpireEvent(val cachedProfile: CachedProfile) : Event() {

    private val handlersList = HandlerList()

    override fun getHandlers(): HandlerList {
        return handlersList
    }
}