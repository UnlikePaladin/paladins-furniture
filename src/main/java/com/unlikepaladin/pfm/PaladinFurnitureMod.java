package com.unlikepaladin.pfm;

import com.unlikepaladin.pfm.blocks.behavior.SinkBehavior;
import com.unlikepaladin.pfm.blocks.blockentities.MicrowaveBlockEntity;
import com.unlikepaladin.pfm.compat.sandwichable.PFMSandwichableRegistry;
import com.unlikepaladin.pfm.menus.*;
import com.unlikepaladin.pfm.recipes.FreezingRecipe;
import com.unlikepaladin.pfm.recipes.FurnitureRecipe;
import com.unlikepaladin.pfm.recipes.FurnitureSerializer;
import com.unlikepaladin.pfm.registry.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CookingRecipeSerializer;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;
import java.util.Set;

public class PaladinFurnitureMod implements ModInitializer {

	public static final String MOD_ID = "pfm";

	public static final Identifier MICROWAVE_PACKET_ID = new Identifier(PaladinFurnitureMod.MOD_ID, "microwave_activate");

	public static final Identifier FREEZER = new Identifier(MOD_ID, "freezer_block_entity");
	public static Identifier MICROWAVE_UPDATE_PACKET_ID = new Identifier(PaladinFurnitureMod.MOD_ID, "microwave_button_update");
	public static final Identifier FURNITURE_DYED_ID = new Identifier("pfm:furniture_dyed");
	public static SoundEvent FURNITURE_DYED_EVENT = new SoundEvent(FURNITURE_DYED_ID);
	public static ScreenHandlerType<AbstractFreezerScreenHandler> FREEZER_SCREEN_HANDLER;
	public static ScreenHandlerType<StoveScreenHandler> STOVE_SCREEN_HANDLER;
	public static ScreenHandlerType<IronStoveScreenHandler> IRON_STOVE_SCREEN_HANDLER;
	public static ScreenHandlerType<MicrowaveScreenHandler> MICROWAVE_SCREEN_HANDLER;
	public static ScreenHandlerType<WorkbenchScreenHandler> WORKBENCH_SCREEN_HANDLER;
	public static final Identifier FURNITURE_ID = new Identifier(MOD_ID,"furniture");


	public static final Logger GENERAL_LOGGER = LogManager.getLogger();



	public static Identifier id(String path) {
		return new Identifier(MOD_ID, path);
	}
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
	public static RecipeType<FreezingRecipe> FREEZING_RECIPE;
	public static RecipeSerializer<FreezingRecipe> FREEZING_RECIPE_SERIALIZER;

	public static RecipeType<FurnitureRecipe> FURNITURE_RECIPE;
	public static RecipeSerializer<FurnitureRecipe> FURNITURE_SERIALIZER;

	//System.out.println("Hello Fabric world!");
	private static Set<BlockState> PFM_BED_STATES;

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

		FREEZING_RECIPE = Registry.register(Registry.RECIPE_TYPE, MOD_ID + ":freezing",  new RecipeType<FreezingRecipe>() {
			@Override
			public String toString() {return "freezing";}
		});
		FREEZING_RECIPE_SERIALIZER = Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(MOD_ID, "freezing"), new CookingRecipeSerializer<>(FreezingRecipe::new, 200));
		FREEZER_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(FREEZER, FreezerScreenHandler::new);
		FURNITURE_RECIPE = Registry.register(Registry.RECIPE_TYPE, FURNITURE_ID,  new RecipeType<FurnitureRecipe>() {
			@Override
			public String toString() {return "furniture";}
		});
		FURNITURE_SERIALIZER = Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(MOD_ID,"furniture"), new FurnitureSerializer());
		WORKBENCH_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(new Identifier(MOD_ID,"furniture"), WorkbenchScreenHandler::new);
		STOVE_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(new Identifier(MOD_ID, "stove_block_entity"), StoveScreenHandler::new);
		IRON_STOVE_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(new Identifier(MOD_ID, "iron_stove_block_entity"), IronStoveScreenHandler::new);
		MICROWAVE_SCREEN_HANDLER = ScreenHandlerRegistry.registerExtended(new Identifier(MOD_ID, "microwave_block_entity"), MicrowaveScreenHandler::new);

		ServerSidePacketRegistry.INSTANCE.register(MICROWAVE_PACKET_ID, (packetContext, attachedData) -> {
			BlockPos pos = attachedData.readBlockPos();
			boolean active = attachedData.readBoolean();
			packetContext.getTaskQueue().execute(() -> {
				if(Objects.nonNull(packetContext.getPlayer().world.getBlockEntity(pos))){
					MicrowaveBlockEntity microwaveBlockEntity = (MicrowaveBlockEntity) packetContext.getPlayer().world.getBlockEntity(pos);
					microwaveBlockEntity.setActive(active);
				}

			});
		});
	}
}
