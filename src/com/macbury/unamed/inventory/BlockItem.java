package com.macbury.unamed.inventory;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import com.macbury.unamed.SoundManager;
import com.macbury.unamed.entity.Player;
import com.macbury.unamed.level.Cobblestone;
import com.macbury.unamed.level.Dirt;
import com.macbury.unamed.level.HarvestableBlock;
import com.macbury.unamed.level.Level;
import com.macbury.unamed.level.Rock;
import com.macbury.unamed.level.Sand;

public class BlockItem extends InventoryItem {
  public Class<HarvestableBlock> blockType;
  
  public BlockItem(Class blockType) {
    this.blockType = blockType;
  }
  
  @Override
  public String getKey() {
    return this.blockType.getName();
  }

  @Override
  public String getName() {
    return "Change this";
  }

  @Override
  public boolean place(Vector2f tilePos) throws SlickException {
    HarvestableBlock block = null;
    
    if(haveItems()) {
      if (Cobblestone.class.equals(blockType)) {
        block = new Cobblestone((int)tilePos.x, (int)tilePos.y);
      }
      
      if (Rock.class.equals(blockType)) {
        block = new Rock((int)tilePos.x, (int)tilePos.y);
      }
      
      if (Dirt.class.equals(blockType)) {
        block = new Dirt((int)tilePos.x, (int)tilePos.y);
      }
      
      if (Sand.class.equals(blockType)) {
        block = new Sand((int)tilePos.x, (int)tilePos.y);
      }
    }
    
    if (block != null) {
      Level.shared().setBlockForPosition(block, (int)tilePos.x, (int)tilePos.y);
      SoundManager.shared().placeBlockSound.playAsSoundEffect(1.0f, 1.0f, false);
      this.popItem();
      return true;
    }
    
    return (block != null);
  }

  @Override
  public int harvestPower() {
    return STANDARD_HARVEST_POWER;
  }

}
