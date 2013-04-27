package com.macbury.unamed.ai;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.util.pathfinding.Path;

import com.macbury.unamed.AnimationManager;
import com.macbury.unamed.PathFindingCallback;
import com.macbury.unamed.PathFindingQueue;
import com.macbury.unamed.Position;
import com.macbury.unamed.SoundManager;
import com.macbury.unamed.Timer;
import com.macbury.unamed.TimerInterface;
import com.macbury.unamed.combat.Damage;
import com.macbury.unamed.component.TileFollowCallback;
import com.macbury.unamed.entity.AnimationEntity;
import com.macbury.unamed.entity.DigEffectEntity;
import com.macbury.unamed.inventory.InventoryManager;
import com.macbury.unamed.level.Level;
import com.macbury.unamed.util.MonsterManager;

public class HostileWanderAI extends WanderAI implements TimerInterface {
  private static final short LOOK_LOOP_TIME = 100;
  private static final int MIN_DISTANCE_TO_ATTACK = 1;
  Timer lookIfICanSeePlayerTimer;
  Timer attackTimer;
  private Path pathToLastSeenTargetPosition;
  private short attackPower;
  
  public HostileWanderAI() {
    super();
    lookIfICanSeePlayerTimer = new Timer(LOOK_LOOP_TIME, this);
    lookIfICanSeePlayerTimer.setEnabled(true);
    attackTimer = new Timer((short)1000, this);
    attackTimer.setEnabled(false);
    attackPower = 5;
  }
  
  @Override
  public void update(int delta) throws SlickException {
    lookIfICanSeePlayerTimer.update(delta);
    attackTimer.update(delta);
    super.update(delta);
    
    switch(getState()) {
      
      case CHECK_PLAYER_LAST_POSITION:
        if (pathToLastSeenTargetPosition != null && !this.tileMovement.isMoving()) {
          this.tileFollowPath.followPath(this.pathToLastSeenTargetPosition);
          pathToLastSeenTargetPosition = null;
        }
      break;
    
      case ATTACK:
        if (this.getOwner().distanceTo(this.getTarget()) > MIN_DISTANCE_TO_ATTACK) {
          this.setState(State.TARGET_PLAYER);
        }
      break;
    
      case TARGET_PLAYER:
        if (!this.tileMovement.isMoving()) {
          if (this.getOwner().distanceTo(this.getTarget()) > MIN_DISTANCE_TO_ATTACK) {
            this.tileMovement.lookAt(this.getTarget());
            if (!this.tileMovement.moveForward()) {
              this.setState(State.WANDERING);
            }
          } else {
            this.setState(State.ATTACK);
          }
        }
        
      break;
    
      default:
        
      break;
    }
  }
  
  @Override
  protected void onStateTransition(State old, State next) throws SlickException {
    super.onStateTransition(old, next);
    Log.info("Switching from state: " + old + " to " + next);
    
    if (next == State.ATTACK) {
      attackTimer.startAndFire();
    }
    
    if (old == State.ATTACK) {
      attackTimer.stop();
    }
    
    if (next == State.WANDERING) {
      this.randomMovement.enabled = true;
    }
    
    if (old == State.WANDERING) {
      this.randomMovement.enabled = false;
    }
  }

  @Override
  public void onStart() throws SlickException {
    super.onStart();
    lookIfICanSeePlayerTimer.start();
    attackTimer.stop();
    this.setState(State.WANDERING);
  }

  @Override
  public void onStop() throws SlickException {
    super.onStop();
    lookIfICanSeePlayerTimer.stop();
    attackTimer.stop();
  }

  public void checkIfISee() throws SlickException {
    if (canISeePlayer()) {
      this.setState(State.TARGET_PLAYER);
      setTarget(Level.shared().getPlayer());
    } else {
      this.setState(State.WANDERING);
      
      setTarget(null);
    }
  }
  
  @Override
  public void onTimerFire(Timer timer) throws SlickException {
    if (lookIfICanSeePlayerTimer == timer && (getState() == State.WANDERING || getState() == State.TARGET_PLAYER || getState() == State.WANDERING)) {
      checkIfISee();
    } else if (timer == attackTimer) {
      if (this.getOwner().distanceTo(this.getTarget()) > MIN_DISTANCE_TO_ATTACK) {
        this.setState(State.TARGET_PLAYER);
      } else {
        attack();
      }
    }
  }

  private void attack() throws SlickException {
    AnimationEntity entity = (AnimationEntity) Level.shared().getUsedEntity(AnimationEntity.class);
    entity.setAnimation(AnimationManager.shared().biteAnimation);
    this.getOwner().getLevel().addEntity(entity);
    entity.setTilePosition(this.getTarget().getTileX(), this.getTarget().getTileY());
    
    this.getTarget().getHealth().applyDamage(new Damage(this.attackPower));
    this.tileMovement.lookAt(this.getTarget());
    
    SoundManager.shared().playAt(this.getTarget().getTileX(), this.getTarget().getTileY(), SoundManager.shared().bite);
  }

}
