package com.unlikepaladin.pfm.config.option;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import net.minecraft.text.Text;
import org.apache.commons.lang3.ObjectUtils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NullConfigOption extends AbstractConfigOption<ObjectUtils.Null>{
    public static final ConfigOptionType<NullConfigOption> TYPE = new ConfigOptionType<NullConfigOption>(){

        public NullConfigOption read(DataInput dataInput, int i, ConfigSizeTracker nbtTagSizeTracker, byte n) {
            nbtTagSizeTracker.add(64L);
            return INSTANCE;
        }

        @Override
        public String getCrashReportName() {
            return "END";
        }

        @Override
        public boolean isImmutable() {
            return true;
        }

        @Override
        public NullConfigOption read(DataInput input, int depth, ConfigSizeTracker tracker) {
            return this.read(input, depth, tracker, (byte) 0x0000);
        }
    };
    public static final NullConfigOption INSTANCE = new NullConfigOption();
    @Override
    public Text getTitle() {
        return null;
    }

    @Override
    public String getCategory() {
        return null;
    }

    @Override
    public ObjectUtils.Null getValue() {
        return null;
    }

    @Override
    public ObjectUtils.Null getDefaultValue() {
        return null;
    }

    @Override
    public Text getToolTip() {
        return null;
    }

    @Override
    public void setValue(ObjectUtils.Null value) {

    }

    @Override
    public Class<ObjectUtils.Null> getType() {
        return null;
    }

    @Override
    public boolean isDefault() {
        return false;
    }

    @Override
    public Side getSide() {
        return null;
    }

    @Override
    public byte getConfigType() {
        return NULL_TYPE;
    }

    @Override
    public void write(DataOutput output) throws IOException {
        PaladinFurnitureMod.GENERAL_LOGGER.warn("Writing Null Config Option");
    }
}
