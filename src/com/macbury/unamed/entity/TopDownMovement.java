package com.macbury.unamed.entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

public class TopDownMovement extends Component {
  float direction;
  float speed;
   
  public TopDownMovement( String id )
  {
      this.id = id;
  }
   
  public float getSpeed()
  {
      return speed;
  }
   
  public float getDirection()
  {
      return direction;
  }
   
  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) {
    
      float rotation     = owner.getRotation();
      float scale        = owner.getScale();
      Rectangle rect     = owner.getRect();
      
      
      float diff = 0.1f * delta;
      float x   = rect.getX();
      float y   = rect.getY();
      
      Input input = gc.getInput();
        
      if(input.isKeyDown(Input.KEY_DOWN)) {
        y += diff;
      }

      if(input.isKeyDown(Input.KEY_UP)) {
        y -= diff;
      }
       
      if(input.isKeyDown(Input.KEY_LEFT)) {
        x -= diff;
      }
      
      if(input.isKeyDown(Input.KEY_RIGHT)) {
        x += diff;
      }
      
      rect.setLocation(x, y);
      owner.setRect(rect);
      owner.setRotation(rotation);
      owner.setScale(scale);
  }

  @Override
  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) {
    // TODO Auto-generated method stub
    
  }

}
