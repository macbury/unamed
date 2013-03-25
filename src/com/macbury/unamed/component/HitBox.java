package com.macbury.unamed.component;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

public class HitBox extends Component {

  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) {
    
  }

  @Override
  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) {
    gr.setColor(new Color(255, 0, 0, 100));
    gr.fillRect(this.owner.getX(), this.owner.getY(), this.owner.getWidth(), this.owner.getHeight());
  }


}
