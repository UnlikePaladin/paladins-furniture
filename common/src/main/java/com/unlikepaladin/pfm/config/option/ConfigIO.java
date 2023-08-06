package com.unlikepaladin.pfm.config.option;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import net.minecraft.nbt.*;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class ConfigIO {
    public static AbstractConfigOption readCompressed(File file) throws IOException {
        try (FileInputStream inputStream = new FileInputStream(file);){
            AbstractConfigOption option = ConfigIO.readCompressed(inputStream);
            return option;
        }
    }

    public static AbstractConfigOption readCompressed(InputStream stream) throws IOException {
        try (DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(new GZIPInputStream(stream)));){
            AbstractConfigOption nbtCompound = ConfigIO.read(dataInputStream, ConfigSizeTracker.EMPTY);
            return nbtCompound;
        }
    }

    public static void writeCompressed(AbstractConfigOption compound, File file) throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream(file);){
            ConfigIO.writeCompressed(compound, outputStream);
        }
    }

    public static void writeCompressed(AbstractConfigOption compound, OutputStream stream) throws IOException {
        try (DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(new GZIPOutputStream(stream)));){
            ConfigIO.write(compound, dataOutputStream);
        }
    }

    public static void write(AbstractConfigOption compound, File file) throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(file);
             DataOutputStream dataOutputStream = new DataOutputStream(fileOutputStream);){
            ConfigIO.write(compound, dataOutputStream);
        }
    }

    @Nullable
    public static AbstractConfigOption read(File file) throws IOException {
        if (!file.exists()) {
            return null;
        }
        try (FileInputStream fileInputStream = new FileInputStream(file);){
            AbstractConfigOption nbtCompound;
            try (DataInputStream dataInputStream = new DataInputStream(fileInputStream);){
                nbtCompound = read(dataInputStream, ConfigSizeTracker.EMPTY);
            }
            return nbtCompound;
        }
    }

    public static AbstractConfigOption read(DataInput input) throws IOException {
        return ConfigIO.read(input, ConfigSizeTracker.EMPTY);
    }

    public static AbstractConfigOption read(DataInput input, ConfigSizeTracker tracker) throws IOException {
        AbstractConfigOption configOption = ConfigIO.read(input, 0, tracker);
        if (configOption != null) {
            return configOption;
        }
        throw new IOException("Object was not instance of AbstractConfig");
    }

    protected static void write(AbstractConfigOption element, DataOutput output) throws IOException {
        output.writeByte(element.getConfigType());
        if (element.getType() != Boolean.class) {
            PaladinFurnitureMod.GENERAL_LOGGER.warn("Unsupported Type: " + element.getType());
            return;
        }
        element.write(output);
    }

    private static AbstractConfigOption read(DataInput input, int depth, ConfigSizeTracker tracker) throws IOException {
        byte b = input.readByte();
        if (b == 0) {
            return NullConfigOption.INSTANCE;
        }
        try {
            return ConfigOptionTypes.byId(b).read(input, depth, tracker);
        }
        catch (IOException iOException) {
            CrashReport crashReport = CrashReport.create(iOException, "Loading Config data");
            CrashReportSection crashReportSection = crashReport.addElement("Config Element");
            crashReportSection.add("Config type", b);
            throw new CrashException(crashReport);
        }
    }
}

