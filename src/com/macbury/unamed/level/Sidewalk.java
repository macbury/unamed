package com.macbury.unamed.level;

import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

public class Sidewalk extends Block {
  public byte shadowMapIndex                        = SHADOW_NEED_TO_REFRESH;
  public final static byte SHADOW_NEED_TO_REFRESH   = 0;
  public final static byte SHADOW_NONE              = 1;
  public final static byte SHADOW_CORNER            = 2;
  
  public Sidewalk(int x, int y) {
    super(x,y);
    this.solid = false;
  }
  
  public boolean shouldRefreshShadowMap() {
    return this.shadowMapIndex == SHADOW_NEED_TO_REFRESH;
  }
  
  public Image getCurrentShadowMap(SpriteSheet sheet) {
    return sheet.getSprite(0, 5);
  }

  public void computeShadowMapBasedOnLevel(Level level) {
    Block topLeftBlock = level.getBlockForPosition(this.x - 1, this.x - 1);
  }
}
