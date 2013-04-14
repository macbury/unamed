package com.macbury.unamed.entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.macbury.unamed.ImagesManager;
import com.macbury.unamed.SoundManager;
import com.macbury.unamed.component.Light;
import com.macbury.unamed.component.Sprite;
import com.macbury.unamed.inventory.InventoryItem;
import com.macbury.unamed.level.HarvestableBlock;

public class Dynamite extends BlockEntity {
  
  private static final int IGNITE_POWER = 3;
  private short timeToExplosion = 0;
  private final static short TIME_TO_EXPLOSION = 5 * 1000;
  
  public Dynamite() throws SlickException {
    super();
    this.visibleUnderTheFog = true;
    
    Light light = new Light();
    light.setLightPower(IGNITE_POWER);
    light.updateLight();
    addComponent(light);
    
    Sprite sprite = new Sprite(ImagesManager.shared().iconDynamite);
    addComponent(sprite);
    this.timeToExplosion = TIME_TO_EXPLOSION;
  }
  
  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
    super.update(gc, sb, delta);
    if (timeToExplosion == TIME_TO_EXPLOSION) {
      SoundManager.shared().playAt(this.getTileX(), this.getTileY(), SoundManager.shared().fuse);
    }
    
    timeToExplosion -= delta;

    if (timeToExplosion <= 0) {
      SoundManager.shared().playAt(this.getTileX(), this.getTileY(), SoundManager.shared().explode);
      this.destroy();
    }
  }

  @Override
  public boolean use() {
    return false;
  }

  @Override
  public int getHardness() {
    return HarvestableBlock.HARDNESS_INFITNITY;
  }

  @Override
  public InventoryItem harvestedByPlayer(Player byPlayer) {
    return null;
  }

}
