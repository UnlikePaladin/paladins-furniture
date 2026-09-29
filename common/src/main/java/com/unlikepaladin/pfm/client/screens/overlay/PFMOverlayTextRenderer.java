package com.unlikepaladin.pfm.client.screens.overlay;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.MipmapStrategy;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureContents;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.metadata.texture.TextureMetadataSection;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.ARGB;

import java.util.Arrays;

/**
 * Self-contained early overlay text renderer using GuiGraphicsExtractor.blit.
 * Generates an internal font atlas texture in NativeImage, independent of Minecraft's Font asset system.
 * Compatible with all backends (Vulkan / OpenGL).
 */
public class PFMOverlayTextRenderer {
    private static final Identifier FONT_TEXTURE_ID = Identifier.fromNamespaceAndPath("pfm", "overlay_font");

    private static final String GLYPH_CHARACTERS = " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.,!?-+/():;%&`*#=[]\"";
    private static final int GLYPH_COUNT = 83;
    private static final char GLYPH_MIN_CHAR = ' ';
    private static final char GLYPH_MAX_CHAR = 'z';
    public static final int LINE_HEIGHT = 17;

    private static class Glyph {
        char c;
        int x, y;
        int w, h;
        float u1, v1;
        float u2, v2;
        boolean drawable;
    }

    private static class GlyphData {
        int x, y;
        int w, h;
        int marginLeft, marginTop;
        int marginRight, marginBottom;
        int dataWidth, dataHeight;
    }

    private static final long[] GLYPH_RECTS = new long[] {
            0x1100040000L, 0x304090004L, 0x30209000DL, 0x304090016L, 0x30209001FL, 0x304090028L, 0x302090031L, 0x409003AL,
            0x302090043L, 0x30109004CL, 0x1080055L, 0x30209005DL, 0x302090066L, 0x3040A006FL, 0x304090079L, 0x304090082L,
            0x409008BL, 0x4090094L, 0x30409009DL, 0x3040900A6L, 0x3020900AFL, 0x3040900B8L, 0x3040900C1L, 0x3040A00CAL,
            0x3040900D4L, 0x40A00DD, 0x3040900E7L, 0x3020900F0L, 0x3020900F9L, 0x302090102L, 0x30209010BL, 0x302090114L,
            0x30209011DL, 0x302090126L, 0x30209012FL, 0x302070138L, 0x30209013FL, 0x302090148L, 0x302090151L, 0x3020A015AL,
            0x3020A0164L, 0x30209016EL, 0x302090177L, 0x102090180L, 0x302090189L, 0x302090192L, 0x30209019BL, 0x3020901A4L,
            0x3020901ADL, 0x3020A01B6L, 0x3020901C0L, 0x3020901C9L, 0x3020901D2L, 0x3020901DBL, 0x3020901E4L, 0x3020901EDL,
            0x3020901F6L, 0x3020A01FFL, 0x302090209L, 0x302090212L, 0x30209021BL, 0x302090224L, 0x30209022DL, 0x309060236L,
            0x10906023CL, 0x302070242L, 0x302090249L, 0x706090252L, 0x50409025BL, 0x202090264L, 0x10207026DL, 0x102070274L,
            0x30406027BL, 0x104060281L, 0x2010B0287L, 0x3020A0292L, 0xB0007029CL, 0x5040A02AAL, 0x3020A02B4L, 0x6050902BEL,
            0x20702C7L, 0x20702CEL, 0xB010902D5L,
    };

    private static final int FONT_PIXEL_SIZE_BITS = 2;
    private static final int GLYPH_DATA_COUNT = 387;

