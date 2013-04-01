package com.macbury.unamed.level;


import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import com.macbury.unamed.Core;
import com.macbury.unamed.ImagesManager;
import com.macbury.unamed.component.AnimatedSprite;
import com.macbury.unamed.component.Light;

public class BlockResources {
  SpriteSheet spriteSheet;
  Image sidewalkImage;
  Image bedrockImage;
  Image rockImage;
  
  public BlockResources() throws SlickException {
    spriteSheet = ImagesManager.shared().getSpriteSheet("terrain.png", Core.TILE_SIZE, Core.TILE_SIZE);
    
    sidewalkImage = spriteSheet.getSprite(1, 0);
    bedrockImage  = spriteSheet.getSprite(0, 14);
    rockImage     = spriteSheet.getSprite(0, 1);
  }
  
  public Image imageForBlock(Block block) {
    if (Rock.class.isInstance(block)) {
      return rockImage;
    } else if (Bedrock.class.isInstance(block)) {
      return bedrockImage;
    } else if (Sidewalk.class.isInstance(block)) {
      return sidewalkImage;
    } else {
      return null;
    }
  }
}
