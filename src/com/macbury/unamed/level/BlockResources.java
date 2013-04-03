package com.macbury.unamed.level;


import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import com.macbury.unamed.Core;
import com.macbury.unamed.ImagesManager;
import com.macbury.unamed.component.AnimatedSprite;
import com.macbury.unamed.component.Light;

public class BlockResources {
  private static BlockResources shared;
  SpriteSheet spriteSheet;
  Image sidewalkImage;
  Image bedrockImage;
  Image rockImage;
  Image dirtImage;
  Image coalImage;
  
  public static BlockResources shared() throws SlickException {
    if (shared == null) {
      shared = new BlockResources();
    }
    
    return shared;
  }
  
  public BlockResources() throws SlickException {
    spriteSheet = ImagesManager.shared().getSpriteSheet("terrain.png", Core.TILE_SIZE, Core.TILE_SIZE);
    
    sidewalkImage = spriteSheet.getSprite(1, 1);
    bedrockImage  = spriteSheet.getSprite(0, 14);
    rockImage     = spriteSheet.getSprite(1, 0);
    dirtImage     = spriteSheet.getSprite(3, 1);
    coalImage     = spriteSheet.getSprite(2, 2);
  }
  
  public Image imageForBlock(Block block) {
    return imageForBlockClass(block.getClass());
  }
  
  public Image imageForBlockClass(Class klass) {
    if (klass.getName().equals(CoalOre.class.getName())) {
      return coalImage;
    } else if (klass.getName().equals(Dirt.class.getName())) {
      return dirtImage;
    } else if (klass.getName().equals(Rock.class.getName())) {
      return rockImage;
    } else if (klass.getName().equals(Bedrock.class.getName())) {
      return bedrockImage;
    } else if (klass.getName().equals(Sidewalk.class.getName())) {
      return sidewalkImage;
    } else {
      return null;
    }
  }
}
