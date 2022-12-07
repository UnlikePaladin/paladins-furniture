package com.unlikepaladin.pfm.registry.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.entity.ChairEntity;
import com.unlikepaladin.pfm.registry.Entities;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.DefaultAttributeRegistry;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registry;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(modid = "pfm", bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityRegistryForge {

    @SubscribeEvent
    public static void registerEntity(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.ENTITY_TYPES, entityTypeRegisterHelper -> {
            entityTypeRegisterHelper.register(new Identifier(PaladinFurnitureMod.MOD_ID, "chair"), Entities.CHAIR);
        });
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(Entities.CHAIR, ChairEntity.createMobAttributes().build());
    }
}
