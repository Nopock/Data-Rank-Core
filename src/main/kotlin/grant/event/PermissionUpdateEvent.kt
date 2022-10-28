package org.hyrical.data.grant.event

import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import java.util.UUID

class PermissionUpdateEvent(val uuid: UUID) : Event() {

    private val handler = HandlerList()

    override fun getHandlers(): HandlerList {
        return handler
    }
}