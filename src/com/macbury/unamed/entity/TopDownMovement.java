package com.macbury.unamed.entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
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
    
      float rotation = owner.getRotation();
      float scale = owner.getScale();
      Vector2f position = owner.getPosition();
               
      Input input = gc.getInput();
        
      if(input.isKeyDown(Input.KEY_DOWN))
      {
          rotation += -0.2f * delta;
      }

      if(input.isKeyDown(Input.KEY_UP))
      {
          rotation += 0.2f * delta;
      }
       
      if(input.isKeyDown(Input.KEY_LEFT))
      {
          float hip = 0.4f * delta;

          position.x += hip * java.lang.Math.sin(java.lang.Math.toRadians(rotation));
          position.y -= hip *java.lang.Math.cos(java.lang.Math.toRadians(rotation));
      }

      owner.setPosition(position);
      owner.setRotation(rotation);
      owner.setScale(scale);
  }

  @Override
  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) {
    // TODO Auto-generated method stub
    
  }

}
