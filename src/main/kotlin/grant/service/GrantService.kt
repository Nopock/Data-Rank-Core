package org.hyrical.data.grant.service

import org.hyrical.data.CONSOLE_UUID
import org.hyrical.data.grant.Grant
import org.hyrical.data.grant.repository.GrantRepository
import org.hyrical.data.profile.Profile
import org.hyrical.data.rank.service.RankService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.UUID

@Service("grantService")
class GrantService @Autowired constructor(val grantRepository: GrantRepository, val rankService: RankService) {

    fun grantDefault(profile: Profile) : Grant {
        Grant(
            UUID.randomUUID(),
            profile.id,
            CONSOLE_UUID,
            System.currentTimeMillis(),
            "Default Grant",
            -1L,
            rankId = "default"
        ).also {
            grantRepository.save(it).block()
            return it
        }
    }
}