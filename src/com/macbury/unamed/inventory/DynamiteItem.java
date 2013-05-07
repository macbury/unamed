package com.macbury.unamed.inventory;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import com.macbury.unamed.SoundManager;
import com.macbury.unamed.entity.Dynamite;
import com.macbury.unamed.level.Level;

public class DynamiteItem extends InventoryItem {

  public DynamiteItem() {
    super();
    this.setItemType(InventoryItemType.Item);
  }
  
  @Override
  public String getKey() {
    return "DynamiteItem";
  }

  @Override
  public String getName() {
    return "Dynamite";
  }

  @Override
  public boolean place(Vector2f tilePos) throws SlickException {
    if(haveItems()) {
      Dynamite dynamite = new Dynamite();
      Level.shared().addEntity(dynamite);
      
      dynamite.setTilePosition((int)tilePos.x, (int)tilePos.y);
      SoundManager.shared().placeBlockSound.playAsSoundEffect(1.0f, 1.0f, false);
      
      this.popItem();
      return true;
    } else {
     return false; 
    }
  }

  @Override
  public int harvestPower() {
    return 1;
  }

}
