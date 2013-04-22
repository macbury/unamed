package com.macbury.unamed.serializers;

import org.newdawn.slick.SlickException;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.macbury.unamed.component.TileBasedMovement;
import com.macbury.unamed.entity.Entity;
import com.macbury.unamed.entity.Player;

public class PlayerSerializer extends EntitySerializer {

  @Override
  public Entity read(Kryo kryo, Input input, Class<Entity> klass) {
    Player player = null;
    try {
      player = new Player();
    } catch (SlickException e) {
      e.printStackTrace();
    }

    
    return player;
  }

  @Override
  public void write(Kryo kryo, Output output, Entity entity) {
    super.write(kryo, output, entity);
    
    Player player = (Player)entity;
    
    
  }
  
}
