package com.macbury.unamed.level;

import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

public class Sidewalk extends Block {
  public byte shadowMapIndex                           = SHADOW_NEED_TO_REFRESH;
  public final static byte SHADOW_NEED_TO_REFRESH      = 0x0;
  public final static byte SHADOW_NONE                 = 0x2;
  public final static byte SHADOW_CORNER_TOP_LEFT      = 0x4;
  public final static byte SHADOW_LEFT                 = 0x8;
  public final static byte SHADOW_TOP                  = 0x16;
  public final static byte SHADOW_CORNER_BOTTOM_LEFT   = 0x32;
  public Sidewalk(int x, int y) {
    super(x,y);
    this.solid = false;
  }
  
  public boolean shouldRefreshShadowMap() {
    return this.shadowMapIndex == SHADOW_NEED_TO_REFRESH;
  }
  
  public Image getCurrentShadowMap(SpriteSheet sheet) {
    return null; //sheet.getSprite(0, 5);
  }

  public void computeShadowMapBasedOnLevel(Level level) {
    Block leftBlock     = level.getBlockForPosition(this.x - 1, this.y);
    Block topLeftBlock  = level.getBlockForPosition(this.x - 1, this.y - 1);
    Block topBlock      = level.getBlockForPosition(this.x, this.y - 1);
    //Block topRightBlock = level.getBlockForPosition(this.x + 1, this.y - 1);
    
    shadowMapIndex = SHADOW_NONE;
    
    if ((topBlock != null && topBlock.solid) && (leftBlock != null && leftBlock.solid)) {
      shadowMapIndex = (byte) (shadowMapIndex | SHADOW_CORNER_TOP_LEFT);
    } else if (leftBlock != null && leftBlock.solid) {
      shadowMapIndex = (byte) (shadowMapIndex | SHADOW_LEFT);
    } else if (topBlock != null && topBlock.solid) {
      shadowMapIndex = (byte) (shadowMapIndex | SHADOW_TOP);
    } else if (topLeftBlock != null && topLeftBlock.solid) {
      shadowMapIndex = (byte) (shadowMapIndex | SHADOW_CORNER_BOTTOM_LEFT);
    }
  }
  
  public boolean isShadowMap(byte cs) {
    return (shadowMapIndex & cs) != 0;
  }
}
