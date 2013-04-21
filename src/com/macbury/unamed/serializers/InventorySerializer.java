package com.macbury.unamed.serializers;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.macbury.unamed.inventory.BlockItem;
import com.macbury.unamed.inventory.InventoryItem;
import com.macbury.unamed.inventory.InventoryManager;

public class InventorySerializer extends Serializer<InventoryManager> {

  @Override
  public InventoryManager read(Kryo kryo, Input input, Class<InventoryManager> klass) {
    Log.info("Loading inventory");
    InventoryManager manager = null;
    try {
      manager = new InventoryManager();
    } catch (SlickException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    int itemCount = input.readInt();
    int currentHotBarIndex = input.readInt();
    
    while(itemCount > 0) {
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
      
      manager.add(item);
      itemCount--;
    }
    
    manager.setInventoryIndex(currentHotBarIndex);
    
    return manager;
  }

  @Override
  public void write(Kryo kryo, Output out, InventoryManager inventory) {
    Log.info("Saving inventory");
    out.writeInt(inventory.size());
    out.writeInt(inventory.getCurrentHotBarIndex());
    for (InventoryItem inventoryItem : inventory) {
      kryo.writeClass(out, inventoryItem.getClass());
      out.writeInt(inventoryItem.getQuantity());
      if (BlockItem.class.isInstance(inventoryItem)) {
        BlockItem bi = (BlockItem) inventoryItem;
        kryo.writeClass(out, bi.getBlockType());
      }
    }
  }

}
