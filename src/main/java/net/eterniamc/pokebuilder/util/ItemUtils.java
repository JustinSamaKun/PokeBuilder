package net.eterniamc.pokebuilder.util;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

import java.util.List;

public class ItemUtils {

    public static void setItemLore(ItemStack stack, List<String> lore) {
        NBTTagCompound display = stack.getOrCreateSubCompound("display");
        NBTTagList list = new NBTTagList();
        for (String s : lore) {
            list.appendTag(new NBTTagString(TextUtils.text(s)));
        }
        display.setTag("Lore", list);
    }

    public static void setItemLore(ItemStack stack, String... lore) {
        NBTTagCompound display = stack.getOrCreateSubCompound("display");
        NBTTagList list = new NBTTagList();
        for (String s : lore) {
            list.appendTag(new NBTTagString(TextUtils.text(s)));
        }
        display.setTag("Lore", list);
    }

    public static List<String> splitToLore(String text) {
        return Lists.newArrayList(text.split("\n"));
    }

    public static void setDisplayName(ItemStack stack, String name) {
        NBTTagCompound display = stack.getOrCreateSubCompound("display");
        display.setTag("Name", new NBTTagString(TextUtils.text("&r" + name)));
    }
}
