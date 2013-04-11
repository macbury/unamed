package com.macbury.unamed.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.macbury.unamed.entity.Entity;

public class EntitySerializer extends Serializer<Entity> {

  @Override
  public Entity read(Kryo kryo, Input input, Class<Entity> klass) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void write(Kryo kryo, Output output, Entity entity) {
    output.writeInt(entity.getId());
    output.writeFloat(entity.getX());
    output.writeFloat(entity.getY());
  }

}
