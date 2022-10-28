package org.hyrical.data.punishment

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document
data class Punishment(
    @Id val id: UUID,
    val userId: UUID,
    val issuedBy: UUID,
    val issuedAt: Long,
    val issuedReason: String,
    val duration: Long,
    var removedBy: UUID?,
    var removedAt: Long?,
    var removedReason: String?,
    val punishmentType: PunishmentType
) {
    val active: Boolean get() = removedAt == null || removedAt!! + duration > System.currentTimeMillis()
}