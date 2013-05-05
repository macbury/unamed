package com.macbury.unamed.inventory;

import com.macbury.unamed.attack.AttackBase;
import com.macbury.unamed.attack.SwordAttack;

public class RockSwordItem extends WeaponItem {

  private static final short SWORD_POWER = 4;
  private static final long SWORD_SPEED  = 600;
  private SwordAttack attack;

  public RockSwordItem() {
    attack = new SwordAttack();
    attack.setPower(SWORD_POWER);
    attack.setAttackSpeed(SWORD_SPEED);
  }

  @Override
  public String getKey() {
    return "ROCK_SWORD_ITEM";
  }

  @Override
  public String getName() {
    return "Rock Sword";
  }

  @Override
  public AttackBase getAttack() {
    return attack;
  }

}