    private static final long[] GLYPH_DATA = Arrays.copyOf(new long[] {
            0x551695416A901554L, 0x569695A5A56AA55AL, 0x0555554155545AA9L, 0x916AA41569005A40L, 0xA5A569695A5A5696L, 0x51555556AA569695L, 0x696916A941554155L, 0x69155A55569555A5L,
            0x15541555456A9569L, 0xA9569545A4005500L, 0x569695A5A569695AL, 0x5545AA9569695A5AL, 0x916A941554555415L, 0x55A56AA95A5A5696L, 0x40555416A9555695L, 0x55A45AA505550155L,
            0xA55AAA455A555691L, 0x0169005A45569155L, 0xA945554015400554L, 0x569695A5A569695AL, 0x9545AA9569695A5AL, 0x4555555AA95A5556L, 0x55A4016900154555L, 0xA569695A5A45AA90L,
            0x69695A5A569695A5L, 0x9001541555455555L, 0x05AA4155505A4016L, 0xA40169005A501695L, 0x155555AAA4569505L, 0x5A405A4015405555L, 0x5A505A545AA45554L, 0x5A405A405A405A40L,
            0x555556A95A555A40L, 0x569005A400551554L, 0x9569A569695A5A45L, 0xA5A56969169A556AL, 0xA405555555155555L, 0x169005A50169505AL, 0x9505A40169005A40L, 0x5555155555AAA456L,
            0x95A66916AA905555L, 0x695A6695A6695A66L, 0x154555555A5695A6L, 0x5696916AA4155555L, 0x9695A5A569695A5AL, 0x45555155555A5A56L, 0xA5A5696916A94155L, 0x9569695A5A569695L,
            0x155515541555456AL, 0x695A5A5696916AA4L, 0x56AA569695A5A569L, 0x5540169155A55569L, 0x56AA515550015400L, 0x9695A5A569695A5AL, 0x05A5516AA55A5A56L, 0x500155005A401695L,
            0xA56A695A5A455555L, 0x0169015A55569556L, 0x54005500155005A4L, 0x555A555695AA9455L, 0xAA569555A5505AA5L, 0x0015415551555556L, 0x5A55AAA455A50169L, 0x40169005A4556915L,
            0x550155505AA5055AL, 0xA569695A5A455555L, 0x69695A5A569695A5L, 0x555554155545AA95L, 0x5A5A569695A5A455L, 0xA515AA55A5A56969L, 0x1545505500555055L, 0x95A6695A6695A569L,
            0xA4569A55A6695A66L, 0x5551555015554569L, 0x456A9569695A5A45L, 0xA5A5696916A95569L, 0x4155545555155555L, 0xA45A5A45A5A45A5AL, 0xA945A5A45A5A45A5L, 0x56A915A555695056L,
            0x4555501554055551L, 0x6945695169555AAAL, 0x55AAA45569156955L, 0x5A50055055551555L, 0xA569695A5A45AA50L, 0x69695A5A56AA95A5L, 0x555555155555A5A5L, 0x5A5A5696916AA415L,
            0x5A5696956AA56969L, 0x1555556AA569695AL, 0x96916A9415541555L, 0x9555A555695A5A56L, 0x6A9569695A5A4556L, 0xA405551554155545L, 0x69695A5A45A6905AL, 0x695A5A569695A5A5L,
            0x05550555555AA55AL, 0x5A555695AAA45555L, 0x4556916AA4156955L, 0x5555AAA45569155AL, 0x95AAA45555555515L, 0x6AA41569555A5556L, 0xA40169155A455691L, 0x1554005500155005L,
            0x695A5A5696916A94L, 0x5A5A56A69555A555L, 0x54155545AA956969L, 0x569695A5A4555555L, 0x9695AAA569695A5AL, 0x55A5A569695A5A56L, 0x55AA455555551555L, 0x5A416905A456915AL,
            0x515555AA45A51690L, 0x169005A400550055L, 0x9555A40169005A40L, 0x456A9569695A5A56L, 0xA5A4555515541555L, 0xA55A69569A569695L, 0x6969169A45A6915AL, 0x555555155555A5A5L,
            0x005A40169005A400L, 0x5A40169005A40169L, 0x155555AAA4556900L, 0x695A569154555555L, 0x6695A6695A9A95A5L, 0xA5695A5695A6695AL, 0x55154555555A5695L, 0x95A5695A56915455L,
            0x695AA695A6A95A5AL, 0x5695A5695A5695A9L, 0x155455154555555AL, 0x695A5A5696916A94L, 0x5A5A569695A5A569L, 0x541555456A956969L, 0x5696916AA4155515L, 0x56956AA569695A5AL,
            0x5005A40169155A55L, 0x6A94155400550015L, 0xA569695A5A569691L, 0x69695A5A569695A5L, 0x005A5415A5456A95L, 0x16AA415555500155L, 0xAA569695A5A56969L, 0x569695A5A55A6956L,
            0x5545555155555A5AL, 0x5555A5696916A941L, 0xA5545A5005A5155AL, 0x41555456A9569695L, 0x56955AAA45555155L, 0x9005A40169055A51L, 0x05A40169005A4016L, 0x5A45555055001550L,
            0x569695A5A569695AL, 0x9695A5A569695A5AL, 0x515541555456A956L, 0xA5A569695A5A4555L, 0xA569695A5A569695L, 0x555055A515AA55A5L, 0x95A5691545505500L, 0x695A6695A5695A56L,
            0x9A4569A55A6695A6L, 0x555015554169A456L, 0x9569695A5A455551L, 0x5A6515A515694566L, 0x555A5A569695A5A4L, 0x5A5A455555555155L, 0xA9569695A5A56969L, 0x0169015A41569456L,
            0x55505500155005A4L, 0x05A55169555AAA45L, 0x55A455A555A515A5L, 0x5155555AAA455690L, 0x696916A941554555L, 0xA95A5A56A695A9A5L, 0x56A9569695A6A569L, 0x9401540155415554L,
            0x05A5516AA45A9516L, 0xA40169005A401695L, 0x4154005540169005L, 0xA5A5696916A94155L, 0x9556945695169555L, 0x55555AAA45569156L, 0x6916A94155455551L, 0x56A5169555A5A569L,
            0xA9569695A5A56955L, 0x0015415541555456L, 0x4169A4055A4005A4L, 0xA916969169A5169AL, 0x50056954569555AAL, 0x5AAA455551540015L, 0xAA41569555A55569L, 0x55A555A551695516L,
            0x55005550555555AAL, 0x915694569416A401L, 0xA5A569695A5A45AAL, 0x41555456A9569695L, 0x69555AAA45555155L, 0x9415A415A5056951L, 0x0169015A40569056L, 0xA941554015400554L,
            0x569A95A5A5696916L, 0x9695A5A56A6956A9L, 0x415541555456A956L, 0xA5A5696916A94155L, 0x516AA55A5A569695L, 0x155415A915694569L, 0x555A95A915505540L, 0x5A55A95A91555545L,
            0x1694154154555569L, 0xA456956A95AA56A9L, 0x055416905A415515L, 0x696916A941554154L, 0x9055A515A555A5A5L, 0x05A4016900554056L, 0xAA45555055001550L, 0x005505555155555AL,
            0x6955AAA4569505A4L, 0x005500155055A515L, 0x690169405A400550L, 0x90569415A415A505L, 0x0569015A415A5056L, 0x6941540015400554L, 0xA456915A55A55691L, 0x16905A505A416905L,
            0x6901555405541694L, 0x16905A505A416941L, 0x6955A45A516905A4L, 0xA455415415545695L, 0x6A45555515556A56L, 0x56A45555515556A5L, 0xA56A45555515556AL, 0x5505515555A56956L,
            0x690569A4016A5001L, 0x4056954169A9459AL, 0x416A690156941569L, 0x15A9505A695169A6L, 0x4015505540055540L, 0x94169A4169A405A9L, 0x5A56A9A4555A415AL, 0x555169A955A5A55AL,
            0x6945A90555555415L, 0x1555154055416941L, 0x56AAA456A545A690L, 0x40555515A69156A5L, 0x6945A69015550555L, 0xA6915A6956AAA45AL, 0x5A6956AAA45A6955L, 0x455540555515A691L,
            0xAA9555556AA91555L, 0xA915555554555556L, 0x416905A556955A56L, 0x5A416905A416905AL, 0x555515555AA45690L, 0x6905A516955AA455L, 0x416905A416905A41L, 0x55556A95A556905AL,
            0xA5A5696915555554L, 0x5555155555L,
    }, GLYPH_DATA_COUNT);

