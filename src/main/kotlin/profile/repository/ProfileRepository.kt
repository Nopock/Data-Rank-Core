package org.hyrical.data.profile.repository

import org.hyrical.data.profile.Profile
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.util.*

@Repository
interface ProfileRepository : ReactiveMongoRepository<Profile, String> {
    
    fun findByUsername(username: String): Mono<Profile>

    fun existsByUsername(username: String): Mono<Boolean>
}