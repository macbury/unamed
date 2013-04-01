package com.macbury.unamed.inventory;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import com.macbury.unamed.SoundManager;
import com.macbury.unamed.entity.Entity;
import com.macbury.unamed.entity.Player;
import com.macbury.unamed.entity.Torch;

public class TorchItem extends InventoryItem {

  public TorchItem(Player entity) {
    super(entity);
  }

  @Override
  public boolean place(Vector2f tilePos) throws SlickException {
    if(haveItems()) {
      Torch torch = new Torch();
      this.owner.getLevel().addEntity(torch);
      
      torch.setTilePosition((int)tilePos.x, (int)tilePos.y);
      SoundManager.shared().placeBlockSound.playAsSoundEffect(1.0f, 1.0f, false);
      
      this.popItem();
      return true;
    } else {
     return false; 
    }
  }

  @Override
  public String getName() {
    return "Torch";
  }

  @Override
  public int harvestPower() {
    return InventoryItem.STANDARD_HARVEST_POWER;
  }

  @Override
  public String getKey() {
    return "ITEM_TORCH_TYPE";
  }

}
