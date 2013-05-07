package com.macbury.unamed;

import java.util.HashMap;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.util.Log;

import com.macbury.unamed.intefrace.InGameInterface;

public class ImagesManager {
  private static final int WINDOW_SKIN_SPLIT = 32;
  private static int MAX_BREAKING_BLOCK_SIZE = 9;
  public static ImagesManager sharedInstance = null;
  private HashMap<String, Image> imagesCache;
  private HashMap<String, SpriteSheet> spriteSheetCache;
  private SpriteSheet shadowMapSpriteSheet;
  private SpriteSheet inventorySpriteSheet;
  public  SpriteSheet windowSpriteSheet;
  public Image iconDynamite;
  public SpriteSheet iconsSpriteSheet;
  
  public static ImagesManager shared() throws SlickException {
    if (ImagesManager.sharedInstance == null) {
      ImagesManager.sharedInstance = new ImagesManager();
    }
    
    return ImagesManager.sharedInstance;
  }
  
  public ImagesManager() throws SlickException {
    this.imagesCache           = new HashMap<>();
    this.spriteSheetCache      = new HashMap<>();
    this.iconsSpriteSheet      = getSpriteSheet("hud/items.png", Core.TILE_SIZE, Core.TILE_SIZE);
    this.shadowMapSpriteSheet  = getSpriteSheet("effects/shadowmap.png", Core.TILE_SIZE, Core.TILE_SIZE);
    this.inventorySpriteSheet  = getSpriteSheet("hud/items.png", Core.TILE_SIZE, Core.TILE_SIZE);
    this.windowSpriteSheet     = getSpriteSheet("hud/windows.png", WINDOW_SKIN_SPLIT, WINDOW_SKIN_SPLIT);
    buildItemsCache();
  }
  
  private void buildItemsCache() {
    this.iconDynamite = this.inventorySpriteSheet.getSprite(4, 7);
  }

  public Image getImage(String name) throws SlickException {
    Image image = imagesCache.get(name);
    
    if (image == null) {
      String path = "res/images/"+name;
      Core.log(this.getClass(),"Loading image: " + path);
      image = new Image(path);
      imagesCache.put(name, image);
    }
    
    return image;
  }
  
  public SpriteSheet getSpriteSheet(String name, int width, int height) throws SlickException {
    SpriteSheet image = spriteSheetCache.get(name);
    
    if (image == null) {
      String path = "res/images/"+name;
      Core.log(this.getClass(),"Loading sprite sheet: " + path);
      image = new SpriteSheet(path, width, height);
      spriteSheetCache.put(name, image);
    }
    
    return image;
  }
  
  public Image getDestroyBlockEffectForProgress(float progress) throws SlickException {
    SpriteSheet sheet = getSpriteSheet("effects/breaking_block_animation.png", 32, 32);
    return sheet.getSprite(Math.round(MAX_BREAKING_BLOCK_SIZE * progress), 0);
  }

  public SpriteSheet getShadowMapSpriteSheet() {
    return this.shadowMapSpriteSheet;
  }
}
