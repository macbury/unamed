package com.macbury.unamed.component;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class CollectableItemSprite extends RenderComponent {
  private Image image;
  
  float angle = 0.0f;
  final static float SPEED_ROTATION = 0.1f;
  public CollectableItemSprite(Image image) throws SlickException {
    this.image = image;
  }
  
  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
    angle += delta * SPEED_ROTATION;
  }

  @Override
  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) {
    if (image != null) {
      float scale = 0.5f;
      float oldRotation = image.getRotation();
      int width  = (int) (scale * this.owner.getWidth());
      int height = (int) (scale * this.owner.getHeight());
      image.setRotation(angle);
      image.draw(width/4, height/4, scale);
      image.setRotation(oldRotation);
    }
  }
}
