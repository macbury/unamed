package com.macbury.unamed.level;


import org.newdawn.slick.Animation;
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
  Image sandImage;
  Image copperImage;
  Image goldImage;
  private Image diamondImage;
  private Animation animatedWater;
  private Animation animatedLava;
  private Image cobblestone;
  
  public static BlockResources shared() throws SlickException {
    if (shared == null) {
      shared = new BlockResources();
    }
    
    return shared;
  }
  
  public BlockResources() throws SlickException {
    spriteSheet = ImagesManager.shared().getSpriteSheet("terrain.png", Core.TILE_SIZE, Core.TILE_SIZE);
    
    sidewalkImage = spriteSheet.getSprite(0, 0);
    bedrockImage  = spriteSheet.getSprite(1, 1);
    rockImage     = spriteSheet.getSprite(1, 0);
    dirtImage     = spriteSheet.getSprite(2, 0);
    coalImage     = spriteSheet.getSprite(2, 2);
    sandImage     = spriteSheet.getSprite(2, 1);
    copperImage   = spriteSheet.getSprite(1, 2);
    goldImage     = spriteSheet.getSprite(0, 2);
    diamondImage  = spriteSheet.getSprite(2, 3);
    cobblestone   = spriteSheet.getSprite(4, 6);
    
    SpriteSheet waterSpriteSheet = ImagesManager.shared().getSpriteSheet("water.png", Core.TILE_SIZE, Core.TILE_SIZE);
    Image[] liquidImages         = new Image[waterSpriteSheet.getVerticalCount()];
    
    for (int i = 0; i < waterSpriteSheet.getVerticalCount(); i++) {
      liquidImages[i] = waterSpriteSheet.getSprite(0, i);
    }
    
    animatedWater = new Animation(liquidImages, 150);
    animatedWater.setAutoUpdate(false);
    
    SpriteSheet lavaSpriteSheet = ImagesManager.shared().getSpriteSheet("lava.png", Core.TILE_SIZE, Core.TILE_SIZE);
    liquidImages                = new Image[lavaSpriteSheet.getVerticalCount()];
    for (int i = 0; i < lavaSpriteSheet.getVerticalCount(); i++) {
      liquidImages[i] = lavaSpriteSheet.getSprite(0, i);
    }
    
    animatedLava = new Animation(liquidImages, 150);
    animatedLava.setAutoUpdate(false);
  }
  
  public Image imageForBlock(Block block) {
    if (Sidewalk.class.isInstance(block)) {
      Sidewalk sidewalk = (Sidewalk) block;
      if (sidewalk.getHarvestedBlockType() == null) {
        return imageForBlockClass(block.getClass());
      } else {
        return imageForBlockClass(sidewalk.getHarvestedBlockType());
      }
    } else {
      return imageForBlockClass(block.getClass());
    }
    
  }
  
  public Image imageForBlockClass(Class klass) {
    if (klass.getName().equals(Cobblestone.class.getName())) {
      return cobblestone;
    } else if (klass.getName().equals(Lava.class.getName())) {
      return animatedLava.getCurrentFrame();
    } else if (klass.getName().equals(Water.class.getName())) {
      return animatedWater.getCurrentFrame();
    } else if (klass.getName().equals(DiamondOre.class.getName())) {
      return diamondImage;
    } else if (klass.getName().equals(GoldOre.class.getName())) {
      return goldImage;
    } else if (klass.getName().equals(CopperOre.class.getName())) {
      return copperImage;
    } else if (klass.getName().equals(Sand.class.getName())) {
      return sandImage;
    } else if (klass.getName().equals(CoalOre.class.getName())) {
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

  public void update(int delta) {
    animatedWater.update(delta);
    animatedLava.update(delta);
  }
}
