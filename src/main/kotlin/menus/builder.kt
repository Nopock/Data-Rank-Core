package org.hyrical.data.menus

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta

inline fun ItemBuilder(itemStack: ItemStack, builder: ItemStackBuilder.() -> Unit): ItemStack = ItemStackBuilder(itemStack = itemStack).apply(builder).build()

inline fun ItemBuilder(builder: ItemStackBuilder.() -> Unit): ItemStack = ItemStackBuilder().apply(builder).build()

class ItemStackBuilder(var itemStack: ItemStack = ItemStack(Material.AIR)) {

    fun build(): ItemStack = itemStack

    fun type(material: Material) = apply { itemStack.type = material }

    fun amount(amount: Int) = apply { itemStack.amount = amount }

    fun name(name: String) = apply {
        val meta = itemStack.itemMeta ?: Bukkit.getItemFactory().getItemMeta(itemStack.type);
        meta.displayName = translate(name)
        itemStack.itemMeta = meta
    }

    fun lore(lore: List<String>) = apply {
        val meta = itemStack.itemMeta ?: Bukkit.getItemFactory().getItemMeta(itemStack.type);
        if (meta.lore == null) {
            meta.lore = lore.map { translate(it) }
        } else {
            meta.lore!!.addAll(lore.map { translate(it) })
        }
        itemStack.itemMeta = meta
    }

    fun lore(vararg lore: String) = apply {
        val meta = itemStack.itemMeta ?: Bukkit.getItemFactory().getItemMeta(itemStack.type);
        if (meta.lore == null) {
            meta.lore = lore.map { translate(it) }
        } else {
            meta.lore!!.addAll(lore.map { translate(it) })
        }
        itemStack.itemMeta = meta
    }

    fun enchantment(enchantment: Enchantment, level: Int) = apply {
        val meta = itemStack.itemMeta ?: Bukkit.getItemFactory().getItemMeta(itemStack.type);
        meta.addEnchant(enchantment, level, true)
        itemStack.itemMeta = meta
    }

    fun enchantments(enchantments: Map<Enchantment, Int>) = apply {
        val meta = itemStack.itemMeta ?: Bukkit.getItemFactory().getItemMeta(itemStack.type);
        enchantments.forEach { (enchantment, level) ->
            meta.addEnchant(enchantment, level, true)
        }
        itemStack.itemMeta = meta
    }

    fun itemFlag(itemFlag: ItemFlag) = apply {
        val meta = itemStack.itemMeta ?: Bukkit.getItemFactory().getItemMeta(itemStack.type);
        meta.addItemFlags(itemFlag)
        itemStack.itemMeta = meta
    }

    fun data(data: Short) = apply {
        this.itemStack.durability = data
    }

    private fun translate(s: String): String {
        return ChatColor.translateAlternateColorCodes('&', s)
    }
}
