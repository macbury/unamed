package com.macbury.unamed.entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

import com.macbury.unamed.Core;
import com.macbury.unamed.SoundManager;
import com.macbury.unamed.Timer;
import com.macbury.unamed.TimerInterface;
import com.macbury.unamed.Util;
import com.macbury.unamed.component.CollectableItemSprite;
import com.macbury.unamed.inventory.BlockItem;
import com.macbury.unamed.inventory.InventoryItem;
import com.macbury.unamed.inventory.InventoryManager;
import com.macbury.unamed.level.BlockResources;

public class CollectableItem extends ReusableEntity implements TimerInterface {
  public final static short LIFE_TIME = 15000;
  InventoryItem itemToCollect;

  int lifeTime = 0;
  private CollectableItemSprite imageSprite;
  private Timer hideTimer;
  private byte state;
  
  private float totalMoveTime = 0.0f;
  private Entity targetPlayer;
  private Vector2f basePosition;
  private final static byte STATE_IDLE = 0;
  private static final byte STATE_COLLECTING  = 1;
  private static final float COLLECTING_SPEED = 0.01f;
  private static final byte STATE_LOOT = 2;
  
  public CollectableItem() throws SlickException {
    super();
    
    this.setWidth(Core.TILE_SIZE / 2);
    this.setHeight(Core.TILE_SIZE / 2);
    this.collidable    = true;
    
    this.visibleUnderTheFog = true;
    
    this.state = STATE_IDLE;
  }

  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
    super.update(gc, sb, delta);
    
    switch (this.state) {
      case STATE_IDLE:
        this.hideTimer.update(delta);
        basePosition = getCenteredPosition();
      break;
      
      case STATE_COLLECTING:
        totalMoveTime += COLLECTING_SPEED * (float)delta;
        float x = Math.round(Util.lerp(basePosition.x, targetPlayer.getRect().getCenterX(), totalMoveTime));
        float y = Math.round(Util.lerp(basePosition.y, targetPlayer.getRect().getCenterY(), totalMoveTime));
        this.setCenterX(x);
        this.setCenterY(y);
        
        if (this.totalMoveTime > 1.0) {
          this.state = STATE_LOOT;
        }
      break;
      
      case STATE_LOOT:
        InventoryManager.shared().addItem(itemToCollect);
        SoundManager.shared().loot.playAsSoundEffect(1.0f, 1.0f, false);
        this.destroy();
      break;
    }
  }

  @Override
  public void onCollideWith(Entity entity) throws SlickException {
    Log.info("Collided with: " + entity.toString() + " state: "+STATE_IDLE);
    if (this.state == STATE_IDLE) {
      if (Player.class.isInstance(entity)) {
        this.targetPlayer = entity;
        loot();
      }
    }
  }

  public void loot() throws SlickException {
    if (this.state == STATE_IDLE) {
      this.state = STATE_COLLECTING;
      this.collidable = false;
      this.hideTimer.stop();
    }
  }

  @Override
  public void onTimerFire(Timer timer) {
    this.destroy();
  }

  public void lootBy(Player player) throws SlickException {
    this.targetPlayer = player;
    loot();
  }

  @Override
  public void onReuse() {
    this.totalMoveTime = 0;
    this.targetPlayer = null;
    if (this.hideTimer != null) {
      this.hideTimer.restart();
    }
    this.state = STATE_IDLE;
    this.collidable = true;
  }
  
  public InventoryItem getItemToCollect() {
    return itemToCollect;
  }

  public void setItemToCollect(InventoryItem item) throws SlickException {
    this.itemToCollect = item;
    
    Image image = null;
    if (BlockItem.class.isInstance(item)) {
      BlockItem blockItem = (BlockItem) item;
      image = BlockResources.shared().imageForBlockClass(blockItem.blockType);
    } else {
      image = InventoryManager.shared().getImageForInventoryItem(item);
    }
    
    if (this.imageSprite == null) {
      this.imageSprite    = new CollectableItemSprite(image);
      addComponent(imageSprite);
    } else {
      this.imageSprite.setImage(image);
    }
    
    
    this.hideTimer = new Timer(LIFE_TIME, this);
    this.hideTimer.setIsPausableEvent(true);
    this.hideTimer.start();
  }
}
