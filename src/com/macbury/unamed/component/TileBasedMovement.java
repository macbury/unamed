package com.macbury.unamed.component;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

import com.macbury.unamed.Core;
import com.macbury.unamed.Position;
import com.macbury.unamed.SoundManager;
import com.macbury.unamed.Timer;
import com.macbury.unamed.TimerInterface;
import com.macbury.unamed.Util;
import com.macbury.unamed.entity.Entity;
import com.macbury.unamed.entity.Player;
import com.macbury.unamed.intefrace.InterfaceManager;
import com.macbury.unamed.level.Block;
import com.macbury.unamed.level.Lava;
import com.macbury.unamed.level.PassableBlock;

public class TileBasedMovement extends Component implements TimerInterface {
  public final static String NAME = "TileBasedMovement"; 
  public final static byte DIRECTION_LEFT  = 0;
  public final static byte DIRECTION_RIGHT = 1;
  public final static byte DIRECTION_TOP   = 2;
  public final static byte DIRECTION_DOWN  = 3;
  public final static byte DIRECTION_NONE  = 4;
  public  byte  direction                  = DIRECTION_DOWN;
  public  float speed                      = 0.0035f;
  private float totalMoveTime              = 0.0f;
  private Timer lavaDamageTimer            = null;
  private boolean moveInProgress = false;
  private Position basePosition;
  private float blockMoveSpeed;
  public boolean playSoundForStep          = true;
  
  public TileBasedMovement() {
    lavaDamageTimer = new Timer(Lava.APPLY_DAMAGE_EVERY_MILISECONDS, this);
    lavaDamageTimer.setIsPausableEvent(true);
  }
  
  public boolean isMoving() {
    return moveInProgress;
  }
  
  public Position computeTargetPositionForDirection(byte inDirection) {
    float x             = this.owner.getX();
    float y             = this.owner.getY();
    
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
  
    return new Position(x,y);
  }
  
  public Rectangle computeTargetRectForDirection(byte inDirection) {
    Position pos = computeTargetPositionForDirection(inDirection);
    return new Rectangle(pos.getX(), pos.getY(), this.owner.getWidth() - 1, this.owner.getHeight() -1);
  }
  
  public boolean move(byte inDirection) {
    if(this.isMoving()) {
      return false;
    } else {
      this.lookIn(inDirection);
      this.basePosition   = new Position(this.owner.getX(), this.owner.getY());
      this.moveInProgress = true;
      this.totalMoveTime  = 0.0f;
      this.owner.setFuturePosition(computeTargetPositionForDirection(inDirection));
     
      Block block = (Block) this.owner.getLevel().getBlockForPosition(this.owner.getTileX(), this.owner.getTileY());
      
      if (block.isPassable()) {
        PassableBlock passableBlock = (PassableBlock) block;
        this.blockMoveSpeed = passableBlock.getSpeed();
        if (playSoundForStep) {
          SoundManager.shared().playStepForBlock(block, this.owner.getTileX(), this.owner.getTileY());
        }
      }
      
      return true;
    }
  }
  
  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
    if (this.isMoving() && !InterfaceManager.shared().shouldBlockGamePlay()) {
      totalMoveTime += speed * (float)delta * blockMoveSpeed;
      float x = Math.round(Util.lerp(basePosition.getX(), this.owner.getFuturePosition().getX(), totalMoveTime));
      float y = Math.round(Util.lerp(basePosition.getY(), this.owner.getFuturePosition().getY(), totalMoveTime));
      this.owner.setX(x);
      this.owner.setY(y);
      
      if (this.totalMoveTime > 1.0) {
        moveInProgress = false;
        totalMoveTime  = 0.0f;
        this.owner.setTileX(Math.round(this.owner.getFuturePosition().getX()/Core.TILE_SIZE));
        this.owner.setTileY(Math.round(this.owner.getFuturePosition().getY()/Core.TILE_SIZE));
        this.owner.setFuturePosition(null);
        basePosition   = null;
        if (this.owner.haveLight()) {
          this.owner.getLight().updateLight();
        }
      }
    }
    
    lavaDamageTimer.update(delta);
    lavaDamageTimer.setEnabled(this.owner.getBlock().isLava());
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

  public byte getDirection() {
    return this.direction;
  }

  @Override
  public void onTimerFire(Timer timer) {
    if (timer == lavaDamageTimer) {
      if (this.owner.haveHealth()) {
        Block block = this.owner.getBlock();
        
        if (Lava.class.isInstance(block)) {
          Lava lavaBlock = (Lava)block;
          
          this.owner.getHealth().applyDamage(lavaBlock.getDamage());
        }
      }
    }
  }

  public void lookAt(Entity entity) {
    Vector2f direction  = (new Vector2f(entity.getTileX() - this.owner.getTileX(), entity.getTileY() - this.owner.getTileY())).normalise();
    //Log.info(direction.toString());
    if (direction.getX() > 0.0f) {
      this.direction = DIRECTION_RIGHT;
    } else if (direction.getX() < 0.0f) {
      this.direction = DIRECTION_LEFT;
    } else if (direction.getY() < 0.0f) {
      this.direction = DIRECTION_TOP;
    } else {
      this.direction = DIRECTION_DOWN;
    }
  }
}
