package com.macbury.unamed.inventory;

import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import com.macbury.unamed.ImagesManager;
import com.macbury.unamed.SoundManager;
import com.macbury.unamed.attack.AttackBase;
import com.macbury.unamed.attack.PunchAttack;

public class InventoryManager extends ArrayList<InventoryItem> {
  private static InventoryManager shared;
  private static final long serialVersionUID = 1L;
  public final static int MIN_HOTBAR_INVENTORY_INDEX = 1;
  public final static int MAX_HOTBAR_INVENTORY_INDEX = 10;
  
  public int currentHotBarInventoryIndex = 0;
  private SpriteSheet spriteSheet;
  private HashMap<String, Image> itemImageCache;
  
  private PunchAttack defaultAttack;
  
  public static InventoryManager shared() throws SlickException {
    if (shared == null) {
      shared = new InventoryManager();
    }
    return shared;
  }
  
  public InventoryManager() throws SlickException {
    super();
    
    this.setSpriteSheet(ImagesManager.shared().getHotBarCellSpriteSheet());
    this.itemImageCache = new HashMap<>();
    InventoryManager.shared = this;
    defaultAttack = new PunchAttack();
  }
  
  public void update(int delta) throws SlickException {
    if (WeaponItem.class.isInstance(getCurrentHotBarItem())) {
      WeaponItem item = (WeaponItem)getCurrentHotBarItem();
      item.update(delta);
    } else {
      defaultAttack.update(delta);
    }
  }
  
  public InventoryItem getCurrentHotBarItem() {
    return this.getItem(currentHotBarInventoryIndex);
  }
  
  public void setInventoryIndex(int index) {
    SoundManager.shared().cursor.playAsSoundEffect(1.0f, 1.0f, false);
    index = Math.min(index, MAX_HOTBAR_INVENTORY_INDEX);
    index = Math.max(index, MIN_HOTBAR_INVENTORY_INDEX);
    index -= 1;
    
    if (WeaponItem.class.isInstance(this.getItem(index)) && this.getCurrentHotBarItem() != getItem(index)) {
      WeaponItem item = (WeaponItem)this.getItem(index);
      item.reset();
    } else {
      defaultAttack.getAttackTimer().restart();
    }
    this.currentHotBarInventoryIndex = index;
  }
  
  public AttackBase getCurrentAttack() {
    if (WeaponItem.class.isInstance(this.getItem(this.currentHotBarInventoryIndex))) {
      WeaponItem item = (WeaponItem)getCurrentHotBarItem();
      return item.getAttack();
    } else {
      return defaultAttack;
    }
  }

  public int getCurrentHotBarIndex() {
    return this.currentHotBarInventoryIndex;
  }

  public InventoryItem getItem(int i) {
    try {
      return get(i);
    } catch(IndexOutOfBoundsException e) {
      return null;
    }
  }
  
  public void addItem(InventoryItem item) {
    for (InventoryItem itemBackPack : this) {
      if (item.getKey().equals(itemBackPack.getKey())) {
        itemBackPack.addItem(item.getQuantity());
        return;
      }
    }
    
    add(item);
  }

  public SpriteSheet getSpriteSheet() {
    return spriteSheet;
  }

  public void setSpriteSheet(SpriteSheet spriteSheet) {
    this.spriteSheet = spriteSheet;
  }
  
  public Image getOrLoadInventoryItemImage(Class klass, int x, int y) {
    Image img = itemImageCache.get(klass.getName());
    if (img == null) {
      img = spriteSheet.getSubImage(x, y);
      itemImageCache.put(klass.getName(), img);
    }
    return img;
  }
  
  public Image getImageForInventoryItem(InventoryItem item) throws SlickException {
    if (RockPickItem.class.isInstance(item)) {
      return getOrLoadInventoryItemImage(RockPickItem.class, 1, 3);
    } if (CopperPickItem.class.isInstance(item)) {
      return getOrLoadInventoryItemImage(CopperPickItem.class, 1, 3);
    } else if (DynamiteItem.class.isInstance(item)) {
      return getOrLoadInventoryItemImage(DynamiteItem.class, 0, 3);
    } else if (CopperItem.class.isInstance(item)) {
      return getOrLoadInventoryItemImage(CopperItem.class, 1, 2);
    } else if (CoalItem.class.isInstance(item)) {
      return getOrLoadInventoryItemImage(CoalItem.class, 1, 1);
    } if (TorchItem.class.isInstance(item)) {
      return getOrLoadInventoryItemImage(TorchItem.class, 2, 0);
    }
    
    throw new SlickException("No image for inventory item: " +item.getClass().getName());    
  }
  
  
}
