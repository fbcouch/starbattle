package com.ahsgaming.starbattle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * Created with IntelliJ IDEA.
 * User: jami
 * Date: 6/25/13
 * Time: 12:17 AM
 * To change this template use File | Settings | File Templates.
 */
public class TextureService {
    public static final String LOG = "TextureService";

    TextureAtlas textureAtlas;

    ObjectMap<String, TextureRegion> textureRegionMap;

    public TextureService(String file) {
        textureAtlas = new TextureAtlas(Gdx.files.local("assets/" + file));

        textureRegionMap = new ObjectMap<String, TextureRegion>();
    }

    /**
     * Basically, if image.png exists, get that and return it; otherwise, search the textureService
     * @param image
     * @return
     */
    public TextureRegion createSprite(String image) {
        if (textureRegionMap.containsKey(image)) return textureRegionMap.get(image);
        if (Gdx.files.local("assets/" + image + ".png").exists()) {
            TextureRegion region = new TextureRegion(new Texture(Gdx.files.local("assets/" + image + ".png")));
            textureRegionMap.put(image, region);
            return region;
        }

        return textureAtlas.createSprite(image);
    }
}
