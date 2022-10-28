package org.hyrical.data.menus

import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import java.lang.reflect.Field
import java.util.*

enum class TexturedButton(val texture: String) {

    PAGINATED_NEXT_PAGE("h0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTliZjMyOTJlMTI2YTEwNWI1NGViYTcxM2FhMWIxNTJkNTQxYTFkODkzODgyOWM1NjM2NGQxNzhlZDIyYmYifXeyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZX19"),
    PAGINATED_PREVIOUS_PAGE("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmQ2OWUwNmU1ZGFkZmQ4NGU1ZjNkMWMyMTA2M2YyNTUzYjJmYTk0NWVlMWQ0ZDcxNTJmZGM1NDI1YmMxMmE5In19fQ=="),
    PAGINATED_DOWN_PAGE("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzQzNzM0NmQ4YmRhNzhkNTI1ZDE5ZjU0MGE5NWU0ZTc5ZGFlZGE3OTVjYmM1YTEzMjU2MjM2MzEyY2YifX19"),
    PAGINATED_UP_PAGE("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzA0MGZlODM2YTZjMmZiZDJjN2E5YzhlYzZiZTUxNzRmZGRmMWFjMjBmNTVlMzY2MTU2ZmE1ZjcxMmUxMCJ9fX0="),
    DISCORD("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzg3M2MxMmJmZmI1MjUxYTBiODhkNWFlNzVjNzI0N2NiMzlhNzVmZjFhODFjYmU0YzhhMzliMzExZGRlZGEifX19"),
    TWITTER("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzY4NWEwYmU3NDNlOTA2N2RlOTVjZDhjNmQxYmEyMWFiMjFkMzczNzFiM2Q1OTcyMTFiYjc1ZTQzMjc5In19fQ=="),
    MAIL("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzc4YTY2OWFkZWYxY2FkMzQ0YzYwYWM5NDYzMmQyOTVkMTM4OWFjY2VhYjI0YjVkMjA4YTllYmE4YmU0NWI3YyJ9fX0="),
    MONEY("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDdiNjljOWRmYjYxMDY3Yzk0ODRkZjdkMDNlNjNmMTc4OTVjOWNkYTMzMjVjMmM1MzRhNWMyMjM1ODU1NzYzMSJ9fX0="),
    RANK_ICON("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWU5N2Y5OWU5ZWRiODllZDdiMTMwOTBiOTE4Mzk3ZGFjNDdmYTdhNmVhMTA4MGU0MzljZDMzODBhMTYwMzFjOCJ9fX0=");


    fun construct(): ItemStack {
        val itemStack = ItemStack(Material.SKULL_ITEM).apply {
            durability = 3
        }

        val skullMeta = (itemStack.itemMeta as SkullMeta)

        if (texture.length < 16) {
            skullMeta.owner = texture
            itemStack.itemMeta = skullMeta
            return itemStack
        }

        val gameProfile = GameProfile(UUID.randomUUID(), null)

        gameProfile.properties.put("textures", Property("textures", texture))

        val field: Field?

        try {
            field = skullMeta.javaClass.getDeclaredField("profile")
            field.isAccessible = true
            field.set(skullMeta, gameProfile)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        itemStack.itemMeta = skullMeta

        return itemStack
    }

    fun constructButton(): Button {
        return Button.of(construct())
    }
}