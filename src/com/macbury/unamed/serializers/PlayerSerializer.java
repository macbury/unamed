package com.macbury.unamed.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.macbury.unamed.entity.Entity;

public class PlayerSerializer extends EntitySerializer {

  @Override
  public Entity read(Kryo kryo, Input input, Class<Entity> klass) {
    // TODO Auto-generated method stub
    return super.read(kryo, input, klass);
  }

  @Override
  public void write(Kryo kryo, Output output, Entity entity) {
    // TODO Auto-generated method stub
    super.write(kryo, output, entity);
  }
  
}
