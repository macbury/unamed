package com.macbury.unamed.inventory;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Vector2f;

import com.macbury.unamed.SoundManager;
import com.macbury.unamed.entity.Player;
import com.macbury.unamed.entity.Torch;

public class TorchItem extends InventoryItem {

  public TorchItem(Player entity) {
    super(entity);
    // TODO Auto-generated constructor stub
  }

  @Override
  public void use() throws SlickException {
    Vector2f tilePos = this.owner.getTilePositionInFront();
    if (tilePos != null) {
      
      Torch torch = new Torch();
      this.owner.getLevel().addEntity(torch);
      
      torch.setTilePosition((int)tilePos.x, (int)tilePos.y);
      SoundManager.shared().igniteSound.play();
    }
  }

  @Override
  public String getName() {
    return "Torch";
  }


}
