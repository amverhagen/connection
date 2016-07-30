package com.andrew.verhagen.line.gambit.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;

public class Assets {

    public AssetManager manager;

    public Assets() {
        this.manager = new AssetManager();
        this.loadFonts();
        this.manager.finishLoading();
        this.loadImages();
    }

    private void loadImages() {
        this.manager.load("one-player-button.png", Texture.class);
        this.manager.load("two-player-button.png", Texture.class);
    }

    /**
     * Loads the fonts used in the game.
     */
    private void loadFonts() {
        FreeTypeFontGenerator.FreeTypeFontParameter nixieParams = new FreeTypeFontGenerator.FreeTypeFontParameter();
        nixieParams.borderColor = Color.WHITE;
        nixieParams.borderWidth = 1.1f;
        nixieParams.size = 48;
        generateAndLoadTrueTypeFont("NixieOne.ttf", "nixie48.ttf", nixieParams);
    }

    /**
     * Adds a true type font to the asset manager.
     *
     * @param fontFileName      The file name of the true type font used to generate this font.
     * @param fontReferenceName The name that will be used to reference the created font from within the program.
     * @param fontParams        The params that specify things like size, color, border width.
     */
    public void generateAndLoadTrueTypeFont(String fontFileName, String fontReferenceName, FreeTypeFontGenerator.FreeTypeFontParameter fontParams) {
        FileHandleResolver resolver = new InternalFileHandleResolver();
        manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
        FreetypeFontLoader.FreeTypeFontLoaderParameter loaderParams = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        loaderParams.fontFileName = fontFileName;
        loaderParams.fontParameters = fontParams;
        manager.load(fontReferenceName, BitmapFont.class, loaderParams);
    }
}
