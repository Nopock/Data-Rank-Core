package org.hyrical.data.grant.repository

import org.hyrical.data.grant.Grant
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.util.UUID

@Repository
interface GrantRepository : ReactiveMongoRepository<Grant, String> {
    fun findByUserId(userId: UUID): Flux<Grant>

    fun findByUserIdAndActive(userId: UUID, active: Boolean): Flux<Grant>

    fun findByIssuedBy(issuedBy: UUID): Flux<Grant>

    fun findByRemovedAt(removedAt: Long?): Flux<Grant>
}