    private static Glyph[] fontGlyphs;
    private static Glyph[] fontGlyphsLookup;
    private static int atlasWidth;
    private static int atlasHeight;
    private static boolean registered = false;

    public static synchronized void register(TextureManager textureManager) {
        if (!registered) {
            initGlyphs();
            textureManager.registerAndLoad(FONT_TEXTURE_ID, new FontTexture());
            registered = true;
        }
    }

    private static void initGlyphs() {
        if (fontGlyphs != null) return;

        fontGlyphs = new Glyph[GLYPH_COUNT];
        for (int i = 0; i < GLYPH_COUNT; i++) fontGlyphs[i] = new Glyph();

        fontGlyphsLookup = new Glyph[GLYPH_MAX_CHAR - GLYPH_MIN_CHAR + 1];
        for (int i = 0; i < fontGlyphsLookup.length; i++) fontGlyphsLookup[i] = new Glyph();

        GlyphData[] glyphsData = new GlyphData[GLYPH_COUNT];
        for (int i = 0; i < GLYPH_COUNT; i++) glyphsData[i] = new GlyphData();

        int texW = 0;
        int texH = 0;
        int drawableGlyphCount = 0;

        for (int i = 0; i < GLYPH_COUNT; i++) {
            char c = GLYPH_CHARACTERS.charAt(i);
            long glyphPacked = GLYPH_RECTS[i];
            int glyphX = (int) ((glyphPacked >>> 0) & 0xFFFF);
            int glyphWidth = (int) ((glyphPacked >>> 16) & 0xFF);
            int glyphY = 0;
            int glyphHeight = LINE_HEIGHT;
            int glyphMarginPacked = (int) ((glyphPacked >>> 24) & 0xFFFF);
            int glyphMarginTop = glyphMarginPacked & 0xFF;
            int glyphMarginBottom = (glyphMarginPacked >>> 8) & 0xFF;
            int glyphDataWidth = glyphWidth;
            int glyphDataHeight = glyphHeight - (glyphMarginTop + glyphMarginBottom);

            Glyph glyph = fontGlyphs[i];
            glyph.c = c;
            glyph.x = glyphX;
            glyph.y = glyphY;
            glyph.w = glyphWidth;
            glyph.h = glyphHeight;

            GlyphData data = glyphsData[i];
            data.x = glyphX;
            data.y = glyphY;
            data.w = glyphWidth;
            data.h = glyphHeight;
            data.marginLeft = 0;
            data.marginTop = glyphMarginTop;
            data.marginRight = 0;
            data.marginBottom = glyphMarginBottom;
            data.dataWidth = glyphDataWidth;
            data.dataHeight = glyphDataHeight;

            glyph.drawable = glyphDataWidth > 0 && glyphDataHeight > 0;
            if (glyph.drawable) {
                drawableGlyphCount++;
                texW += glyphWidth;
                if (texH < glyphHeight) texH = glyphHeight;
            }
        }

        final int padding = 1;
        final int spacing = 1;
        texW += spacing * (drawableGlyphCount - 1) + padding * 2;
        texH += padding * 2;

        atlasWidth = texW;
        atlasHeight = texH;

        int texX = padding;
        for (int i = 0; i < GLYPH_COUNT; i++) {
            Glyph glyph = fontGlyphs[i];
            GlyphData data = glyphsData[i];
            if (!glyph.drawable) continue;

            float u1 = (float) texX / (float) atlasWidth;
            float v1 = (float) padding / (float) atlasHeight;
            float u2 = (float) glyph.w / (float) atlasWidth;
            float v2 = (float) glyph.h / (float) atlasHeight;

            glyph.u1 = u1;
            glyph.v1 = v1;
            glyph.u2 = u1 + u2;
            glyph.v2 = v1 + v2;

            texX += data.marginLeft + data.dataWidth + data.marginRight + spacing;
        }

        for (int i = 0; i < GLYPH_COUNT; i++) {
            Glyph glyph = fontGlyphs[i];
            fontGlyphsLookup[glyph.c - GLYPH_MIN_CHAR] = glyph;
        }
    }

