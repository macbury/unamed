package com.macbury.unamed.component;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.util.pathfinding.Path;

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
  public final static String NAME          = "TileBasedMovement"; 
  public  Direction  direction             = Direction.Down;
  public  float speed                      = 0.0035f;
  private float totalMoveTime              = 0.0f;
  private Timer lavaDamageTimer            = null;
  private boolean moveInProgress = false;
  private Position basePosition;
  private float blockMoveSpeed;
  public boolean playSoundForStep          = true;
  private ArrayList<TileBasedMovementCallback> callbacks;
  
  public TileBasedMovement() {
    lavaDamageTimer = new Timer(Lava.APPLY_DAMAGE_EVERY_MILISECONDS, this);
    lavaDamageTimer.setIsPausableEvent(true);
    this.callbacks = new ArrayList<TileBasedMovementCallback>();
  }
  
  public boolean isMoving() {
    return moveInProgress;
  }
  
  public Position computeTargetPositionForDirection(Direction inDirection) {
    float x             = this.owner.getX();
    float y             = this.owner.getY();

    
    switch (inDirection) {
      case Down:
        y += Core.TILE_SIZE;
      break;
  
      case Top:
        y -= Core.TILE_SIZE;
      break;
      
      case Left:
        x -= Core.TILE_SIZE;
      break;
      
      case Right:
        x += Core.TILE_SIZE;
      break;
      
      default:
        
      break;
    }
  
    return new Position(x,y);
  }
  
  public Rectangle computeTargetRectForDirection(Direction inDirection) {
    Position pos = computeTargetPositionForDirection(inDirection);
    return new Rectangle(pos.getX(), pos.getY(), this.owner.getWidth() - 1, this.owner.getHeight() -1);
  }
  
  public boolean move(Direction inDirection) {
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
        
        for (TileBasedMovementCallback callback : this.callbacks) {
          callback.onFinishMovement();
        }
      }
    }
    
    lavaDamageTimer.update(delta);
    lavaDamageTimer.setEnabled(this.owner.getBlock().isLava());
  }

  @Override
  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) {
    
  }

  public void lookIn(Direction inDirection) {
    this.direction      = inDirection;
  }

  public void moveInRandomDirection() {
    move(Direction.random());
  }

  public Direction getDirection() {
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
  
  public void lookAt(int x, int y) {
    Vector2f direction  = (new Vector2f(x - this.owner.getTileX(), y - this.owner.getTileY())).normalise();
    //Log.info(direction.toString());
    if (direction.getX() > 0.0f) {
      this.direction = Direction.Right;
    } else if (direction.getX() < 0.0f) {
      this.direction = Direction.Left;
    } else if (direction.getY() < 0.0f) {
      this.direction = Direction.Top;
    } else {
      this.direction = Direction.Down;
    }
  }
  
  public void lookAt(Entity entity) {
    lookAt(entity.getTileX(), entity.getTileY());
  }

  public boolean moveForward() {
    if (this.owner.getLevel().canMoveTo(this.computeTargetRectForDirection(this.direction), this.owner)) {
      this.move(this.direction);
      return true;
    } else {
      return false;
    }
  }
  
  public void registerListener(TileBasedMovementCallback callback) throws SlickException {
    if (this.callbacks.contains(callback)) {
      throw new SlickException("Cannot duplicate callbacks: " + callback.getClass().getSimpleName());
    }
    this.callbacks.add(callback);
  }
}
