package com.unlikepaladin.pfm.config.option;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.handler.codec.EncoderException;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

import net.minecraft.network.chat.contents.TranslatableContents;
import org.jetbrains.annotations.NotNull;

import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

public abstract class AbstractConfigOption<T> implements Comparable<String> {
    public static final byte NULL_TYPE = 0;
    public static final byte BOOL_TYPE = 1;

    public abstract Component getTitle();

    public abstract String getCategory();

    public abstract T getValue();

    public abstract T getDefaultValue();

    public abstract Component getToolTip();

    public abstract void setValue(T value);

    public abstract Class<T> getType();

    public abstract boolean isDefault();

    public abstract Side getSide();

    public abstract byte getConfigType();

    public static Side getSide(String string) {
        if (Objects.equals(string, Side.CLIENT.getSerializedName())) {
            return Side.CLIENT;
        } else if(Objects.equals(string, Side.SERVER.getSerializedName())) {
            return Side.SERVER;
        }
        return null;
    }
    @Override
    public String toString() {
        return "{Type: " + getType() + ", Title: " + ((TranslatableContents)getTitle().getContents()).getKey() + ", Category: " + getCategory() +  ", Value: " + getValue() + ", Side:" + getSide() + "}";
    }

    public abstract void write(DataOutput output) throws IOException;

    public static void writeConfigOption(FriendlyByteBuf packetByteBuf, AbstractConfigOption abstractConfigOption) {
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

    public static AbstractConfigOption readConfigOption(FriendlyByteBuf packetByteBuf) {
        return readConfigOption(packetByteBuf, new ConfigSizeTracker(2097152L));
    }

    public static AbstractConfigOption readConfigOption(FriendlyByteBuf packetByteBuf, ConfigSizeTracker sizeTracker) {
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

    @Override
    public int compareTo(@NotNull String o) {
        return this.getCategory().compareTo(o);
    }
}

