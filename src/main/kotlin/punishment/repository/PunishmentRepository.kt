package org.hyrical.data.punishment.repository

import org.hyrical.data.grant.Grant
import org.hyrical.data.punishment.Punishment
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.util.*

@Repository
interface PunishmentRepository : ReactiveMongoRepository<Punishment, String> {
    fun findByUserId(userId: UUID): Flux<Punishment>

    fun findByUserIdAndActive(userId: UUID, active: Boolean): Flux<Punishment>

    fun findByIssuedBy(issuedBy: UUID): Flux<Punishment>
}