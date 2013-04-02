package com.macbury.unamed.level;

import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;



public class Sidewalk extends Block {
  public Shadow shadowMapIndex = Shadow.SHADOW_NEED_TO_REFRESH;
  public Sidewalk(int x, int y) {
    super(x,y);
    this.solid = false;
  }
  
  public boolean shouldRefreshShadowMap() {
    return this.shadowMapIndex == Shadow.SHADOW_NEED_TO_REFRESH;
  }
  
  public void refreshShadowMap() {
    this.shadowMapIndex = Shadow.SHADOW_NEED_TO_REFRESH;
  }
  
  public Image getCurrentShadowMap(SpriteSheet sheet) {
    if (this.shadowMapIndex == Shadow.SHADOW_TOP) {
      return sheet.getSprite(0, 0); 
    } else if (this.shadowMapIndex == Shadow.SHADOW_CORNER_TOP_LEFT) {
      return sheet.getSprite(0, 5);
    } else if (this.shadowMapIndex == Shadow.SHADOW_LEFT) {
      return sheet.getSprite(0, 3);
    } else if (this.shadowMapIndex == Shadow.SHADOW_CORNER_BOTTOM_LEFT) {
      return sheet.getSprite(0, 2);
    } else if (this.shadowMapIndex == Shadow.SHADOW_LEFT_ENDING) {
      return sheet.getSprite(0, 4);
    } else if (this.shadowMapIndex == Shadow.SHADOW_TOP_ENDING) {
      return sheet.getSprite(0, 1);
    }
    return null; //sheet.getSprite(0, 5);
  }

  public void computeShadowMapBasedOnLevel(Level level) {
    Block leftBlock         = level.getBlockForPosition(this.x - 1, this.y);
    Block topLeftBlock      = level.getBlockForPosition(this.x - 1, this.y - 1);
    Block topBlock          = level.getBlockForPosition(this.x, this.y - 1);
    
    shadowMapIndex = Shadow.SHADOW_NEED_TO_REFRESH;
    if (topBlock != null && topBlock.solid) {
      if (leftBlock != null && leftBlock.solid) {
        shadowMapIndex = Shadow.SHADOW_CORNER_TOP_LEFT;
      } else {
        if (topLeftBlock != null && !topLeftBlock.solid) {
          shadowMapIndex = Shadow.SHADOW_TOP_ENDING;
        } else {
          shadowMapIndex = Shadow.SHADOW_TOP;
        }
      }
    } else if (leftBlock != null && leftBlock.solid) {
      if ((topLeftBlock != null && topLeftBlock.solid)) {
        shadowMapIndex = Shadow.SHADOW_LEFT;
      } else {
        shadowMapIndex = Shadow.SHADOW_LEFT_ENDING;
      }
    } else if (topLeftBlock != null && topLeftBlock.solid) {
      shadowMapIndex = Shadow.SHADOW_CORNER_BOTTOM_LEFT;
    }
  }
  
  public boolean isShadowMap(Shadow mask) {
    return shadowMapIndex == mask;
  }
}
