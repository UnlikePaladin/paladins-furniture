package com.unlikepaladin.pfm;

import com.unlikepaladin.pfm.blocks.behavior.BathtubBehavior;
import com.unlikepaladin.pfm.blocks.behavior.SinkBehavior;
import com.unlikepaladin.pfm.compat.PFMModCompatibility;
import com.unlikepaladin.pfm.compat.cookingforblockheads.PFMCookingForBlockheads;
import com.unlikepaladin.pfm.compat.farmersdelight.PFMFarmersDelight;
import com.unlikepaladin.pfm.compat.imm_ptl.PFMImmersivePortals;
import com.unlikepaladin.pfm.config.PaladinFurnitureModConfig;

import com.unlikepaladin.pfm.data.materials.DynamicBlockRegistry;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import com.unlikepaladin.pfm.registry.BlockEntityRegistry;
import com.unlikepaladin.pfm.registry.dynamic.LateBlockRegistry;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import com.unlikepaladin.pfm.utilities.Version;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PaladinFurnitureMod {

	public static final String MOD_ID = "pfm";
	public static final Identifier FURNITURE_DYED_ID = new Identifier("pfm:furniture_dyed");
	public static SoundEvent FURNITURE_DYED_EVENT = new SoundEvent(FURNITURE_DYED_ID);

	public static final Logger GENERAL_LOGGER = LogManager.getLogger();
	public static ItemGroup FURNITURE_GROUP;

	public static ItemGroup DYE_KITS;
	private static PaladinFurnitureModUpdateChecker updateChecker;
	public static boolean isClient = false;
	public static List<PFMModCompatibility> pfmModCompatibilities = new ArrayList<>();
	public void commonInit() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		SinkBehavior.registerBehavior();
		BathtubBehavior.registerBehavior();
		updateChecker = new PaladinFurnitureModUpdateChecker();
		updateChecker.checkForUpdates(getPFMConfig());
		DynamicBlockRegistry.addBlockSetContainer(WoodVariantRegistry.INSTANCE.getType(), WoodVariantRegistry.INSTANCE);
		if (getModList().contains("cookingforblockheads"))
			pfmModCompatibilities.add(PFMCookingForBlockheads.getInstance());
		if (getModList().contains("farmersdelight"))
			pfmModCompatibilities.add(PFMFarmersDelight.getInstance());
		if (getModList().contains("imm_ptl_core") || getModList().contains("immersive_portals"))
			pfmModCompatibilities.add(new PFMImmersivePortals());
		}


	public static void registerLateEntries() {
		DynamicBlockRegistry.initialize();
		try {
			LateBlockRegistry.registerBlocks();
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		BlockEntityRegistry.registerBlockEntities();
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

	public enum Loader implements StringIdentifiable {
		FORGE("forge"),
		FABRIC_LIKE("fabric_like");
		final String name;
		Loader(String name) {
			this.name = name;
		}

		@Override
		public String asString() {
			return name;
		}
	}
}
