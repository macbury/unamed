package com.macbury.unamed.serializers;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.macbury.unamed.Core;
import com.macbury.unamed.inventory.BlockItem;
import com.macbury.unamed.inventory.InventoryItem;
import com.macbury.unamed.inventory.InventoryManager;

public class InventorySerializer extends Serializer<InventoryManager> {

  @Override
  public InventoryManager read(Kryo kryo, Input input, Class<InventoryManager> klass) {
    Core.log(this.getClass(),"Loading inventory");
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
      InventoryItem item = InventoryItem.loadFrom(kryo, input);
      
      manager.add(item);
      itemCount--;
    }
    
    manager.setInventoryIndex(currentHotBarIndex);
    
    return manager;
  }

  @Override
  public void write(Kryo kryo, Output out, InventoryManager inventory) {
    Core.log(this.getClass(),"Saving inventory");
    out.writeInt(inventory.size());
    out.writeInt(inventory.getCurrentHotBarIndex());
    for (InventoryItem inventoryItem : inventory) {
      inventoryItem.writeTo(kryo, out);
    }
  }

}
