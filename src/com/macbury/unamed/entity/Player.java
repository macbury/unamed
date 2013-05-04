package com.macbury.unamed.entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import com.macbury.unamed.Core;
import com.macbury.unamed.InputManager;
import com.macbury.unamed.SoundManager;
import com.macbury.unamed.Timer;
import com.macbury.unamed.TimerInterface;
import com.macbury.unamed.attack.PunchAttack;
import com.macbury.unamed.block.Block;
import com.macbury.unamed.block.Cobblestone;
import com.macbury.unamed.block.Dirt;
import com.macbury.unamed.block.HarvestableBlock;
import com.macbury.unamed.block.LiquidBlock;
import com.macbury.unamed.block.PassableBlock;
import com.macbury.unamed.block.Rock;
import com.macbury.unamed.block.Sand;
import com.macbury.unamed.combat.Damage;
import com.macbury.unamed.component.CharacterAnimation;
import com.macbury.unamed.component.HealthComponent;
import com.macbury.unamed.component.HitBox;
import com.macbury.unamed.component.KeyboardMovement;
import com.macbury.unamed.component.Light;
import com.macbury.unamed.component.Sprite;
import com.macbury.unamed.component.TextComponent;
import com.macbury.unamed.component.TileBasedMovement;
import com.macbury.unamed.intefrace.InterfaceManager;
import com.macbury.unamed.inventory.BlockItem;
import com.macbury.unamed.inventory.InventoryItem;
import com.macbury.unamed.inventory.InventoryManager;
import com.macbury.unamed.inventory.TorchItem;
import com.macbury.unamed.level.Level;
import com.macbury.unamed.npc.PlayerTriggers;

public class Player extends Character implements TimerInterface {
  //public final static int  FOG_OF_WAR_RADIUS = 10;
  private static final int LIGHT_POWER                = 10;
  
  final static short MAX_PLACING_TIME                   = 250;
  public static final short MAX_TAKING_TIME           = 300;
  private static final short START_HEALTH             = 100;
  private static final float PLAYER_REGENERATE_FACTOR = 0.45f;
  
  KeyboardMovement   keyboardMovement;
  
  private Timer takeActionTimer;
  private Timer placeActionTimer;
  
  public void setKeyboardEnabled(boolean enabled) {
    this.keyboardMovement.enabled = enabled;
  }
  
  public Player() throws SlickException {
    super();
    this.getHealth().setRegenerateFactor(PLAYER_REGENERATE_FACTOR);
    this.getHealth().setMaxHelath(START_HEALTH);

    
    Light light = new Light();
    light.setLightPower(LIGHT_POWER);
    light.updateLight();
    light.setUpdateFogOfWar(true);
    addComponent(light);
    
    charactedAnimation.loadCharacterImage("chars/dman");
    
    keyboardMovement = new KeyboardMovement();
    addComponent(keyboardMovement);
    
    takeActionTimer  = new Timer(MAX_TAKING_TIME, this);
    placeActionTimer = new Timer(MAX_PLACING_TIME, this);

  }

  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
    super.update(gc, sb, delta);
    this.takeActionTimer.update(delta);
    this.placeActionTimer.update(delta);
    SoundManager.shared().setPosition(getTileX(), getTileY());
    
    if (this.keyboardMovement.enabled) {
      Input input    = gc.getInput();

      if (!tileMovement.isMoving()) {
        if (input.isKeyDown(Core.CANCEL_KEY)) {
          if (!this.takeActionTimer.running()) {
            useElementInFrontOfMe();
            this.takeActionTimer.start();
          } else {
            attackEntityInFrontOfMe();
          }
        }
        
        if(input.isKeyDown(Core.ACTION_KEY)) {
          if (!this.placeActionTimer.running()) {
            placeOrUseElementInFrontOfMe();
            this.placeActionTimer.start();
          }
        }
      }
      
      InventoryManager inventory = InventoryManager.shared();
      
      if (input.isKeyPressed(Input.KEY_0)) {
        inventory.setInventoryIndex(10);
      } else if (input.isKeyPressed(Input.KEY_1)) {
        inventory.setInventoryIndex(1);
      } else if (input.isKeyPressed(Input.KEY_2)) {
        inventory.setInventoryIndex(2);
      } else if (input.isKeyPressed(Input.KEY_3)) {
        inventory.setInventoryIndex(3);
      } else if (input.isKeyPressed(Input.KEY_4)) {
        inventory.setInventoryIndex(4);
      } else if (input.isKeyPressed(Input.KEY_5)) {
        inventory.setInventoryIndex(5);
      } else if (input.isKeyPressed(Input.KEY_6)) {
        inventory.setInventoryIndex(6);
      } else if (input.isKeyPressed(Input.KEY_7)) {
        inventory.setInventoryIndex(7);
      } else if (input.isKeyPressed(Input.KEY_8)) {
        inventory.setInventoryIndex(8);
      } else if (input.isKeyPressed(Input.KEY_9)) {
        inventory.setInventoryIndex(9);
      }
    }
    
