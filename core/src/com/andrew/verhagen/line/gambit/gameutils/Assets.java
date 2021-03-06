package com.andrew.verhagen.line.gambit.gameutils;

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
    public static final String black = "images/black.png";
    public static final String options = "images/options-bar.png";
    public static final String white = "images/white.png";
    public static final String onePlayerButton = "images/one-player-button.png";
    public static final String twoPlayerButton = "images/two-player-button.png";
    public static final String gambitTittle = "images/gambit-title.png";
    public static final String musicIcon = "images/music-icon.png";
    public static final String retry = "images/retry.png";
    public static final String clear = "images/clear.png";
    public static final String nixie48 = "nixie48.ttf";

    public AssetManager manager;

    public Assets() {
        this.manager = new AssetManager();
        this.loadFonts();
        this.manager.finishLoading();
        this.loadImages();
    }

    private void loadImages() {
        this.manager.load(black, Texture.class);
        this.manager.load(options, Texture.class);
        this.manager.load(white, Texture.class);
        this.manager.load(onePlayerButton, Texture.class);
        this.manager.load(twoPlayerButton, Texture.class);
        this.manager.load(gambitTittle, Texture.class);
        this.manager.load(musicIcon, Texture.class);
        this.manager.load(retry, Texture.class);
        this.manager.load(clear, Texture.class);
    }

    /**
     * Loads the fonts used in the game.
     */
    private void loadFonts() {
        FreeTypeFontGenerator.FreeTypeFontParameter nixieParams = new FreeTypeFontGenerator.FreeTypeFontParameter();
        nixieParams.borderColor = Color.WHITE;
        nixieParams.borderWidth = 1.1f;
        nixieParams.size = 48;
        generateAndLoadTrueTypeFont("fonts/NixieOne.ttf", nixie48, nixieParams);
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
