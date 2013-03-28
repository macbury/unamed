package com.macbury.unamed.component;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.state.StateBasedGame;

public class AnimatedSprite extends RenderComponent {
  public final static int SPRITE_TILE_WIDTH  = 32;
  public final static int SPRITE_TILE_HEIGHT = 32;
  public final static int ANIMATION_SPEED    = 150;
  private SpriteSheet spriteSheet;
  private Animation currentAnimation = null;
  
  
  public AnimatedSprite(String spriteName) throws SlickException {
    spriteSheet      = new SpriteSheet("res/images/"+spriteName+".png", SPRITE_TILE_WIDTH, SPRITE_TILE_HEIGHT);
    currentAnimation = new Animation(new Image[] { spriteSheet.getSprite(0, 0), spriteSheet.getSprite(1, 0), spriteSheet.getSprite(2, 0) }, ANIMATION_SPEED);
    currentAnimation.setAutoUpdate(false);
  }
  
  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta)
      throws SlickException {
    currentAnimation.update(delta);
  }

  @Override
  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) {
    if (currentAnimation != null) {
      currentAnimation.draw(owner.getX(), owner.getY());
    }
  }

}
