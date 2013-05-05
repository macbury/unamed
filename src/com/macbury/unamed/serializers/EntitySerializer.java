package com.macbury.unamed.serializers;

import java.util.logging.Logger;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.macbury.unamed.Core;
import com.macbury.unamed.entity.Entity;

public class EntitySerializer extends Serializer<Entity> {
  
  public Entity setInfoFromInputFor(Input input, Entity entity) {
    entity.setId(input.readInt());
    entity.setX(input.readFloat());
    entity.setY(input.readFloat());
     
    return entity;
  }
  
  @Override
  public Entity read(Kryo kryo, Input input, Class<Entity> klass) {
    try {
      throw new SlickException("Cannot load bare entity");
    } catch (SlickException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    return null;
  }

  @Override
  public void write(Kryo kryo, Output output, Entity entity) {
    output.writeInt(entity.getId());
    output.writeFloat(entity.getX());
    output.writeFloat(entity.getY());
    Core.log(this.getClass(),"Writing entity: " + entity.toString());
  }

}
