package com.lunagameserve;

import java.io.InputStream;

/**
 * Created by sixstring982 on 1/29/16.
 */
public interface Texture2D {
    /**
     * Bind this Texture, using glBind
     */
    public void bind();

    public void load(InputStream stream);

    public void unload();
}
