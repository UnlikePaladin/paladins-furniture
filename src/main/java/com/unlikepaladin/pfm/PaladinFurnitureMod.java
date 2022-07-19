package com.unlikepaladin.pfm;

import com.unlikepaladin.pfm.blocks.behavior.SinkBehavior;
import com.unlikepaladin.pfm.compat.sandwichable.PFMSandwichableRegistry;
import com.unlikepaladin.pfm.registry.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PaladinFurnitureMod implements ModInitializer {

	public static final String MOD_ID = "pfm";
	public static final Identifier FURNITURE_DYED_ID = new Identifier("pfm:furniture_dyed");
	public static SoundEvent FURNITURE_DYED_EVENT = new SoundEvent(FURNITURE_DYED_ID);

	public static final Logger GENERAL_LOGGER = LogManager.getLogger();

	public static final ItemGroup FURNITURE_GROUP = FabricItemGroupBuilder.build(
			new Identifier(MOD_ID, "furniture"),
			() -> new ItemStack(BlockItemRegistry.OAK_CHAIR));

	public static final ItemGroup DYE_KITS = FabricItemGroupBuilder.create(
			new Identifier(MOD_ID, "dye_kits"))
			.icon(() -> new ItemStack(BlockItemRegistry.DYE_KIT_RED))
			.appendItems(stacks -> {
		stacks.add(new ItemStack(BlockItemRegistry.DYE_KIT_RED));
		stacks.add(new ItemStack(BlockItemRegistry.DYE_KIT_ORANGE));
		stacks.add(new ItemStack(BlockItemRegistry.DYE_KIT_YELLOW));
		stacks.add(new ItemStack(BlockItemRegistry.DYE_KIT_GREEN));
		stacks.add(new ItemStack(BlockItemRegistry.DYE_KIT_LIME));
		stacks.add(new ItemStack(BlockItemRegistry.DYE_KIT_CYAN));
		stacks.add(new ItemStack(BlockItemRegistry.DYE_KIT_BLUE));
		stacks.add(new ItemStack(BlockItemRegistry.DYE_KIT_LIGHT_BLUE));
		stacks.add(new ItemStack(BlockItemRegistry.DYE_KIT_PURPLE));
		stacks.add(new ItemStack(BlockItemRegistry.DYE_KIT_MAGENTA));
		stacks.add(new ItemStack(BlockItemRegistry.DYE_KIT_PINK));
		stacks.add(new ItemStack(BlockItemRegistry.DYE_KIT_BROWN));
		stacks.add(new ItemStack(BlockItemRegistry.DYE_KIT_WHITE));
		stacks.add(new ItemStack(BlockItemRegistry.DYE_KIT_GRAY));
		stacks.add(new ItemStack(BlockItemRegistry.DYE_KIT_LIGHT_GRAY));
		stacks.add(new ItemStack(BlockItemRegistry.DYE_KIT_BLACK));
	})
			.build();


	//System.out.println("Hello Fabric world!");
	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		EntityRegistry.registerEntities();
		SinkBehavior.registerBehavior();
		BlockItemRegistry.registerBlocks();
		if (FabricLoader.getInstance().isModLoaded("sandwichable")) {
			PFMSandwichableRegistry.register();
		}
		StatisticsRegistry.registerStatistics();
		SoundRegistry.registerSounds();
		BlockEntityRegistry.registerBlockEntities();
		NetworkRegistry.registerPackets();
		ScreenHandlersRegistry.registerScreenHandlers();
		RecipeRegistry.registerRecipes();
	}
}
