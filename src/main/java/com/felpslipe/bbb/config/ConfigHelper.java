package com.felpslipe.bbb.config;


import com.felpslipe.bbb.misc.Utils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ConfigHelper {
    public static boolean canBlock(ItemStack itemStack) {
        for(String blockable : ConfigManager.config.blockables) {
            if(blockable.startsWith("#")) {
                Identifier id = Identifier.parse(blockable.substring(1));
                TagKey<Item> tag = TagKey.create(Registries.ITEM, id);

                if(itemStack.is(tag)) return true;
            }
            else if(blockable.startsWith("skyblock:")) {
                String id = blockable.substring(9);
                String sbitem = Utils.getSkyBlockId(itemStack);
                if (id.equalsIgnoreCase(sbitem)) return true;
            }
            else {
                Identifier id = Identifier.parse(blockable);
                Item item = BuiltInRegistries.ITEM.getValue(id);
                if(itemStack.is(item)) return true;
            }
        }
        return false;
    }
}
