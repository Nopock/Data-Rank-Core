package org.hyrical.data.punishment

enum class PunishmentType(val punishmentMessage: String) {
    WARN("warned"),
    MUTE("muted"),
    BAN("banned"),
    BLACKLIST("blacklisted");
}