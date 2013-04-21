package com.macbury.unamed;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class AnimationManager {
  private static AnimationManager sharedInstance;

  public static AnimationManager shared() throws SlickException {
    if (AnimationManager.sharedInstance == null) {
      AnimationManager.sharedInstance = new AnimationManager();
    }
    
    return AnimationManager.sharedInstance;
  }

  public Animation punchAnimation;
  public Animation swordAnimation;
  
  public AnimationManager() throws SlickException {
    this.punchAnimation = new Animation(ImagesManager.shared().getSpriteSheet("effects/Attack1.png", 32, 32), 100);
    this.punchAnimation.setAutoUpdate(false);
    
    SpriteSheet swordSpriteSheet = ImagesManager.shared().getSpriteSheet("effects/Sword1.png", 32, 32);
    this.swordAnimation = new Animation(new Image[] { 
        swordSpriteSheet.getSprite(0, 0), 
        swordSpriteSheet.getSprite(1, 0), 
        swordSpriteSheet.getSprite(2, 0), 
        swordSpriteSheet.getSprite(3, 0), 
        swordSpriteSheet.getSprite(4, 0), 
        swordSpriteSheet.getSprite(0, 1)
    }, 100);
  }
}
