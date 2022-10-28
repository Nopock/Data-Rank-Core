package org.hyrical.data.rank.repository

import org.hyrical.data.rank.Rank
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface RankRepository : ReactiveMongoRepository<Rank, String>