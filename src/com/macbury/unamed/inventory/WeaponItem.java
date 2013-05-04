package com.macbury.unamed.inventory;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import com.macbury.unamed.attack.AttackBase;

public abstract class WeaponItem extends InventoryItem {
  private AttackBase attack;
  
  public WeaponItem() {
    // TODO Auto-generated constructor stub
  }

  @Override
  public abstract String getKey();

  @Override
  public abstract String getName();

  @Override
  public boolean place(Vector2f tilePos) throws SlickException {
    return false;
  }

  @Override
  public int harvestPower() {
    return 1;
  }
  
  public void update(int delta) throws SlickException {
    attack.update(delta);
  }
  
  public AttackBase getAttack() {
    return attack;
  }

  public void setAttack(AttackBase attack) {
    this.attack = attack;
  }

  public void reset() {
    this.attack.getAttackTimer().restart();
  }
  
  
}
