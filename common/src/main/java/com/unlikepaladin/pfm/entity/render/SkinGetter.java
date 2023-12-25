package com.unlikepaladin.pfm.entity.render;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class SkinGetter {
    public static String playerName = "UnlikePaladin";
    public String basejson;
    public String getFromName(String name) {
        try {
            URL url_0 = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
            InputStreamReader reader_0 = new InputStreamReader(url_0.openStream());
            String uuid = new JsonParser().parse(reader_0).getAsJsonObject().get("id").getAsString();

            URL url_1 = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
            InputStreamReader reader_1 = new InputStreamReader(url_1.openStream());
            JsonObject textureProperty = new JsonParser().parse(reader_1).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
            String texture = textureProperty.get("value").getAsString();
            String signature = textureProperty.get("signature").getAsString();
            basejson = texture;
            // System.out.println("DEBUG:" + texture);
            return new String(texture);
        } catch (IOException e) {
            System.err.println("Could not get skin data from session servers!");
            e.printStackTrace();
            return null;
        }
    }


    public static final Identifier TEXTURE = new Identifier("minecraft", "textures/entity/steve.png");

    public Identifier getjson() {
        this.getFromName(playerName);
        System.out.println("Getting the Json");
        System.out.println("DEBUG:" + basejson);
        byte[] decodedBytes = Base64.getDecoder().decode(basejson);
        String decodedjson = new String(decodedBytes);
        // System.out.println("DEBUG:" + decodedjson);

        if (decodedjson.contains("url")) {
            String str = decodedjson.substring(decodedjson.lastIndexOf("\"http") + 1, decodedjson.indexOf("}"));
            System.out.println(str);

            String st = str.replaceAll("\"", "");
            System.out.println(st);
            Path texturepath = Paths.get((FabricLoader.getInstance().getConfigDir() + "/resourcepacks/" + playerName + ".png"));

            if (!texturepath.toFile().exists()) {
                System.out.println("Beginning to save file");
                try (InputStream in = new URL(st).openStream()) {
                    Files.copy(in, texturepath);
                    in.close();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    System.out.println("Error: Malformed Skin URL");

                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Opening Stream failed");

                }
            } else {
                System.out.println("File Already Exists");
            }
            System.out.println(texturepath);

            return new Identifier(texturepath.toString());
        } else {
            System.out.println("Identifier is null, defaulting to Steve");
            return new Identifier(TEXTURE.toString());


        }
    }


}
