package com.macbury.unamed.attack;

import org.newdawn.slick.SlickException;

import com.macbury.unamed.AnimationManager;
import com.macbury.unamed.SoundManager;
import com.macbury.unamed.combat.Damage;
import com.macbury.unamed.component.TileBasedMovement;
import com.macbury.unamed.entity.AnimationEntity;
import com.macbury.unamed.entity.Entity;
import com.macbury.unamed.level.Level;

public class SwordAttack extends AttackBase {
  
  public SwordAttack() {
    super();
  }
  
  @Override
  protected void onAttack(Entity hunter, Entity prey) throws SlickException {
    AnimationEntity entity = (AnimationEntity) Level.shared().getUsedEntity(AnimationEntity.class);
    entity.setAnimation(AnimationManager.shared().swordAnimation);
    Level.shared().addEntity(entity);
    entity.setTilePosition(prey.getTileX(), prey.getTileY());
    
    prey.getHealth().applyDamage(new Damage(getPower()));
    ((TileBasedMovement) hunter.getComponent(TileBasedMovement.class)).lookAt(prey);
    
    SoundManager.shared().playAt(prey.getTileX(), prey.getTileY(), SoundManager.shared().sword);
  }

}
