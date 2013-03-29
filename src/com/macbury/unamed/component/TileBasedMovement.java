package com.macbury.unamed.component;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import com.macbury.unamed.Core;
import com.macbury.unamed.SoundManager;
import com.macbury.unamed.Util;

public class TileBasedMovement extends Component {
  public final static String NAME = "TileBasedMovement"; 
  public final static byte DIRECTION_LEFT  = 0;
  public final static byte DIRECTION_RIGHT = 1;
  public final static byte DIRECTION_TOP   = 2;
  public final static byte DIRECTION_DOWN  = 3;
  public final static byte DIRECTION_NONE  = 4;
  public  byte  direction           = DIRECTION_DOWN;
  public  float speed               = 0.0035f;
  private float totalMoveTime       = 0.0f;
  
  private boolean moveInProgress = false;
  private Vector2f basePosition;
  
  public boolean isMoving() {
    return moveInProgress;
  }
  
  public Vector2f computeTargetPositionForDirection(byte inDirection) {
    float x             = this.owner.getX();
    float y             = this.owner.getY();
    
    switch (inDirection) {
      case DIRECTION_DOWN:
        y += this.owner.getLevel().tileHeight;
      break;
  
      case DIRECTION_TOP:
        y -= this.owner.getLevel().tileHeight;
      break;
      
      case DIRECTION_LEFT:
        x -= this.owner.getLevel().tileWidth;
      break;
      
      case DIRECTION_RIGHT:
        x += this.owner.getLevel().tileWidth;
      break;
      
      default:
        
      break;
    }
  
    return new Vector2f(x,y);
  }
  
  public Rectangle computeTargetRectForDirection(byte inDirection) {
    Vector2f pos = computeTargetPositionForDirection(inDirection);
    return new Rectangle(pos.x, pos.y, this.owner.getWidth() - 1, this.owner.getHeight() -1);
  }
  
  public boolean move(byte inDirection) {
    if(this.isMoving()) {
      return false;
    } else {
      this.lookIn(inDirection);
      this.basePosition   = new Vector2f(this.owner.getX(), this.owner.getY());
      this.moveInProgress = true;
      this.totalMoveTime  = 0.0f;
      this.owner.setFuturePosition(computeTargetPositionForDirection(inDirection));
      SoundManager.shared().playStep();
      return true;
    }
  }
  
  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) {
    if (this.isMoving()) {
      totalMoveTime += speed * (float)delta;
      float x = Math.round(Util.lerp(basePosition.x, this.owner.getFuturePosition().x, totalMoveTime));
      float y = Math.round(Util.lerp(basePosition.y, this.owner.getFuturePosition().y, totalMoveTime));
      this.owner.setX(x);
      this.owner.setY(y);
      
      if (this.totalMoveTime > 1.0) {
        moveInProgress = false;
        totalMoveTime  = 0.0f;
        this.owner.setFuturePosition(null);
        basePosition   = null;
        if (this.owner.haveLight()) {
          this.owner.getLight().updateLight();
        }
      }
    }
  }

  @Override
  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) {
    
  }

  public void lookIn(byte inDirection) {
    this.direction      = inDirection;
  }
  
  public byte randomDirection() {
    return (byte) Math.round(Math.random() * DIRECTION_DOWN);
  }
  
  public void moveInRandomDirection() {
    move(randomDirection());
  }
}
