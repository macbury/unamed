package com.macbury.unamed.combat;

public class Damage {
  private short power;
  private short lifeTime = 0;
  public Damage(short hitPoints) {
    this.setPower(hitPoints);
  }

  public Damage(int hitPoints) {
    this.setPower((short)hitPoints);
  }

  public short getPower() {
    return power;
  }

  public void setPower(short power) {
    this.power = power;
  }

  public short getLifeTime() {
    return lifeTime;
  }

  public void setLifeTime(short lifeTime) {
    this.lifeTime = lifeTime;
  }

  public void addPower(short pow) {
    this.power += pow;
  }
}
