package org.hyrical.data.rank

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Rank (
    @Id var id: String,
    var name: String,
    var displayName: String = name,
    var weight: Int = 0,
    var permissions: MutableList<String> = mutableListOf(),
    var parents: MutableList<String> = mutableListOf(),

    var prefix: String = "",
    var staff: Boolean = false,
    var default: Boolean = false,
)