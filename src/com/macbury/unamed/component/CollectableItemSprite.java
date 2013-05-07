package com.macbury.unamed.component;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.macbury.unamed.level.Level;

public class CollectableItemSprite extends PositionBasedComponent {
  private Image image;
  
  float angle = 0.0f;

  private float zz;
  private float za;
  final static float SPEED_ROTATION = 0.1f;
  int direction = 1;
  private static final float SPEED = 0.025f;
  
  public CollectableItemSprite(Image image) throws SlickException {
    this.image = image;
    reset();
  }
  
  public void reset() {
    direction = 1;
  }

  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
    zz += delta * SPEED * direction;
    if (zz > 4.0f) {
      direction = -1;
    }
    
    if (zz <= 0.0f) {
      direction = 1;
    }
  }

  @Override
  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) {
    if (image != null) {
      float scale       = 0.5f;
      int width         = (int) (scale * this.owner.getWidth()) / 4;
      image.draw(width, 2 - Math.round(zz), scale);
      gr.setColor(Color.black);
      //gr.fillOval(0, 0, width, 12);
    }
  }
  
  public Image getImage() {
    return image;
  }

  public void setImage(Image image) {
    this.image = image;
  }

}
