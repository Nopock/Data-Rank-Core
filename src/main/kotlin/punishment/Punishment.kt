package org.hyrical.data.punishment

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document
data class Punishment(
    @Id val id: UUID,
    val userId: UUID,
    val issuedBy: UUID,
    val issuedAt: Long = System.currentTimeMillis(),
    val issuedReason: String,
    val duration: Long,
    var removedBy: UUID? = null,
    var removedAt: Long? = null,
    var removedReason: String? = null,
    val punishmentType: PunishmentType
) {
    val active: Boolean get() = if (duration == -1L && removedAt == null) true else System.currentTimeMillis() < issuedAt + duration
}