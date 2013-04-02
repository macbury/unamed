package com.macbury.unamed.component;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class AnimatedSprite extends RenderComponent {
  public final static int SPRITE_TILE_WIDTH  = 32;
  public final static int SPRITE_TILE_HEIGHT = 32;
  public final static int ANIMATION_SPEED    = 150;
  private Animation currentAnimation = null;
  
  
  public AnimatedSprite(Image[] images) throws SlickException {
    currentAnimation = new Animation(images, ANIMATION_SPEED);
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
