package org.hyrical.data.profile

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Contextual
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document
open class Profile(
    @Id val id: UUID,
    var username: String,
    var registeredAt: Long = System.currentTimeMillis(),
    var lastSeen: Long = System.currentTimeMillis(),
    var isOnline: Boolean = true
) {
    companion object {
        fun create(uuid: UUID, username: String): Profile {
            return Profile(uuid, username)
        }
    }
}