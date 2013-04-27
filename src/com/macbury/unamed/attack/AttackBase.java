package com.macbury.unamed.attack;

import org.newdawn.slick.SlickException;

import com.macbury.unamed.Timer;
import com.macbury.unamed.TimerInterface;
import com.macbury.unamed.entity.Entity;

public abstract class AttackBase implements TimerInterface {
  private Timer attackTimer;
  private short power;
  private boolean canAttack = false;
  
  private Entity hunter;
  private Entity prey;
  
  public AttackBase(Entity hunter, Entity prey) {
    attackTimer = new Timer();
    attackTimer.setDelegate(this);
    attackTimer.setEnabled(false);
  }
  
  public void update(int delta) throws SlickException {
    attackTimer.update(delta);
  }

  @Override
  public void onTimerFire(Timer timer) throws SlickException {
    this.canAttack  = true;
  }

  public void attack() {
    if (this.canAttack) {
      this.onAttack();
      this.canAttack = false;
    }
  }
  
  abstract protected void onAttack();

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

  public Entity getHunter() {
    return hunter;
  }

  public void setHunter(Entity hunter) {
    this.hunter = hunter;
  }

  public Entity getPrey() {
    return prey;
  }

  public void setPrey(Entity prey) {
    this.prey = prey;
  }
}
