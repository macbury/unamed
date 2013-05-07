package com.macbury.unamed.inventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import com.macbury.unamed.ImagesManager;
import com.macbury.unamed.SoundManager;
import com.macbury.unamed.attack.AttackBase;
import com.macbury.unamed.attack.PunchAttack;
import com.macbury.unamed.block.BlockResources;
import com.macbury.unamed.intefrace.InventoryItemMenuItem;
import com.macbury.unamed.intefrace.MenuItem;
import com.macbury.unamed.intefrace.MenuList;

public class InventoryManager extends ArrayList<InventoryItem> {
  private static InventoryManager shared;
  private static final long serialVersionUID = 1L;
  public final static int MIN_HOTBAR_INVENTORY_INDEX = 1;
  public final static int MAX_HOTBAR_INVENTORY_INDEX = 10;
  
  private SpriteSheet spriteSheet;
  private HashMap<String, Image> itemImageCache;
  
  private WeaponItem    weapon;
  private PickItem      harvest;
  private InventoryItem place;

  private PunchAttack defaultAttack;
  
  public static InventoryManager shared() throws SlickException {
    if (shared == null) {
      shared = new InventoryManager();
    }
    return shared;
  }
  
  public InventoryManager() throws SlickException {
    super();
    
    this.setSpriteSheet(ImagesManager.shared().iconsSpriteSheet);
    this.itemImageCache = new HashMap<>();
    InventoryManager.shared = this;
    defaultAttack = new PunchAttack();
  }
  
  public WeaponItem getWeapon() {
    return weapon;
  }

  public void setWeapon(WeaponItem weapon) {
    this.weapon = weapon;
  }
  
  public void update(int delta) throws SlickException {
    defaultAttack.update(delta);
    if (haveWeapon()) {
      getWeapon().update(delta);
    }
  }
  
  private boolean haveWeapon() {
    return getWeapon() != null;
  }
  
  
  public AttackBase getCurrentAttack() {
    if (haveWeapon()) {
      return getWeapon().getAttack();
    } else {
      return defaultAttack;
    }
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
    if (BlockItem.class.isInstance(item)) {
      BlockItem blockItem = (BlockItem) item;
      return BlockResources.shared().imageForBlockClass(blockItem.blockType);
    } else if (RockSwordItem.class.isInstance(item)) {
      return getOrLoadInventoryItemImage(RockSwordItem.class, 1, 4);
    } else if (RockPickItem.class.isInstance(item)) {
      return getOrLoadInventoryItemImage(RockPickItem.class, 1, 6);
    } else if (CopperPickItem.class.isInstance(item)) {
      return getOrLoadInventoryItemImage(CopperPickItem.class, 0, 6);
    } else if (DynamiteItem.class.isInstance(item)) {
      return getOrLoadInventoryItemImage(DynamiteItem.class, 14, 4);
    } else if (CopperItem.class.isInstance(item)) {
      return getOrLoadInventoryItemImage(CopperItem.class, 6, 1);
    } else if (CoalItem.class.isInstance(item)) {
      return getOrLoadInventoryItemImage(CoalItem.class, 7, 0);
    } if (TorchItem.class.isInstance(item)) {
      return getOrLoadInventoryItemImage(TorchItem.class, 12, 6);
    }
    
    throw new SlickException("No image for inventory item: " +item.getClass().getName());    
  }

  public MenuList getItemsListForMenu() {
    MenuList list = new MenuList();
    
    for (InventoryItem item : this) {
      list.add(new InventoryItemMenuItem(item));
    }
    
    return list;
  }

  public PickItem getHarvest() {
    return harvest;
  }

  public void setHarvest(PickItem harvest) {
    this.harvest = harvest;
  }

  public InventoryItem getPlace() {
    return place;
  }

  public void setPlace(InventoryItem place) {
    this.place = place;
  }

  public MenuList getItemTypes(InventoryItemType typ) {
    MenuList list = new MenuList();
    
    for (InventoryItem item : this) {
      if (item.getItemType() == typ) {
        list.add(new InventoryItemMenuItem(item));
      }
      
    }
    Collections.sort(list);
    return list;
  }
  
}