    private static NativeImage createFontNativeImage() {
        initGlyphs();
        NativeImage nativeImage = new NativeImage(atlasWidth, atlasHeight, true);

        // fill with transparent
        for (int x = 0; x < atlasWidth; x++) {
            for (int y = 0; y < atlasHeight; y++) {
                nativeImage.setPixel(x, y, 0);
            }
        }

        GlyphData[] glyphsData = new GlyphData[GLYPH_COUNT];
        for (int i = 0; i < GLYPH_COUNT; i++) {
            long glyphPacked = GLYPH_RECTS[i];
            int glyphWidth = (int) ((glyphPacked >>> 16) & 0xFF);
            int glyphMarginPacked = (int) ((glyphPacked >>> 24) & 0xFFFF);
            int glyphMarginTop = glyphMarginPacked & 0xFF;
            int glyphMarginBottom = (glyphMarginPacked >>> 8) & 0xFF;
            GlyphData data = new GlyphData();
            data.marginTop = glyphMarginTop;
            data.dataWidth = glyphWidth;
            data.dataHeight = LINE_HEIGHT - (glyphMarginTop + glyphMarginBottom);
            glyphsData[i] = data;
        }

        final int padding = 1;
        final int spacing = 1;
        final int glyphDataTypeSizeBits = Long.SIZE;
        int data0Index = 0;
        int data1Index = 0;
        int bit0Index = 0;
        int bit1Index = 1;

        int texX = padding;
        for (int i = 0; i < GLYPH_COUNT; i++) {
            Glyph glyph = fontGlyphs[i];
            GlyphData data = glyphsData[i];
            if (!glyph.drawable) continue;

            int drawX = texX;
            int drawY = padding + data.marginTop;

            for (int y = 0; y < data.dataHeight; y++) {
                for (int x = 0; x < data.dataWidth; x++) {
                    long c0 = ((GLYPH_DATA[data0Index] >>> bit0Index) & 1);
                    long c1 = ((GLYPH_DATA[data1Index] >>> bit1Index) & 1);

                    int a = (c0 == 0 && c1 == 1) ? 255 : 0;
                    if (a > 0) {
                        nativeImage.setPixel(drawX + x, drawY + y, ARGB.color(a, 255, 255, 255));
                    }

                    bit0Index += FONT_PIXEL_SIZE_BITS;
                    bit1Index += FONT_PIXEL_SIZE_BITS;

                    if (bit0Index >= glyphDataTypeSizeBits) {
                        bit0Index = bit0Index % glyphDataTypeSizeBits;
                        data0Index++;
                    }
                    if (bit1Index >= glyphDataTypeSizeBits) {
                        bit1Index = bit1Index % glyphDataTypeSizeBits;
                        data1Index++;
                    }
                }
            }

            texX += data.dataWidth + spacing;
        }

        return nativeImage;
    }

