package com.macbury.unamed.level;


import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import com.macbury.unamed.Core;
import com.macbury.unamed.component.Light;

public class BlockResources {
  SpriteSheet spriteSheet;
  Image sidewalkImage;
  Image bedrockImage;
  
  public BlockResources() throws SlickException {
    spriteSheet = new SpriteSheet("res/images/terrain.png", Core.TILE_SIZE, Core.TILE_SIZE);
    
    sidewalkImage = spriteSheet.getSprite(1, 0);
    bedrockImage  = spriteSheet.getSprite(0, 14);
  }
  
  public Image imageForBlock(Block block) {
    if (Bedrock.class.isInstance(block)) {
      return bedrockImage;
    } else if (Sidewalk.class.isInstance(block)) {
      return sidewalkImage;
    } else {
      return null;
    }
  }
}
