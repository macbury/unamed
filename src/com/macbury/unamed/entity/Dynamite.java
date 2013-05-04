package com.macbury.unamed.entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.macbury.unamed.ImagesManager;
import com.macbury.unamed.SoundManager;
import com.macbury.unamed.block.Block;
import com.macbury.unamed.block.HarvestableBlock;
import com.macbury.unamed.combat.Damage;
import com.macbury.unamed.component.Light;
import com.macbury.unamed.component.Sprite;
import com.macbury.unamed.intefrace.InterfaceManager;
import com.macbury.unamed.inventory.InventoryItem;
import com.macbury.unamed.util.RaytraceCallback;
import com.macbury.unamed.util.RaytraceUtil;

public class Dynamite extends BlockEntity implements RaytraceCallback {
  
  private static final int IGNITE_POWER = 3;
  private short timer = 0;
  private final static short TIME_TO_EXPLOSION = 5 * 1000;
  private static final byte STATE_COUNTDOWN = 0;
  private static final byte STATE_EXPLOSION = 1;
  private static final short TIME_AFTER_EXPLOSION = 500;
  private static final int DYNAMITE_POWER = 30;
  
  private byte state = STATE_COUNTDOWN;
  private Sprite sprite;
  private RaytraceUtil raytrace;
  private int shockWavePower;
  
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
    this.raytrace = new RaytraceUtil(this);
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
    
    if (!InterfaceManager.shared().shouldBlockGamePlay()) {
      timer -= delta;
    }

    if (timer <= 0) {
      this.sprite.enabled = false;
      this.state = STATE_EXPLOSION;
      this.timer = TIME_AFTER_EXPLOSION;
      SoundManager.shared().playExplode(this.getTileX(), this.getTileY());
      for (int i = 1; i <= 4; i++) {
        digCircle(this.getTileX(), this.getTileY(), i);
      }
      this.getLight().setLightPower(IGNITE_POWER * 4);
      this.getLight().updateLight();
    }
  }
  
  public void applyDamage(int x, int y, int distance) throws SlickException {
    Block block = this.getLevel().getBlockForPosition(x, y);
    
    if (block != null && block.isHarvestable()) {
      int hardness = block.asHarvestableBlock().getHardness();
      
      if (hardness > this.shockWavePower) {
        this.shockWavePower = 0;
      } else {
        this.shockWavePower -= hardness;
        this.getLevel().digSidewalk(x, y, true, true);
      }
    } else {
      Entity attackedEntity = this.getLevel().getEntityForTilePosition(x, y);
      if (attackedEntity != null) {
        Damage damage = new Damage(shockWavePower);
        if (BlockEntity.class.isInstance(attackedEntity)) {
          BlockEntity entity = (BlockEntity)attackedEntity;
          shockWavePower -= entity.getHardness();
          entity.harvest(shockWavePower);
        } else if (attackedEntity.haveHealth()) {
          attackedEntity.getHealth().applyDamage(damage);
        }
      }
    }
  }
  
  @Override
  public void onRayVisits(int x, int y, int distance) throws SlickException {
    if (this.shockWavePower > 0) {
      applyDamage(x, y, distance);
    }
  }
  
  public void shockWave(int x0, int y0, int x1, int y1) throws SlickException {
    this.shockWavePower = DYNAMITE_POWER;
    raytrace.raytrace(x0, y0, x1, y1);
  }
  
  public void digCircle(int x0, int y0, int radius) throws SlickException {
    int x = radius, y = 0;
    int xChange = 1 - (radius << 1);
    int yChange = 0;
    int radiusError = 0;
    while(x >= y)  {
      
      shockWave(x0, y0, x + x0, y + y0);
      shockWave(x0, y0, y + x0, x + y0);
      shockWave(x0, y0, -x + x0, y + y0);
      shockWave(x0, y0, -y + x0, x + y0);
      shockWave(x0, y0, -y + x0, x + y0);
      shockWave(x0, y0, -x + x0, -y + y0);
      shockWave(x0, y0, -y + x0, -x + y0);
      shockWave(x0, y0, x + x0, -y + y0);
      shockWave(x0, y0, y + x0, -x + y0);
   
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
