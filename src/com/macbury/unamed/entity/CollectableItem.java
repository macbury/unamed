package com.macbury.unamed.entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

import com.macbury.unamed.SoundManager;
import com.macbury.unamed.component.CollectableItemSprite;
import com.macbury.unamed.inventory.BlockItem;
import com.macbury.unamed.inventory.InventoryItem;
import com.macbury.unamed.inventory.InventoryManager;
import com.macbury.unamed.level.BlockResources;

public class CollectableItem extends Entity {
  public final static int LIFE_TIME = 15000;
  InventoryItem itemToCollect;
  int lifeTime = 0;
  private CollectableItemSprite imageSprite;
  
  public CollectableItem( InventoryItem item ) throws SlickException {
    super();
    
    this.collidable    = true;
    this.itemToCollect = item;
    
    this.visibleUnderTheFog = true;
    
    Image image = null;
    if (BlockItem.class.isInstance(item)) {
      BlockItem blockItem = (BlockItem) item;
      image = BlockResources.shared().imageForBlockClass(blockItem.blockType);
    } else {
      image = InventoryManager.shared().getImageForInventoryItem(item);
    }
    
    this.imageSprite    = new CollectableItemSprite(image);
    addComponent(imageSprite);
  }

  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
    super.update(gc, sb, delta);
    
    lifeTime += delta;
    if (lifeTime > LIFE_TIME) {
      destroy();
    }
  }

  @Override
  public void onCollideWith(Entity entity) throws SlickException {
    if (Player.class.isInstance(entity)) {
      loot();
    }
  }

  public void loot() throws SlickException {
    InventoryManager.shared().addItem(itemToCollect);
    SoundManager.shared().loot.playAsSoundEffect(1.0f, 1.0f, false);
    this.destroy();
  }
  
  
}
