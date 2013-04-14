package com.macbury.unamed.entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import com.macbury.unamed.Core;
import com.macbury.unamed.SoundManager;
import com.macbury.unamed.component.CharacterAnimation;
import com.macbury.unamed.component.HitBox;
import com.macbury.unamed.component.KeyboardMovement;
import com.macbury.unamed.component.Light;
import com.macbury.unamed.component.Sprite;
import com.macbury.unamed.component.TileBasedMovement;
import com.macbury.unamed.inventory.BlockItem;
import com.macbury.unamed.inventory.InventoryItem;
import com.macbury.unamed.inventory.InventoryManager;
import com.macbury.unamed.inventory.TorchItem;
import com.macbury.unamed.level.Block;
import com.macbury.unamed.level.Cobblestone;
import com.macbury.unamed.level.Dirt;
import com.macbury.unamed.level.HarvestableBlock;
import com.macbury.unamed.level.LiquidBlock;
import com.macbury.unamed.level.PassableBlock;
import com.macbury.unamed.level.Rock;
import com.macbury.unamed.level.Sand;

public class Player extends Entity {
  public final static int  FOG_OF_WAR_RADIUS = 10;
  private static final int ENTITY_ZINDEX    = Entity.ENTITY_BASE_LAYER+1;
  private static final int LIGHT_POWER      = 11;
  TileBasedMovement tileMovement;
  KeyboardMovement  keyboardMovement;
  
  final static int MAX_PLACING_TIME = 250;
  private static final int MAX_TAKING_TIME = 300;
  
  private boolean pressedPlaceKey   = false;
  private boolean pressedTakeKey    = false;
  
  private int buttonPlacingThrottle = 0;
  private int buttonTakingThrottle  = 0;
  private Sprite punchSprite;
  public InventoryManager inventory;
  
  public void setKeyboardEnabled(boolean enabled) {
    this.keyboardMovement.enabled = enabled;
  }
  
  public Player() throws SlickException {
    super();
    
    this.collidable = true;
    this.inventory  = InventoryManager.shared();
    //TorchItem item  = new TorchItem(this);
    //item.addItem(99);
    //this.inventory.add(item);
    
    /*BlockItem blockItem = new BlockItem(this, Dirt.class);
    blockItem.addItem(98);
    this.inventory.add(blockItem);
    
    blockItem = new BlockItem(this, Rock.class);
    blockItem.addItem(98);
    this.inventory.add(blockItem);
    
    blockItem = new BlockItem(this, Sand.class);
    blockItem.addItem(98);
    this.inventory.add(blockItem);
    
    blockItem = new BlockItem(this, Cobblestone.class);
    blockItem.addItem(98);
    this.inventory.add(blockItem);*/
    
    this.z = ENTITY_ZINDEX;
    tileMovement = new TileBasedMovement();
    addComponent(tileMovement);
    
    keyboardMovement = new KeyboardMovement();
    addComponent(keyboardMovement);
    
    //addComponent(new Sprite(ImagesManager.shared().getImage("Shadow.png")));

    //punchSprite = new Sprite(ImagesManager.shared().getImage("base_punch.png"));
    //addComponent(punchSprite);
   // punchSprite.enabled = true;
    
    CharacterAnimation characterAnimationComponent = new CharacterAnimation();
    addComponent(characterAnimationComponent);
    characterAnimationComponent.loadCharacterImage("chars/dman");

    solid = true;
    if (Core.DEBUG) {
      addComponent(new HitBox());
    }
    
    Light light = new Light();
    light.setLightPower(LIGHT_POWER);
    light.updateLight();
    addComponent(light);
  }
 
  
  public Vector2f getTilePositionInFront() {
    int dx = this.getTileX();
    int dy = this.getTileY();
    
    switch (tileMovement.direction) {
      case TileBasedMovement.DIRECTION_DOWN:
        dy += 1;
      break;

      case TileBasedMovement.DIRECTION_TOP:
        dy -= 1;
      break;
      
      case TileBasedMovement.DIRECTION_LEFT:
        dx -= 1;
      break;
      
      case TileBasedMovement.DIRECTION_RIGHT:
        dx += 1;
      break;
    }
    
    return new Vector2f(dx, dy);
  }
  
  /*
   * Return position as tiles cordinates for empty tile that is not solid
   */
  public Vector2f getNotSolidTilePositionInFront() {
    Vector2f tileInFront = getTilePositionInFront();
    
    if (!this.getLevel().isSolid((int)tileInFront.getX(), (int)tileInFront.getY())) {
      return tileInFront;
    } else {
      return null;
    }
  }
  
  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
    super.update(gc, sb, delta);
    SoundManager.shared().setPosition(getTileX(), getTileY());
    
