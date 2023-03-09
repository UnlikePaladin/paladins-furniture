package com.unlikepaladin.pfm.config.option;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.handler.codec.EncoderException;
import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import javax.annotation.Nullable;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

public abstract class AbstractConfigOption<T> {
    public static final byte NULL_TYPE = 0;
    public static final byte BOOL_TYPE = 1;

    public abstract Text getTitle();

    public abstract String getCategory();

    public abstract T getValue();

    public abstract T getDefaultValue();

    public abstract Text getToolTip();

    public abstract void setValue(T value);

    public abstract Class<T> getType();

    public abstract boolean isDefault();

    public abstract Side getSide();

    public abstract byte getConfigType();

    public static Side getSide(String string) {
        if (Objects.equals(string, Side.CLIENT.asString())) {
            return Side.CLIENT;
        } else if(Objects.equals(string, Side.SERVER.asString())) {
            return Side.SERVER;
        }
        return null;
    }
    @Override
    public String toString() {
        return "{Type: " + getType() + ", Title: " + ((TranslatableText)getTitle()).getKey() + ", Category: " + getCategory() +  ", Value: " + getValue() + ", Side:" + getSide() + "}";
    }

    public abstract void write(DataOutput output) throws IOException;

    public static void writeConfigOption(PacketByteBuf packetByteBuf, AbstractConfigOption abstractConfigOption) {
        if (abstractConfigOption == null) {
            packetByteBuf.writeByte(0);
        } else {
            try {
                ConfigIO.write(abstractConfigOption, new ByteBufOutputStream(packetByteBuf));
            }
            catch (IOException iOException) {
                throw new EncoderException(iOException);
            }
        }
    }

    public static AbstractConfigOption readConfigOption(PacketByteBuf packetByteBuf) {
        return readConfigOption(packetByteBuf, new ConfigSizeTracker(2097152L));
    }
    @Nullable
    public static AbstractConfigOption readConfigOption(PacketByteBuf packetByteBuf, ConfigSizeTracker sizeTracker) {
        int i = packetByteBuf.readerIndex();
        byte b0 = packetByteBuf.readByte();
        if (b0 == 0) {
            PaladinFurnitureMod.GENERAL_LOGGER.warn("Config Option Type was null");
            return null;
        } else {
            packetByteBuf.readerIndex(i);
            try {
                return ConfigIO.read(new ByteBufInputStream(packetByteBuf), sizeTracker);
            } catch (IOException var5) {
                throw new EncoderException(var5);
            }
        }
    }

}

