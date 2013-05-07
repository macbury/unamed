package com.macbury.unamed.inventory;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import com.macbury.unamed.attack.AttackBase;

public abstract class WeaponItem extends InventoryItem {
  public WeaponItem() {
    super();
    this.setItemType(InventoryItemType.Weapon);
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
    getAttack().update(delta);
  }
  
  public abstract AttackBase getAttack();

  public void reset() {
    getAttack().getAttackTimer().restart();
  }
  
  
}
