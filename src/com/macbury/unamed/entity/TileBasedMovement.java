package com.macbury.unamed.entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import com.macbury.unamed.Core;
import com.macbury.unamed.Util;

public class TileBasedMovement extends Component {
  public final static String NAME = "TileBasedMovement"; 
  public final static byte DIRECTION_LEFT  = 0;
  public final static byte DIRECTION_RIGHT = 1;
  public final static byte DIRECTION_TOP   = 2;
  public final static byte DIRECTION_DOWN  = 3;
  public  byte  direction           = DIRECTION_DOWN;
  public  float speed               = 0.0035f;
  private float totalMoveTime       = 0.0f;
  
  private boolean moveInProgress = false;
  private Vector2f targetPosition;
  private Vector2f basePosition;
  
  public TileBasedMovement(String id) {
    super(id);
  }
  
  public boolean isMoving() {
    return moveInProgress;
  }
  
  public boolean move(byte inDirection) {
    if(this.isMoving()) {
      return false;
    } else {
      float x             = this.owner.getX();
      float y             = this.owner.getY();
      
      this.direction      = inDirection;
      this.basePosition   = new Vector2f(x, y);
      this.moveInProgress = true;
      this.totalMoveTime  = 0.0f;
      
      switch (inDirection) {
        case DIRECTION_DOWN:
          y += Core.TILE_SIZE;
        break;

        case DIRECTION_TOP:
          y -= Core.TILE_SIZE;
        break;
        
        case DIRECTION_LEFT:
          x -= Core.TILE_SIZE;
        break;
        
        case DIRECTION_RIGHT:
          x += Core.TILE_SIZE;
        break;
        
        default:
          
        break;
      }
      
      this.targetPosition = new Vector2f(x, y);
      
      return true;
    }
  }
  
  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) {
    if (this.isMoving()) {
      totalMoveTime += speed * (float)delta;
      float x = Util.lerp(basePosition.x, targetPosition.x, totalMoveTime);
      float y = Util.lerp(basePosition.y, targetPosition.y, totalMoveTime);
      this.owner.setX(x);
      this.owner.setY(y);
      
      if (this.totalMoveTime > 1.0) {
        moveInProgress = false;
        totalMoveTime  = 0.0f;
        targetPosition = null;
        basePosition   = null;
      }
    }
  }

  @Override
  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) {
    
  }
}
