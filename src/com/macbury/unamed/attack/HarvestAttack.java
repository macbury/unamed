package com.macbury.unamed.attack;

import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;

import com.macbury.unamed.SoundManager;
import com.macbury.unamed.entity.BlockEntity;
import com.macbury.unamed.entity.DigEffectEntity;
import com.macbury.unamed.entity.Entity;
import com.macbury.unamed.entity.Player;
import com.macbury.unamed.entity.TextParticle;
import com.macbury.unamed.inventory.InventoryItem;
import com.macbury.unamed.inventory.InventoryManager;
import com.macbury.unamed.level.Level;

public class HarvestAttack extends AttackBase {

  private static final Color COLOR_HARVEST = Color.white;

  @Override
  protected void onAttack(Entity hunter, Entity prey) throws SlickException {
    if (BlockEntity.class.isInstance(prey)) {
      harvest((Player)hunter, (BlockEntity)prey);
    } else {
      throw new SlickException("Must be Block entity: "+ prey.toString());
    }
  }

  private void harvest(Player player, BlockEntity usableEntity) throws SlickException {
    InventoryItem item = usableEntity.harvest(player.currentHarvestPower());
    
    TextParticle.spawnTextFor("-"+player.currentHarvestPower(), usableEntity, COLOR_HARVEST);

    DigEffectEntity punchEntity = (DigEffectEntity) Level.shared().getUsedEntity(DigEffectEntity.class);
    punchEntity.setAnimationByItem(InventoryManager.shared().getCurrentHotBarItem());
    Level.shared().addEntity(punchEntity);
    punchEntity.setTilePosition(usableEntity.getTileX(), usableEntity.getTileY());
    
    if (item == null) {
      SoundManager.shared().playDigForBlock(usableEntity.getBlock());
    } else {
      SoundManager.shared().pop.playAsSoundEffect(1.0f, 1.0f, false);
    }
  }

  
}
