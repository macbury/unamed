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
  }
  
  public void update(int delta) throws SlickException {
    attackTimer.update(delta);
  }

  @Override
  public void onTimerFire(Timer timer) throws SlickException {
    this.canAttack  = true;
    timer.stop();
  }

  public void attack(Entity hunter, Entity prey) throws SlickException {
    if (this.canAttack) {
      this.onAttack(hunter, prey);
      attackTimer.start();
      this.canAttack = false;
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
    attackTimer.setTime((long) attackJSON.get("speed"));
    attackTimer.restart();
  }

  public short getDistance() {
    return distance;
  }

  public void setDistance(short distance) {
    this.distance = distance;
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
