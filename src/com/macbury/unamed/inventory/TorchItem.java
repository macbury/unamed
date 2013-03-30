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
  public boolean place() throws SlickException {
    Vector2f tilePos = this.owner.getNotSolidTilePositionInFront();
    if (tilePos != null ) {
      
      Entity entityInFront = this.owner.getLevel().getEntityForTilePosition((int)tilePos.x, (int)tilePos.y);
      
      if (entityInFront == null) {
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
      } else {
        return false;
      }
      
    }
    
    return false;
  }

  @Override
  public String getName() {
    return "Torch";
  }

  @Override
  public int harvestPower() {
    return InventoryItem.STANDARD_HARVEST_POWER;
  }

}
