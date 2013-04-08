package com.macbury.unamed.entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.macbury.unamed.inventory.InventoryItem;

public abstract class BlockEntity extends Entity {
  final static int HARVEST_TIMEOUT = 1000;
  protected int hardness           = 1;
  private   int harvestTimeout     = HARVEST_TIMEOUT;
  protected boolean isHarvesting     = false;
  public abstract boolean use();
  public abstract int getHardness();
  public abstract InventoryItem harvestedByPlayer(Player byPlayer);
  
  public InventoryItem harvest(int power, Player byPlayer) throws SlickException {
    if (!isHarvesting) {
      hardness       = getHardness();
    }
    isHarvesting   = true;
    harvestTimeout = HARVEST_TIMEOUT;
    hardness       -= power;
    
    if (hardness <= 0) {
      InventoryItem item        = harvestedByPlayer(byPlayer);
      if (item != null) {
        CollectableItem spawnItem = new CollectableItem(item);
        this.getLevel().addEntity(spawnItem);
        spawnItem.setTileX(this.getTileX());
        spawnItem.setTileY(this.getTileY());
      }
      return item;
    } else {
      return null;
    }
  }
  
  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
    if (isHarvesting) {
      if (harvestTimeout <= 0) {
        hardness       = getHardness(); 
        harvestTimeout = 0;
        isHarvesting   = false;
      } else {
        harvestTimeout -= delta;
      }
    }

    super.update(gc, sb, delta);
  }

}
