package com.macbury.unamed;

import java.util.HashMap;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.util.Log;

public class ImagesManager {
  public static ImagesManager sharedInstance = null;
  private HashMap<String, Image> imagesCache;
  private HashMap<String, SpriteSheet> spriteSheetCache;
  
  public static ImagesManager shared() {
    if (ImagesManager.sharedInstance == null) {
      ImagesManager.sharedInstance = new ImagesManager();
    }
    
    return ImagesManager.sharedInstance;
  }
  
  public ImagesManager() {
    this.imagesCache      = new HashMap<>();
    this.spriteSheetCache = new HashMap<>();
  }
  
  public Image getImage(String name) throws SlickException {
    Image image = imagesCache.get(name);
    
    if (image == null) {
      String path = "res/images/"+name;
      Log.info("Loading image: " + path);
      image = new Image(path);
      imagesCache.put(name, image);
    }
    
    return image;
  }
  
  public SpriteSheet getSpriteSheet(String name, int width, int height) throws SlickException {
    SpriteSheet image = spriteSheetCache.get(name);
    
    if (image == null) {
      String path = "res/images/"+name;
      Log.info("Loading sprite sheet: " + path);
      image = new SpriteSheet(path, width, height);
      spriteSheetCache.put(name, image);
    }
    
    return image;
  }
}
