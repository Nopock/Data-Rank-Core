package org.hyrical.data

import co.aikar.commands.BukkitCommandManager
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.hyrical.data.cache.CacheManager
import org.hyrical.data.cache.listener.CacheListener
import org.hyrical.data.menus.MenuHandler
import org.springframework.beans.factory.getBeansWithAnnotation
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.core.io.DefaultResourceLoader
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories
import java.util.UUID


class Data : JavaPlugin() {

    companion object {
        lateinit var instance: Data
    }

    var context: ConfigurableApplicationContext? = null
    val manager: BukkitCommandManager by lazy { BukkitCommandManager(this) }

    override fun onEnable() {
        saveDefaultConfig()

        instance = this

        Bukkit.getScheduler().runTaskAsynchronously(this) {
            SpringApplicationBuilder(DataApplication::class.java)
                .resourceLoader(DefaultResourceLoader(this.classLoader))
                .run().also { context = it }
        }

        manager.enableUnstableAPI("help")

        MenuHandler.setup(this)
    }

    override fun onDisable() {
        context!!.stop()
    }
}

@EnableReactiveMongoRepositories
@SpringBootApplication(scanBasePackages = ["org.hyrical.data"], exclude = [org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration::class])
class DataApplication

fun log(message: String) {
    Bukkit.getLogger().log(java.util.logging.Level.INFO, message)
}

val CONSOLE_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000")