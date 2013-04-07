package com.macbury.unamed.component;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Sprite extends RenderComponent {
  private Image image;
  
  public Sprite(Image image) throws SlickException {
    this.image = image;
  }
  
  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta)
      throws SlickException {
    // TODO Auto-generated method stub

  }

  @Override
  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) {
    if (image != null) {
      image.draw(0, 0);
    }
  }
  
  public void setImage(Image newImage) {
    this.image = newImage;
  }

}
