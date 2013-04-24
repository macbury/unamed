package com.macbury.unamed;

import java.util.ArrayList;
import java.util.Collection;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class FpsGraph extends ArrayList<Float> implements TimerInterface {
  public static final int MAX_STORED_VALUES = 240;
  private static final short PUSH_EVERY_MILI = 50;
  private static final float PADDING = 10;
  private static final int BAR_HEIGHT = 60;
  private static final Color COLOR = new Color(255, 255, 255, 100);
  private Timer timer;
  
  public FpsGraph() {
    this.timer = new Timer(PUSH_EVERY_MILI, this);
    this.timer.start();
  }

  public void push(Float fps) {
    if (this.size() > MAX_STORED_VALUES) {
      this.remove(0);
    }
    this.add(fps);
  }
  
  public void update(int delta) throws SlickException {
    this.timer.update(delta);
  }

  @Override
  public void onTimerFire(Timer timer) {
    this.push((float)Core.instance().getContainer().getFPS()/Core.MAX_FPS);
  }

  public void render(Graphics gr) throws SlickException {
    gr.pushTransform();
    gr.translate(PADDING, Core.WINDOW_HEIGHT - PADDING - BAR_HEIGHT);
    gr.setColor(COLOR);
    for (int i = 0; i < this.size(); i++) {
      Float fps = this.get(i) * BAR_HEIGHT;
      gr.drawLine(i, BAR_HEIGHT - fps, i, BAR_HEIGHT);
      
    }
    
    Core.instance().getFont().drawString(5, BAR_HEIGHT - 25, "FPS: "+ Core.instance().getContainer().getFPS());
    
    gr.popTransform();
  }
}
