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
  private short timer = 0;
  private final static short TIME_TO_EXPLOSION = 5 * 1000;
  private static final byte STATE_COUNTDOWN = 0;
  private static final byte STATE_EXPLOSION = 1;
  private static final short TIME_AFTER_EXPLOSION = 500;
  
  private byte state = STATE_COUNTDOWN;
  private Sprite sprite;
  
  public Dynamite() throws SlickException {
    super();
    this.visibleUnderTheFog = true;
    
    Light light = new Light();
    light.setLightPower(IGNITE_POWER);
    light.updateLight();
    addComponent(light);
    
    this.sprite = new Sprite(ImagesManager.shared().iconDynamite);
    addComponent(sprite);
    this.timer = TIME_TO_EXPLOSION;
  }
  
  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
    super.update(gc, sb, delta);
    switch (this.state) {
      case STATE_COUNTDOWN:
        stateCountdown(delta);
      break;
      case STATE_EXPLOSION:
        stateExplosion(delta);
      break;
    }
  }

  private void stateExplosion(int delta) {
    timer -= delta;
    
    if (timer <= 0) {
      this.destroy();
    }
  }

  private void stateCountdown(int delta) throws SlickException {
    if (timer == TIME_TO_EXPLOSION) {
      SoundManager.shared().playAt(this.getTileX(), this.getTileY(), SoundManager.shared().fuse);
    }
    
    timer -= delta;

    if (timer <= 0) {
      this.sprite.enabled = false;
      this.state = STATE_EXPLOSION;
      this.timer = TIME_AFTER_EXPLOSION;
      SoundManager.shared().playExplode();
      for (int i = 1; i <= 4; i++) {
        digCircle(this.getTileX(), this.getTileY(), i);
      }
      this.getLight().setLightPower(IGNITE_POWER * 4);
      this.getLight().updateLight();
    }
  }
  
  public void applyDamage(int x, int y, int power) throws SlickException {
    this.getLevel().digSidewalk(x, y, true, true);
    Entity attackedEntity = this.getLevel().getEntityForTilePosition(x, y);
    if (attackedEntity != null) {
      if (BlockEntity.class.isInstance(attackedEntity)) {
        BlockEntity entity = (BlockEntity)attackedEntity;
        InventoryItem item = entity.harvest(power);
      } else if (attackedEntity.haveHealth()) {
        attackedEntity.getHealth().applyDamage((short) power);
      }
    }
  }
  
  public void digCircle(int x0, int y0, int radius) throws SlickException {
    int x = radius, y = 0;
    int xChange = 1 - (radius << 1);
    int yChange = 0;
    int radiusError = 0;
    int power = 80;
    while(x >= y)  {
      applyDamage(x + x0, y + y0, power);
      applyDamage(y + x0, x + y0, power);
      applyDamage(-x + x0, y + y0, power);
      applyDamage(-y + x0, x + y0, power);
      applyDamage(-y + x0, x + y0, power);
      applyDamage(-x + x0, -y + y0, power);
      applyDamage(-y + x0, -x + y0, power);
      applyDamage(x + x0, -y + y0, power);
      applyDamage(y + x0, -x + y0, power);
   
      y++;
      radiusError += yChange;
      yChange += 2;
      if(((radiusError << 1) + xChange) > 0)
      {
        x--;
        radiusError += xChange;
        xChange += 2;
      }
    }
  }

  @Override
  public boolean use() {
    return false;
  }

  @Override
  public int getHardness() {
    return 5;
  }

  @Override
  public InventoryItem harvestedByPlayer() {
    if (this.state == STATE_COUNTDOWN) {
      this.timer = (short) (TIME_AFTER_EXPLOSION * 0.3);
    }
    return null;
  }

}
