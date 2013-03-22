package com.macbury.unamed.entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import com.macbury.unamed.scenes.BaseScene;

public class ImageRenderComponent extends RenderComponent {
  private Image image;
  
  public ImageRenderComponent(String id, Image image) {
    super(id);
    this.setImage(image);
  }

  @Override
  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) {
    Rectangle rect = owner.getRect();
    float scale    = owner.getScale();
    
    image.draw(rect.getX(), rect.getY(), scale);
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
