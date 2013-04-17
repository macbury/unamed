package com.macbury.unamed.intefrace;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;

import com.macbury.unamed.ImagesManager;

public class MessageBox extends Rectangle {
  private static final Color MESSAGE_BOX_COLOR = new Color(255, 255, 255, 100);
  private static final float MIN_CELL_SIZE     = 8;
  private static final float BACKGROUND_OFFSET = MIN_CELL_SIZE / 2;
  private static final Color BACKGROUND_COLOR  = new Color(55, 106, 240, 200);
  
  public MessageBox(float x, float y, float width, float height) {
    super(x, y, width, height);
  }

  public void draw(Graphics gr) throws SlickException {
    SpriteSheet window = ImagesManager.shared().windowSpriteSheet;
    gr.setColor(MESSAGE_BOX_COLOR);
    gr.pushTransform();
    gr.translate(this.getX(), this.getY());

    window.getSubImage(0, 0, 62, 62).draw(BACKGROUND_OFFSET, BACKGROUND_OFFSET, this.getWidth()-BACKGROUND_OFFSET*2, this.getHeight()-BACKGROUND_OFFSET*2, BACKGROUND_COLOR);
    window.getSprite(8, 0).draw();
    window.getSprite(9, 0).draw(MIN_CELL_SIZE, 0, this.getWidth() - MIN_CELL_SIZE*2, MIN_CELL_SIZE);
    window.getSprite(15, 0).draw(this.getWidth() - MIN_CELL_SIZE, 0);
    window.getSprite(8, 1).draw(0, MIN_CELL_SIZE, MIN_CELL_SIZE, this.getHeight() - MIN_CELL_SIZE*2 );
    window.getSprite(8, 7).draw(0, this.getHeight() - MIN_CELL_SIZE);
    window.getSprite(9, 7).draw(MIN_CELL_SIZE, this.getHeight() - MIN_CELL_SIZE, this.getWidth() - MIN_CELL_SIZE*2, MIN_CELL_SIZE);
    window.getSprite(15, 7).draw(this.getWidth() - MIN_CELL_SIZE, this.getHeight() - MIN_CELL_SIZE);
    window.getSprite(15, 1).draw(this.getWidth() - MIN_CELL_SIZE, MIN_CELL_SIZE, MIN_CELL_SIZE, this.getHeight() - MIN_CELL_SIZE*2 );
    gr.popTransform();
  }

}