    if (tileMovement.isMoving()) {
      this.getHealth().setRegenerateFactor(PLAYER_REGENERATE_FACTOR);
    } else {
      this.getHealth().setRegenerateFactor(PLAYER_REGENERATE_FACTOR * 4);
    }
  }


  private void attackEntityInFrontOfMe() throws SlickException {
    Entity entityInFront = getEntityInFront();
    
    if (Monster.class.isInstance(entityInFront)) {
      Monster monster = (Monster)entityInFront;
      InventoryManager.shared().getCurrentAttack().attack(this, monster);
    }
  }

  private void placeOrUseElementInFrontOfMe() throws SlickException {
    Vector2f frontTilePosition = getTilePositionInFront();
    
    Block  blockInFront        = this.getLevel().getBlockForPosition((int)frontTilePosition.x, (int)frontTilePosition.y);
    if (HarvestableBlock.class.isInstance(blockInFront)) {
      // Cannot use element because is harvestable block!
    } else if( PassableBlock.class.isInstance(blockInFront) ) {
      Entity entityInFront       = this.getLevel().getEntityForTilePosition((int)frontTilePosition.x, (int)frontTilePosition.y);
      
      if (PlayerTriggers.class.isInstance(entityInFront)) {
        ((PlayerTriggers) entityInFront).onActionButton(this);
      } else if (CollectableItem.class.isInstance(entityInFront)) {
        CollectableItem item = (CollectableItem) entityInFront;
        item.lootBy(this);
      } else if (BlockEntity.class.isInstance(entityInFront)) {
        BlockEntity usableEntity = (BlockEntity) entityInFront;
        if (!usableEntity.use()) {
          SoundManager.shared().cancelSound.playAsSoundEffect(1.0f, 1.0f, false);
        }
      } else if (!LiquidBlock.class.isInstance(blockInFront) && entityInFront == null) {
        placeCurrentInventoryItemInFront(frontTilePosition);
      } else {
        SoundManager.shared().cancelSound.playAsSoundEffect(1.0f, 1.0f, false);
      }
    } else {
      SoundManager.shared().cancelSound.playAsSoundEffect(1.0f, 1.0f, false);
    }
  }
  
  private void placeCurrentInventoryItemInFront(Vector2f frontTilePosition) throws SlickException {
    InventoryItem currentItem = InventoryManager.shared().getCurrentHotBarItem();
    
    if (currentItem != null) {
      if (currentItem.place(frontTilePosition)) {
        if (!currentItem.haveItems()) {
          InventoryManager.shared().remove(currentItem);
        }
      } else {
        SoundManager.shared().cancelSound.playAsSoundEffect(1.0f, 1.0f, false);
      }
    }
  }
  
  private int currentHarvestPower() throws SlickException {
    InventoryItem currentItem = InventoryManager.shared().getCurrentHotBarItem();
    
    if (currentItem == null) {
      return InventoryItem.STANDARD_HARVEST_POWER;
    } else {
      return currentItem.harvestPower();
    }
  }

  private void useElementInFrontOfMe() throws SlickException {

    Entity entityInFront = getEntityInFront();
    
    if (entityInFront == null) {
      Vector2f frontTilePosition = getTilePositionInFront();
      Block block = this.getLevel().getBlockForPosition((int)frontTilePosition.x, (int)frontTilePosition.y);
      if (HarvestableBlock.class.isInstance(block)) {
        HarvestingBlock harvestingEntityAction = new HarvestingBlock();
        this.getLevel().addEntity(harvestingEntityAction);
        harvestingEntityAction.setBlock((HarvestableBlock) block);
        entityInFront = (Entity)harvestingEntityAction;
      }
    }
    
    if (entityInFront != null) {
      if (Monster.class.isInstance(entityInFront)) {
        attackEntityInFrontOfMe();
      } else if (BlockEntity.class.isInstance(entityInFront)) {
        BlockEntity usableEntity = (BlockEntity) entityInFront;
        
        InventoryItem item = usableEntity.harvest(currentHarvestPower());
        
        TextParticle.spawnTextAt("-"+currentHarvestPower(), (int)usableEntity.getCenteredPosition().getX(), (int)usableEntity.getCenteredPosition().getY());
        
        DigEffectEntity punchEntity = (DigEffectEntity) Level.shared().getUsedEntity(DigEffectEntity.class);
        punchEntity.setAnimationByItem(InventoryManager.shared().getCurrentHotBarItem());
        this.getLevel().addEntity(punchEntity);
        punchEntity.setTilePosition(entityInFront.getTileX(), entityInFront.getTileY());
        
        if (item == null) {
          SoundManager.shared().playDigForBlock(entityInFront.getBlock());
        } else {
          SoundManager.shared().pop.playAsSoundEffect(1.0f, 1.0f, false);
        }
      }
    } else {
      SoundManager.shared().miss.playAsSoundEffect(1.0f, 1.0f, false);
    }
  }

  private Entity getEntityInFront() {
    Vector2f frontTilePosition = getTilePositionInFront();
    return this.getLevel().getEntityForTilePosition((int)frontTilePosition.x, (int)frontTilePosition.y);
  }

  @Override
  public void onTimerFire(Timer timer) throws SlickException {
    timer.stop();
  }

}
