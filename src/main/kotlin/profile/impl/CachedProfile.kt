package org.hyrical.data.profile.impl

import kotlinx.serialization.Contextual
import org.hyrical.data.grant.Grant
import org.hyrical.data.profile.Profile
import org.hyrical.data.punishment.Punishment
import java.util.UUID

data class CachedProfile(
    val uuid: @Contextual UUID,
    var registerAt: Long,
    var name: String,
    var grants: MutableList<Grant> = mutableListOf(),
    var punishments: MutableList<Punishment> = mutableListOf()
) : Profile(uuid, registeredAt = registerAt, username = name) {

    @Transient
    var cacheExpiry: Long? = null

    fun shouldRecache(): Boolean {
        return cacheExpiry!! < System.currentTimeMillis()
    }
}