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
    sandImage     = spriteSheet.getSprite(2, 1);
    copperImage   = spriteSheet.getSprite(1, 2);
    goldImage     = spriteSheet.getSprite(0, 2);
    diamondImage  = spriteSheet.getSprite(2, 3);
    
    SpriteSheet waterSpriteSheet = ImagesManager.shared().getSpriteSheet("water.png", Core.TILE_SIZE, Core.TILE_SIZE);

    Image[] waterImages = new Image[waterSpriteSheet.getVerticalCount()];
    
    for (int i = 0; i < waterSpriteSheet.getVerticalCount(); i++) {
      waterImages[i] = waterSpriteSheet.getSprite(0, i);
    }
    
    animatedWater = new Animation(waterImages, 150);
    animatedWater.setAutoUpdate(false);
    
    animatedLava = new Animation(new Image[] {
        spriteSheet.getSprite(13, 14), 
        spriteSheet.getSprite(14, 14), 
        spriteSheet.getSprite(15, 14),
        spriteSheet.getSprite(14, 15),
        spriteSheet.getSprite(15, 15),
    }, 100);
    animatedLava.setAutoUpdate(false);
  }
  
  public Image imageForBlock(Block block) {
    return imageForBlockClass(block.getClass());
  }
  
  public Image imageForBlockClass(Class klass) {
    if (klass.getName().equals(Lava.class.getName())) {
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
