package org.hyrical.data.grant.expiry

import org.hyrical.data.grant.repository.GrantRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.Timer
import kotlin.concurrent.schedule

@Component
class GrantExpiry @Autowired constructor(private val grantRepository: GrantRepository) {

    init {
        val timer = Timer()

        grantRepository.findByRemovedAt(null).subscribe {
            timer.schedule(it.issuedAt + it.duration + System.currentTimeMillis()) {
                grantRepository.save(it).block()

                TODO("Update perms and what not !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
            }
        }
    }
}