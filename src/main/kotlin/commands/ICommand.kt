package org.hyrical.data.commands

import co.aikar.commands.BaseCommand
import org.hyrical.data.Data
import org.hyrical.data.log
import org.springframework.stereotype.Component

abstract class ICommand : BaseCommand() {

    init {
        log("Registering command: ${this.javaClass.simpleName}")
        Data.instance.manager.registerCommand(this)
    }
}