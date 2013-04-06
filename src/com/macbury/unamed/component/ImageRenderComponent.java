package com.macbury.unamed.component;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;


public class ImageRenderComponent extends RenderComponent {
  private Image image;
  
  public ImageRenderComponent(Image image) {
    this.setImage(image);
  }

  @Override
  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) {
    //float scale    = owner.getScale();
    
    gr.drawImage(image, owner.getX(), owner.getY());
    //image.draw(owner.getX(), owner.getY(), scale);
  }
  
  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) {
    Rectangle rect = this.owner.getRect();
    rect.setWidth(this.image.getWidth());
    rect.setHeight(this.image.getHeight());
    
    this.owner.setRect(rect);
  }

  public Image getImage() {
    return image;
  }

  public void setImage(Image image) {
    this.image = image;
  }
}
