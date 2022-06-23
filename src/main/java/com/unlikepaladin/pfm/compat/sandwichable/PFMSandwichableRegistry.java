package com.unlikepaladin.pfm.compat.sandwichable;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.compat.sandwichable.blocks.PFMToaster;
import com.unlikepaladin.pfm.compat.sandwichable.blocks.blockentities.PFMToasterBlockEntity;
import net.devtech.arrp.api.RRPCallback;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.loot.JCondition;
import net.devtech.arrp.json.recipe.*;
import net.devtech.arrp.json.tags.JTag;
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

import static com.unlikepaladin.pfm.PaladinFurnitureMod.MOD_ID;
import static net.devtech.arrp.api.RuntimeResourcePack.id;
import static net.devtech.arrp.json.loot.JLootTable.*;

public class PFMSandwichableRegistry {
    public static BlockEntityType<PFMToasterBlockEntity> IRON_TOASTER_BLOCKENTITY;
    public static final RuntimeResourcePack SANDWICHABLE_RESOURCE_PACK = RuntimeResourcePack.create("pfm:sandiwchable_data");

    public static void registerFurniture(String blockName, Block block, Boolean registerItem) {
        Registry.register(Registry.BLOCK, new Identifier(MOD_ID, blockName),  block);
        if (registerItem) {
            Registry.register(Registry.ITEM, new Identifier(MOD_ID, blockName), new BlockItem(block, new FabricItemSettings().group(PaladinFurnitureMod.FURNITURE_GROUP)));
        }
    }
    //protected static JTag iron_toaster_tag = JTag.tag();
    public static final PFMToaster IRON_TOASTER = new PFMToaster(FabricBlockSettings.copy(Blocks.STONECUTTER).requiresTool());
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
                                        .condition(new JCondition("minecraft:survives_explosion"))
                        )

            );
            pack.addTag(id("minecraft:mineable/pickaxe"), JTag.tag().add(id("iron_toaster")));
            pack.addRecipe(id("pfm:iron_toaster"), JRecipe.shaped(JPattern.pattern(" L ","IRI", "III"), JKeys.keys().key("I", JIngredient.ingredient().item(Items.IRON_INGOT)).key("L", JIngredient.ingredient().item(Items.LEVER)).key("R", JIngredient.ingredient().item(Items.REDSTONE)), JResult.item(PFMSandwichableRegistry.IRON_TOASTER.asItem())));

            PaladinFurnitureMod.GENERAL_LOGGER.info("wtf is this loading even?");
            RRPCallback.BEFORE_VANILLA.register(aaaaa -> {
                pack.dump();
                aaaaa.add(pack);
            });
    }


    //IGNORE ME PLS
   /* private static byte[] serialize(Object object) {
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
    }*/
}


