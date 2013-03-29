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
    // TODO Auto-generated constructor stub
  }

  @Override
  public boolean use() throws SlickException {
    Vector2f tilePos = this.owner.getTilePositionInFront();
    if (tilePos != null) {
      
      Entity entityInFront = this.owner.getLevel().getEntityForTilePosition((int)tilePos.x, (int)tilePos.y);
      
      if (entityInFront == null) {
        Torch torch = new Torch();
        this.owner.getLevel().addEntity(torch);
        
        torch.setTilePosition((int)tilePos.x, (int)tilePos.y);
        SoundManager.shared().placeBlockSound.playAsSoundEffect(1.0f, 1.0f, false);
        return true;
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


}
