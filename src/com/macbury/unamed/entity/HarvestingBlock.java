package com.macbury.unamed.entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

import com.macbury.unamed.Core;
import com.macbury.unamed.ImagesManager;
import com.macbury.unamed.component.Sprite;
import com.macbury.unamed.inventory.InventoryItem;
import com.macbury.unamed.level.HarvestableBlock;
import com.macbury.unamed.level.Sidewalk;


public class HarvestingBlock extends BlockEntity {
  private HarvestableBlock currentBlock;
  private Sprite breakEffectSprite;

  public HarvestingBlock() throws SlickException {
    super();
    this.solid              = true;
    this.visibleUnderTheFog = true;
    this.breakEffectSprite  = new Sprite(ImagesManager.shared().getDestroyBlockEffectForProgress(0.0f));
    addComponent(breakEffectSprite);
  }
  
  public void setBlock(HarvestableBlock block) {
    this.currentBlock       = block;
    this.setTileX(block.x);
    this.setTileY(block.y);
    this.setWidth(Core.TILE_SIZE);
    this.setHeight(Core.TILE_SIZE);
    
    Log.info("Starting harvesting block: " + block.getId());
  }
  
  @Override
  public boolean use() {
    return false;
  }

  @Override
  public int getHardness() {
    return currentBlock.getHardness();
  }

  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
    super.update(gc, sb, delta);
    
    if (!isHarvesting) {
      Log.info("Did not finished in time to harvest block: "+ currentBlock.getId());
      this.destroy();
    } else {
      float progress = 1.0f - (float)Math.max(this.hardness, 0) / this.getHardness();
      //Log.debug("Harvesting status: " + progress + " with hardness " + this.hardness);
      this.breakEffectSprite.setImage(ImagesManager.shared().getDestroyBlockEffectForProgress(progress));
    }
  }

  @Override
  public InventoryItem harvestedByPlayer() throws SlickException {
    this.getLevel().digSidewalk(this.getTileX(), this.getTileY(), true);
    this.destroy();
    return currentBlock.harvestedByPlayer();
  }
}
