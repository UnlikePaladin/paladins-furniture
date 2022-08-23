package com.unlikepaladin.pfm.compat.fabric.rei;

import net.minecraft.recipe.Recipe;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public interface FurnitureRecipeSizeProvider<R extends Recipe<?>> {
    final class Size {
        private final int width;
        private final int height;

        public Size(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (Size) obj;
            return this.width == that.width &&
                    this.height == that.height;
        }

        @Override
        public int hashCode() {
            return Objects.hash(width, height);
        }

        @Override
        public String toString() {
            return "Size[" +
                    "width=" + width + ", " +
                    "height=" + height + ']';
        }
    }

    @Nullable
    Size getSize(R recipe);
}