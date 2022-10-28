package org.hyrical.data.grant

import org.hyrical.data.rank.Rank
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Duration
import java.util.UUID

@Document
data class Grant(
    @Id val id: UUID,
    val userId: UUID,
    val issuedBy: UUID,
    val issuedAt: Long,
    val issuedReason: String,
    val duration: Long,
    val removedBy: UUID? = null,
    val removedAt: Long? = null,
    val removedReason: String? = null,
    val rankId: String
) {
    val active: Boolean get() = removedAt == null || removedAt + duration > System.currentTimeMillis()
}