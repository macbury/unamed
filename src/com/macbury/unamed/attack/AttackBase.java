package com.macbury.unamed.attack;

import org.json.simple.JSONObject;
import org.newdawn.slick.SlickException;

import com.macbury.unamed.Timer;
import com.macbury.unamed.TimerInterface;
import com.macbury.unamed.entity.Entity;

public abstract class AttackBase implements TimerInterface, Comparable<AttackBase> {
  private Timer attackTimer;
  private short power;
  private short distance;
  private boolean canAttack = false;
  
  public AttackBase() {
    attackTimer = new Timer();
    attackTimer.setDelegate(this);
    attackTimer.setEnabled(true);
    this.distance = 1;
    attackTimer.setTime(300);
    attackTimer.restart();
    attackTimer.setIsPausableEvent(true);
  }
  
  public void update(int delta) throws SlickException {
    attackTimer.update(delta);
  }

  @Override
  public void onTimerFire(Timer timer) throws SlickException {
    this.canAttack  = true;
    timer.stop();
  }

  public boolean attack(Entity hunter, Entity prey) throws SlickException {
    if (this.canAttack) {
      this.onAttack(hunter, prey);
      attackTimer.start();
      this.canAttack = false;
      return true;
    } else {
      return false;
    }
  }
  
  abstract protected void onAttack(Entity hunter, Entity prey) throws SlickException;

  public Timer getAttackTimer() {
    return attackTimer;
  }

  public void setAttackTimer(Timer attackTimer) {
    this.attackTimer = attackTimer;
  }
 

  public short getPower() {
    return power;
  }

  public void setPower(short power) {
    this.power = power;
  }

  public void setConfig(JSONObject attackJSON) {
    long pow  = (long) attackJSON.get("power");
    long dist = (long) attackJSON.get("distance");
    setDistance((short) dist);
    setPower((short) pow);
    setAttackSpeed((long) attackJSON.get("speed"));
  }

  public short getDistance() {
    return distance;
  }

  public void setDistance(short distance) {
    this.distance = distance;
  }
  
  public void setAttackSpeed(long time) {
    setAttackSpeed((short)time);
  }
  
  public void setAttackSpeed(short time) {
    attackTimer.setTime(time);
    attackTimer.restart();
  }
  
  @Override
  public int compareTo(AttackBase attack) {
    if (attack.getDistance() == this.getDistance()) {
      return 0;
    } else {
      if (attack.getDistance() > this.getDistance()) {
        return -1;
      } else {
        return 1;
      }
    }
  }
}
