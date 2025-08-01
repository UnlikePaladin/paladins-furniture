package com.unlikepaladin.pfm.client.forge;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.unlikepaladin.pfm.blocks.models.basicCoffeeTable.UnbakedCoffeeBasicTableModel;
import com.unlikepaladin.pfm.blocks.models.basicDesk.UnbakedBasicDeskModel;
import com.unlikepaladin.pfm.blocks.models.basicDeskCabinet.UnbakedBasicDeskCabinetModel;
import com.unlikepaladin.pfm.blocks.models.basicLamp.UnbakedBasicLampModel;
import com.unlikepaladin.pfm.blocks.models.basicTable.UnbakedBasicTableModel;
import com.unlikepaladin.pfm.blocks.models.bed.UnbakedBedModel;
import com.unlikepaladin.pfm.blocks.models.chair.UnbakedChairModel;
import com.unlikepaladin.pfm.blocks.models.chairClassic.UnbakedChairClassicModel;
import com.unlikepaladin.pfm.blocks.models.chairDinner.UnbakedChairDinnerModel;
import com.unlikepaladin.pfm.blocks.models.chairModern.UnbakedChairModernModel;
import com.unlikepaladin.pfm.blocks.models.classicCoffeeTable.UnbakedClassicCoffeeTableModel;
import com.unlikepaladin.pfm.blocks.models.classicNightstand.UnbakedClassicNightstandModel;
import com.unlikepaladin.pfm.blocks.models.classicStool.UnbakedClassicStoolModel;
import com.unlikepaladin.pfm.blocks.models.classicTable.UnbakedClassicTableModel;
import com.unlikepaladin.pfm.blocks.models.dinnerTable.UnbakedDinnerTableModel;
import com.unlikepaladin.pfm.blocks.models.fridge.UnbakedFreezerModel;
import com.unlikepaladin.pfm.blocks.models.fridge.UnbakedFridgeModel;
import com.unlikepaladin.pfm.blocks.models.fridge.UnbakedIronFridgeModel;
import com.unlikepaladin.pfm.blocks.models.kitchenCabinet.UnbakedKitchenCabinetModel;
import com.unlikepaladin.pfm.blocks.models.kitchenCounter.UnbakedKitchenCounterModel;
import com.unlikepaladin.pfm.blocks.models.kitchenCounterOven.UnbakedKitchenCounterOvenModel;
import com.unlikepaladin.pfm.blocks.models.kitchenDrawer.UnbakedKitchenDrawerModel;
import com.unlikepaladin.pfm.blocks.models.kitchenSink.UnbakedKitchenSinkModel;
import com.unlikepaladin.pfm.blocks.models.kitchenWallCounter.UnbakedKitchenWallCounterModel;
import com.unlikepaladin.pfm.blocks.models.kitchenWallDrawer.UnbakedKitchenWallDrawerModel;
import com.unlikepaladin.pfm.blocks.models.kitchenWallDrawerSmall.UnbakedKitchenWallDrawerSmallModel;
import com.unlikepaladin.pfm.blocks.models.ladder.UnbakedLadderModel;
import com.unlikepaladin.pfm.blocks.models.logStool.UnbakedLogStoolModel;
import com.unlikepaladin.pfm.blocks.models.logTable.UnbakedLogTableModel;
import com.unlikepaladin.pfm.blocks.models.mirror.UnbakedMirrorModel;
import com.unlikepaladin.pfm.blocks.models.modernCoffeeTable.UnbakedModernCoffeeTableModel;
import com.unlikepaladin.pfm.blocks.models.modernDinnerTable.UnbakedModernDinnerTableModel;
import com.unlikepaladin.pfm.blocks.models.modernStool.UnbakedModernStoolModel;
import com.unlikepaladin.pfm.blocks.models.simpleStool.UnbakedSimpleStoolModel;
import net.minecraft.client.render.model.BlockStateModel;
import net.minecraft.client.render.model.json.BlockModelDefinition;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;


public class BlockStateModelRegistryForge {
    public static final Codec<BlockModelDefinition> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(BlockModelDefinition.Variants.CODEC.optionalFieldOf("variants").forGetter(BlockModelDefinition::simpleModels), BlockModelDefinition.Multipart.CODEC.optionalFieldOf("multipart").forGetter(BlockModelDefinition::multipartModel)).apply(instance, BlockModelDefinition::new);
    }).validate((modelDefinition) -> {
        return modelDefinition.simpleModels().isEmpty() && modelDefinition.multipartModel().isEmpty() ? DataResult.error(() -> {
            return "Neither 'variants' nor 'multipart' found";
        }) : DataResult.success(modelDefinition);
    });
}
