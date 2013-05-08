package com.macbury.unamed.inventory;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.macbury.unamed.Core;
import com.macbury.unamed.entity.CollectableItem;
import com.macbury.unamed.entity.Entity;
import com.macbury.unamed.entity.Player;


public abstract class InventoryItem {
  public static final int STANDARD_HARVEST_POWER = 1;
  public static final int INFINITY_COUNT = -666;
  protected int elementCount = 0;
  protected InventoryItemType itemType = InventoryItemType.Resource;

  public InventoryItem() {
    this.elementCount = 1;
  }
  
  public abstract String getKey();
  
  public abstract String getName();
  public abstract boolean place(Vector2f tilePos) throws SlickException;
  public abstract int harvestPower();
  public int getCount() {
    if (isInfinity()) {
      return 99;
    } else {
      return elementCount;
    }
  }
  
  public void addItem(int quantity) {
    if (!isInfinity()) {
      this.elementCount += quantity;
    }
  }
  
  public void setItemCount(int quantity) {
    if (!isInfinity()) {
      this.elementCount = quantity;
    }
  }
  
  public void popItem() throws SlickException {
    if (!isInfinity()) {
      this.elementCount--;
    }
    
    if (!haveItems()) {
      Core.log(getClass(), "Dont have " + getName() + " removing");
      InventoryManager.shared().remove(this);
    }
  }
  
  public boolean isInfinity() {
    return elementCount == INFINITY_COUNT;
  }
  
  public boolean haveItems() {
    return elementCount > 0 || isInfinity() ;
  }

  public int getQuantity() {
    return elementCount;
  }

  public void writeTo(Kryo kryo, Output output) {
    kryo.writeClass(output, this.getClass());
    output.writeInt(this.elementCount);
    if (BlockItem.class.isInstance(this)) {
      BlockItem bi = (BlockItem) this;
      kryo.writeClass(output, bi.getBlockType());
    }
  }

  public static InventoryItem loadFrom(Kryo kryo, Input input) {
    Class<? extends InventoryItem> itemKlass = kryo.readClass(input).getType();
    InventoryItem item = null;
    try {
      item = itemKlass.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    item.setItemCount(input.readInt());
    
    if (BlockItem.class.isInstance(item)) {
      BlockItem bi = (BlockItem) item;
      bi.setBlockType(kryo.readClass(input).getType());
    }
    return item;
  }
  
  
  public InventoryItemType getItemType() {
    return itemType;
  }

  public void setItemType(InventoryItemType itemType) {
    this.itemType = itemType;
  }

}