    if (this.keyboardMovement.enabled) {
      Input input    = gc.getInput();
      
      if (pressedPlaceKey) {
        buttonPlacingThrottle += delta;
      }
      
      if (pressedTakeKey) {
        buttonTakingThrottle += delta;
      }
      
      if (buttonPlacingThrottle > MAX_PLACING_TIME) {
        pressedPlaceKey = false;
      }
      
      if (buttonTakingThrottle > MAX_TAKING_TIME) {
        pressedTakeKey = false;
      }
      
      if (!tileMovement.isMoving()) {
        if (input.isKeyDown(Input.KEY_X) && !pressedTakeKey) {
          pressedTakeKey       = true;
          buttonTakingThrottle = 0;
          
          useElementInFrontOfMe();
        }
        
        if(input.isKeyDown(Input.KEY_Z) && !pressedPlaceKey) {
          pressedPlaceKey         = true;
          buttonPlacingThrottle   = 0;
          placeOrUseElementInFrontOfMe();
        }
      }
      
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
  }


  private void placeOrUseElementInFrontOfMe() throws SlickException {
    Vector2f frontTilePosition = getTilePositionInFront();
    
    Block  blockInFront        = this.getLevel().getBlockForPosition((int)frontTilePosition.x, (int)frontTilePosition.y);
    if (HarvestableBlock.class.isInstance(blockInFront)) {
      // Cannot use element because is harvestable block!
    } else if( PassableBlock.class.isInstance(blockInFront) && !LiquidBlock.class.isInstance(blockInFront) ) {
      Entity entityInFront       = this.getLevel().getEntityForTilePosition((int)frontTilePosition.x, (int)frontTilePosition.y);
      
      if (entityInFront != null) {
        if (CollectableItem.class.isInstance(entityInFront)) {
          CollectableItem item = (CollectableItem) entityInFront;
          item.loot();
        } else if (BlockEntity.class.isInstance(entityInFront)) {
          BlockEntity usableEntity = (BlockEntity) entityInFront;
          if (!usableEntity.use()) {
            SoundManager.shared().cancelSound.playAsSoundEffect(1.0f, 1.0f, false);
          }
        } else {
          placeCurrentInventoryItemInFront(frontTilePosition);
        }
      } else {
        placeCurrentInventoryItemInFront(frontTilePosition);
      }
    } else {
      SoundManager.shared().cancelSound.playAsSoundEffect(1.0f, 1.0f, false);
    }
  }
  
  private void placeCurrentInventoryItemInFront(Vector2f frontTilePosition) throws SlickException {
    InventoryItem currentItem = inventory.getCurrentHotBarItem();
    
    if (currentItem != null) {
      if (currentItem.place(frontTilePosition)) {
        if (!currentItem.haveItems()) {
          inventory.remove(currentItem);
        }
      } else {
        SoundManager.shared().cancelSound.playAsSoundEffect(1.0f, 1.0f, false);
      }
    }
  }
  
  private int currentHarvestPower() {
    InventoryItem currentItem = inventory.getCurrentHotBarItem();
    
    if (currentItem == null) {
      return InventoryItem.STANDARD_HARVEST_POWER;
    } else {
      return currentItem.harvestPower();
    }
  }

  private void useElementInFrontOfMe() throws SlickException {
    Vector2f frontTilePosition = getTilePositionInFront();
    Entity entityInFront       = this.getLevel().getEntityForTilePosition((int)frontTilePosition.x, (int)frontTilePosition.y);
    
    if (entityInFront == null) {
      Block block = this.getLevel().getBlockForPosition((int)frontTilePosition.x, (int)frontTilePosition.y);
      if (HarvestableBlock.class.isInstance(block)) {
        HarvestingBlock harvestingEntityAction = new HarvestingBlock();
        this.getLevel().addEntity(harvestingEntityAction);
        harvestingEntityAction.setBlock((HarvestableBlock) block);
        entityInFront = (Entity)harvestingEntityAction;
      }
    }
    
    if (entityInFront != null) {
      if (BlockEntity.class.isInstance(entityInFront)) {
        BlockEntity usableEntity = (BlockEntity) entityInFront;
        
        InventoryItem item = usableEntity.harvest(currentHarvestPower(), this);
        if (item == null) {
          SoundManager.shared().playDigForBlock(entityInFront.getBlock());
        } else {
          entityInFront.destroy();
          SoundManager.shared().pop.playAsSoundEffect(1.0f, 1.0f, false);
        }
      }
    } else {
      SoundManager.shared().miss.playAsSoundEffect(1.0f, 1.0f, false);
    }
  }

}
