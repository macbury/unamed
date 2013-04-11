package com.macbury.unamed.entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.macbury.unamed.component.Light;
import com.macbury.unamed.inventory.InventoryItem;

public class Bomb extends BlockEntity {
  private static final int EXPLOADING_TIME = 5000;
  private static final int LIGHT_POWER = 3;
  
  private int timeToExplode = EXPLOADING_TIME;
  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
    super.update(gc, sb, delta);
    
    timeToExplode -= delta;
    if (timeToExplode <= 0) {
      this.destroy();
    }
  }

  public Bomb() throws SlickException {
    super();
    
    this.visibleUnderTheFog = true;
    
    Light light = new Light();
    light.setLightPower(LIGHT_POWER);
    light.updateLight();
    addComponent(light);
    
    //Sprite sprite      = new Sprite(spriteSheet.getSprite(3, 0));
    //addComponent(sprite);
  }
  
  @Override
  public boolean use() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public int getHardness() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public InventoryItem harvestedByPlayer(Player byPlayer) {
    // TODO Auto-generated method stub
    return null;
  }

}
