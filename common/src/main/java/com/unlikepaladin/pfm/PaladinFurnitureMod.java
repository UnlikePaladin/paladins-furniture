package com.unlikepaladin.pfm;

import com.unlikepaladin.pfm.blocks.behavior.BathtubBehavior;
import com.unlikepaladin.pfm.blocks.behavior.SinkBehavior;
import com.unlikepaladin.pfm.config.PaladinFurnitureModConfig;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PaladinFurnitureMod {

	public static final String MOD_ID = "pfm";
	public static final Identifier FURNITURE_DYED_ID = new Identifier("pfm:furniture_dyed");
	public static SoundEvent FURNITURE_DYED_EVENT = new SoundEvent(FURNITURE_DYED_ID);

	public static final Logger GENERAL_LOGGER = LogManager.getLogger();
	public static ItemGroup FURNITURE_GROUP;

	public static ItemGroup DYE_KITS;
	private static PaladinFurnitureModUpdateChecker updateChecker;

	public void commonInit() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		SinkBehavior.registerBehavior();
		BathtubBehavior.registerBehavior();
		updateChecker = new PaladinFurnitureModUpdateChecker();
		updateChecker.checkForUpdates(getPFMConfig());
	}


	@ExpectPlatform
    public static PaladinFurnitureModConfig getPFMConfig() {
		throw new AssertionError();
    }

	public static PaladinFurnitureModUpdateChecker getUpdateChecker() {
		return updateChecker;
	}
}
