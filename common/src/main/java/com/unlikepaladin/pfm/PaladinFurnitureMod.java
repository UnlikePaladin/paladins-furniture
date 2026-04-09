package com.unlikepaladin.pfm;

import com.google.common.collect.ImmutableSet;
import com.unlikepaladin.pfm.blocks.SimpleBedBlock;
import com.unlikepaladin.pfm.blocks.behavior.BathtubBehavior;
import com.unlikepaladin.pfm.blocks.behavior.SinkBehavior;
import com.unlikepaladin.pfm.compat.PFMModCompatibility;
import com.unlikepaladin.pfm.compat.cookingforblockheads.PFMCookingForBlockheads;
import com.unlikepaladin.pfm.compat.farmersdelight.PFMFarmersDelight;
import com.unlikepaladin.pfm.compat.imm_ptl.PFMImmersivePortals;
import com.unlikepaladin.pfm.config.PaladinFurnitureModConfig;

import com.unlikepaladin.pfm.data.materials.DynamicBlockRegistry;
import com.unlikepaladin.pfm.data.materials.StoneVariantRegistry;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import com.unlikepaladin.pfm.items.PFMComponents;
import com.unlikepaladin.pfm.mixin.PFMPointOfInterestTypeAccessor;
import com.unlikepaladin.pfm.mixin.PFMPointOfInterestTypesAccessor;
import com.unlikepaladin.pfm.registry.dynamic.FurnitureEntry;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import com.unlikepaladin.pfm.utilities.PFMFileUtil;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Block;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class PaladinFurnitureMod {

	public static final String MOD_ID = "pfm";
	public static final ResourceLocation FURNITURE_DYED_ID = new ResourceLocation("pfm:furniture_dyed");
	public static final HashMap<Class<? extends Block>, FurnitureEntry<?>> furnitureEntryMap = new LinkedHashMap<>();
	public static SoundEvent FURNITURE_DYED_EVENT = SoundEvent.createVariableRangeEvent(FURNITURE_DYED_ID);

	public static final Logger GENERAL_LOGGER = LogManager.getLogger();
	public static Tuple<String, CreativeModeTab> FURNITURE_GROUP = new Tuple<>("furniture", null);
    public static Tuple<String, CreativeModeTab> BUILDING_BLOCKS = new Tuple<>("building_blocks", null);
    public static Tuple<String, CreativeModeTab> DYE_KITS = new Tuple<>("dye_kits", null);;
	private static PaladinFurnitureModUpdateChecker updateChecker;
	public static boolean isClientSide = false;
	public static List<PFMModCompatibility> pfmModCompatibilities = new ArrayList<>();
	public void commonInit() {
		if (PFMFileUtil.isModLoaded("connectormod")) {
			GENERAL_LOGGER.error("Sinytra Connector has been detected, this mod can cause rendering and other issues. PFM is not responsible for any issues it may cause.");
		}
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		PFMComponents.registerComponents();
		SinkBehavior.registerBehavior();
		BathtubBehavior.registerBehavior();
		updateChecker = new PaladinFurnitureModUpdateChecker();
		updateChecker.checkForUpdates(getPFMConfig());
		DynamicBlockRegistry.addBlockSetContainer(WoodVariantRegistry.INSTANCE.getType(), WoodVariantRegistry.INSTANCE);
		DynamicBlockRegistry.addBlockSetContainer(StoneVariantRegistry.INSTANCE.getType(), StoneVariantRegistry.INSTANCE);

		if (getModList().contains("cookingforblockheads"))
			pfmModCompatibilities.add(PFMCookingForBlockheads.getInstance());
		if (getModList().contains("farmersdelight"))
			pfmModCompatibilities.add(PFMFarmersDelight.getInstance());
		if (getModList().contains("immersive_portals"))
			pfmModCompatibilities.add(PFMImmersivePortals.getInstance());
		}

	public static void replaceHomePOIStates() {
		PoiType homePOI = BuiltInRegistries.POINT_OF_INTEREST_TYPE.get(PoiTypes.HOME);
		Set<BlockState> originalBedStates = ((PFMPointOfInterestTypeAccessor)(Object)homePOI).getBlockStates();
		Set<BlockState> addedBedStates = Arrays.stream(PaladinFurnitureModBlocksItems.getBeds()).flatMap(block -> block.getStateDefinition().getPossibleStates().stream().filter(state -> state.getValue(SimpleBedBlock.PART) == BedPart.HEAD)).collect(ImmutableSet.toImmutableSet());
		Set<BlockState> newBedStates = new HashSet<>();
		newBedStates.addAll(originalBedStates);
		newBedStates.addAll(addedBedStates);
		((PFMPointOfInterestTypeAccessor) (Object)homePOI).setBlockStates(ImmutableSet.copyOf(newBedStates));
		addedBedStates.forEach(state -> PFMPointOfInterestTypesAccessor.getBlockStateToPointOfInterestType().put(state, BuiltInRegistries.POINT_OF_INTEREST_TYPE.getHolderOrThrow(PoiTypes.HOME)));
	}

	@ExpectPlatform
    public static PaladinFurnitureModConfig getPFMConfig() {
		throw new AssertionError();
    }

	public static PaladinFurnitureModUpdateChecker getUpdateChecker() {
		return updateChecker;
	}

	@ExpectPlatform
	public static List<String> getModList() {throw new AssertionError();}

	@ExpectPlatform
	public static Map<String, String> getVersionMap() {throw new AssertionError();}

	@ExpectPlatform
	public static Loader getLoader() {
		throw new AssertionError();
	}

	private static Boolean optifine = null;
	public static boolean isOptifineLoaded() {
		if (optifine == null) {
			try {
				Class.forName("net.optifine.shaders.Shaders");
				optifine = true;
				return true;
			} catch (ClassNotFoundException e) {
				optifine = false;
				return false;
			}
		}
		return optifine;
	}

	public enum Loader implements StringRepresentable {
		FORGE("forge"),
		FABRIC_LIKE("fabric_like");
		final String name;
		Loader(String name) {
			this.name = name;
		}

		@Override
		public String getSerializedName() {
			return name;
		}
	}
}
