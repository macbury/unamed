package com.macbury.unamed.attack;

import org.newdawn.slick.SlickException;

import com.macbury.unamed.AnimationManager;
import com.macbury.unamed.SoundManager;
import com.macbury.unamed.combat.Damage;
import com.macbury.unamed.component.TileBasedMovement;
import com.macbury.unamed.entity.AnimationEntity;
import com.macbury.unamed.entity.Entity;
import com.macbury.unamed.level.Level;

public class PunchAttack extends AttackBase {

  public static final short PUNCH_ATTACK = 2;
  public static final short PUNCH_SPEED  = 350;

  public PunchAttack() {
    super();
    setPower(PUNCH_ATTACK);
    setAttackSpeed(PUNCH_SPEED);
  }
  
  @Override
  protected void onAttack(Entity hunter, Entity prey) throws SlickException {
    AnimationEntity entity = (AnimationEntity) Level.shared().getUsedEntity(AnimationEntity.class);
    entity.setAnimation(AnimationManager.shared().punchAnimation);
    Level.shared().addEntity(entity);
    entity.setTilePosition(prey.getTileX(), prey.getTileY());
    
    prey.getHealth().applyDamage(new Damage(getPower()));
    ((TileBasedMovement) hunter.getComponent(TileBasedMovement.class)).lookAt(prey);
    
    SoundManager.shared().playAt(prey.getTileX(), prey.getTileY(), SoundManager.shared().blowSound);
  }

}
