package com.unlikepaladin.pfm.compat.fabric.sandwichable;

import com.unlikepaladin.pfm.compat.fabric.sandwichable.blocks.PFMToasterBlock;
import com.unlikepaladin.pfm.compat.fabric.sandwichable.blocks.blockentities.PFMToasterBlockEntity;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.compat.fabric.arrp.JFurnitureRecipe;
import net.devtech.arrp.api.RRPCallback;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.impl.RuntimeResourcePackImpl;
import net.devtech.arrp.json.recipe.JIngredient;
import net.devtech.arrp.json.recipe.JKeys;
import net.devtech.arrp.json.recipe.JPattern;
import net.devtech.arrp.json.recipe.JResult;
import net.devtech.arrp.json.tags.JTag;
import net.devtech.arrp.util.UnsafeByteArrayOutputStream;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import static net.devtech.arrp.api.RuntimeResourcePack.id;
import static net.devtech.arrp.json.loot.JLootTable.*;

public class PFMSandwichableRegistry {
    public static BlockEntityType<PFMToasterBlockEntity> IRON_TOASTER_BLOCKENTITY;

    public static final RuntimeResourcePack SANDWICHABLE_RESOURCE_PACK = RuntimeResourcePack.create("pfm:sandiwchable_data");

    public static void registerFurniture(String blockName, Block block, Boolean registerItem) {
        Registry.register(Registry.BLOCK, new Identifier(PaladinFurnitureMod.MOD_ID, blockName),  block);
        if (registerItem) {
            Registry.register(Registry.ITEM, new Identifier(PaladinFurnitureMod.MOD_ID, blockName), new BlockItem(block, new FabricItemSettings().group(PaladinFurnitureMod.FURNITURE_GROUP)));
        }
    }
    //protected static JTag iron_toaster_tag = JTag.tag();
    public static final PFMToasterBlock IRON_TOASTER = new PFMToasterBlock(FabricBlockSettings.copy(Blocks.STONECUTTER).requiresTool());
    public static void register() {
            RuntimeResourcePack pack = SANDWICHABLE_RESOURCE_PACK;
            registerFurniture("iron_toaster", IRON_TOASTER, true);
            IRON_TOASTER_BLOCKENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier("pfm","iron_toaster_ent"), FabricBlockEntityTypeBuilder.create(PFMToasterBlockEntity::new, new Block[]{IRON_TOASTER}).build(null));

            pack.addLootTable(id("pfm:blocks/iron_toaster"),
                loot("minecraft:block")
                        .pool(pool()
                                .rolls(1)
                                .entry(entry()
                                        .type("minecraft:item")
                                        .name("pfm:iron_toaster"))
                        )

            );
            pack.addTag(id("minecraft:blocks/mineable/pickaxe"), JTag.tag().add(id("pfm:iron_toaster")));
            JFurnitureRecipe recipe =  new JFurnitureRecipe(JResult.item(PFMSandwichableRegistry.IRON_TOASTER.asItem()), JPattern.pattern(" L ","IRI", "III"), JKeys.keys().key("I", JIngredient.ingredient().item(Items.IRON_INGOT)).key("L", JIngredient.ingredient().item(Items.LEVER)).key("R", JIngredient.ingredient().item(Items.REDSTONE)));
            pack.addData(fix(id("pfm","iron_toaster"), "recipes", "json"), serialize(recipe));
            RRPCallback.BEFORE_VANILLA.register(a -> {
                a.add(pack);
            });
    }


    //IGNORE ME PLS
   private static byte[] serialize(Object object) {
        UnsafeByteArrayOutputStream ubaos = new UnsafeByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(ubaos, StandardCharsets.UTF_8);
        RuntimeResourcePackImpl.GSON.toJson(object, writer);
        try {
            writer.close();
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
        return ubaos.getBytes();
    }

    private static Identifier fix(Identifier identifier, String prefix, String append) {
        return new Identifier(identifier.getNamespace(), prefix + '/' + identifier.getPath() + '.' + append);
    }
}


