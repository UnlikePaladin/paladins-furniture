package com.unlikepaladin.pfm.compat.arrp;

import net.devtech.arrp.json.recipe.*;

public class JFurnitureRecipe implements Cloneable  {
    private final JResult result;
    protected final JPattern pattern;
    protected final JKeys key;

    public JFurnitureRecipe(final JResult result, JPattern pattern, JKeys keys) {
        this.result = result;
        this.type = "pfm:furniture";
        this.pattern = pattern;
        this.key = keys;
    }

    protected final String type;

    protected String group;

    public JFurnitureRecipe group(final String group) {
        this.group = group;
        return this;
    }

    @Override
    protected JFurnitureRecipe clone() {
        try {
            return (JFurnitureRecipe) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }
}
