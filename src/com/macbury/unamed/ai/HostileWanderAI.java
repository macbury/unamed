package com.macbury.unamed.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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
import com.macbury.unamed.attack.AttackBase;
import com.macbury.unamed.combat.Damage;
import com.macbury.unamed.component.TileFollowCallback;
import com.macbury.unamed.entity.AnimationEntity;
import com.macbury.unamed.entity.DigEffectEntity;
import com.macbury.unamed.entity.Entity;
import com.macbury.unamed.inventory.InventoryManager;
import com.macbury.unamed.level.Level;
import com.macbury.unamed.util.MonsterManager;

public class HostileWanderAI extends WanderAI implements TimerInterface {
  private static final short LOOK_LOOP_TIME = 100;
  Timer lookIfICanSeePlayerTimer;
  private ArrayList<AttackBase> attacks;
  private int minAttackDistance;
  private Short currentDistanceToTarget;
  private Position lastTargetPosition;
  
  public HostileWanderAI() {
    super();
    attacks = new ArrayList<AttackBase>();
    lookIfICanSeePlayerTimer = new Timer(LOOK_LOOP_TIME, this);
    lookIfICanSeePlayerTimer.setEnabled(true);
  }
  
  @Override
  public void update(int delta) throws SlickException {
    lookIfICanSeePlayerTimer.update(delta);
    super.update(delta);
    currentDistanceToTarget = null;
    for (AttackBase attack : this.attacks) {
      attack.update(delta);
    }
    switch(getState()) {
      
      case CHECK_PLAYER_LAST_POSITION:
        if (lastTargetPosition != null) {
          this.tileMovement.lookAt(lastTargetPosition.getTileX(), lastTargetPosition.getTileY());
          if (!this.tileMovement.moveForward() || (this.getOwner().getTileX() == lastTargetPosition.getTileX() && this.getOwner().getTileY() == lastTargetPosition.getTileY())) {
            this.setState(State.WANDERING);
          }
        } else {
          this.setState(State.WANDERING);
        }
      break;
    
      case ATTACK:
        AttackBase attack = getBestAttackForCurrentDistance();
        if (attack == null) {
          this.setState(State.WANDERING);
        } else {
          attack.attack(this.getOwner(), this.getTarget());
          this.setState(State.TARGET_PLAYER);
        }
      break;
    
      case TARGET_PLAYER:
        if (!this.tileMovement.isMoving()) {
          if (distanceToTarget() > minAttackDistance) {
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
  

  private AttackBase getBestAttackForCurrentDistance() {
    for (AttackBase attack : this.attacks) {
      if (attack.getDistance() <= distanceToTarget()) {
        return attack;
      }
    }
    return null;
  }

  private Short distanceToTarget() {
    if (currentDistanceToTarget == null) {
      currentDistanceToTarget = (short) this.getOwner().distanceTo(this.getTarget());
    }
    return currentDistanceToTarget;
  }

  @Override
  protected void onStateTransition(State old, State next) throws SlickException {
    super.onStateTransition(old, next);
    //Core.log(this.getClass(),"Switching from state: " + old + " to " + next);
    
    if (next == State.CHECK_PLAYER_LAST_POSITION) {
      //this.tileFollowPath.reset();
      //this.tileFollowPath.enabled = true;
    }
    
    if (old == State.CHECK_PLAYER_LAST_POSITION) {
      //this.tileFollowPath.reset();
      //this.tileFollowPath.enabled = false;
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
    this.setState(State.WANDERING);
  }

  @Override
  public void onStop() throws SlickException {
    super.onStop();
    lookIfICanSeePlayerTimer.stop();
  }

  public void checkIfISee() throws SlickException {
    if (canISeePlayer()) {
      this.setState(State.TARGET_PLAYER);
      setTarget(Level.shared().getPlayer());
    } else if (this.getState() != State.CHECK_PLAYER_LAST_POSITION) {
      if (this.getTarget() != null) {
        this.setState(State.CHECK_PLAYER_LAST_POSITION);
      } else {
        this.setState(State.WANDERING);
      }
      setTarget(null);
    }
  }
  
  @Override
  public void onTimerFire(Timer timer) throws SlickException {
    if (lookIfICanSeePlayerTimer == timer && (getState() == State.WANDERING || getState() == State.TARGET_PLAYER || getState() == State.WANDERING)) {
      checkIfISee();
    }
  }


  public void setConfig(JSONObject jsonObject) {
    JSONArray attacksJSON = (JSONArray) jsonObject.get("attacks");
    for (int i = 0; i < attacksJSON.size(); i++) {
      JSONObject attackJSON = (JSONObject) attacksJSON.get(i);
      String type     = (String) attackJSON.get("type");
      Class<?> klass;
      try {
        klass = AttackBase.class.forName("com.macbury.unamed.attack."+type);
        AttackBase attack = (AttackBase) klass.newInstance();
        attack.setConfig(attackJSON);
        attacks.add(attack);
      } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
        e.printStackTrace();
      }
    }
    
    checkMinimalAttackDistance();
  }

  private void checkMinimalAttackDistance() {
    this.minAttackDistance = 1;
    for (AttackBase attack : this.attacks) {
      this.minAttackDistance = Math.min((int)this.minAttackDistance, attack.getDistance());
    }
    Collections.sort(this.attacks);
  }
}