    public static float getStringWidth(String text, float scale) {
        if (text == null || text.isEmpty()) return 0.0f;
        initGlyphs();

        float maxWidth = 0.0f;
        float currentWidth = 0.0f;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '\n' || c == '\r') {
                if (currentWidth > maxWidth) maxWidth = currentWidth;
                currentWidth = 0.0f;
                continue;
            }

            if (c < GLYPH_MIN_CHAR || c > GLYPH_MAX_CHAR) c = ' ';
            Glyph glyph = fontGlyphsLookup[c - GLYPH_MIN_CHAR];
            if (glyph != null) {
                currentWidth += glyph.w * scale;
            }
        }

        if (currentWidth > maxWidth) maxWidth = currentWidth;
        return maxWidth;
    }

    public static void drawString(GuiGraphicsExtractor graphics, String text, float x, float y, float scale, int color) {
        if (text == null || text.isEmpty() || ARGB.alpha(color) == 0) return;
        initGlyphs();

        float curX = x;
        float curY = y;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '\n') {
                curX = x;
                curY += LINE_HEIGHT * scale;
                continue;
            }
            if (c == '\r') continue;

            if (c < GLYPH_MIN_CHAR || c > GLYPH_MAX_CHAR) c = ' ';
            Glyph glyph = fontGlyphsLookup[c - GLYPH_MIN_CHAR];
            if (glyph != null) {
                if (glyph.drawable) {
                    int charW = Math.round(glyph.w * scale);
                    int charH = Math.round(glyph.h * scale);
                    graphics.blit(
                            RenderPipelines.GUI_TEXTURED,
                            FONT_TEXTURE_ID,
                            Math.round(curX),
                            Math.round(curY),
                            glyph.u1 * atlasWidth,
                            glyph.v1 * atlasHeight,
                            charW,
                            charH,
                            glyph.w,
                            glyph.h,
                            atlasWidth,
                            atlasHeight,
                            color
                    );
                }
                curX += glyph.w * scale;
            }
        }
    }

    public static void drawCenteredString(GuiGraphicsExtractor graphics, String text, float centerX, float y, float scale, int color) {
        float width = getStringWidth(text, scale);
        drawString(graphics, text, centerX - width * 0.5f, y, scale, color);
    }

    @Environment(EnvType.CLIENT)
    private static class FontTexture extends SimpleTexture {
        public FontTexture() {
            super(FONT_TEXTURE_ID);
        }

        @Override
        public TextureContents loadContents(ResourceManager resourceManager) {
            NativeImage nativeImage = createFontNativeImage();
            return new TextureContents(nativeImage, new TextureMetadataSection(false, true, MipmapStrategy.AUTO, 0.0F));
        }
    }
}
