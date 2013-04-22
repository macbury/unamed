package com.macbury.unamed.entity;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.macbury.unamed.component.CharacterAnimation;
import com.macbury.unamed.component.HealthComponent;
import com.macbury.unamed.component.KeyboardMovement;
import com.macbury.unamed.component.Light;
import com.macbury.unamed.component.TileBasedMovement;

public abstract class Character extends Entity {
  public static final int ENTITY_ZINDEX    = Entity.ENTITY_BASE_LAYER+1;
  private static final short START_HEALTH   = 20;
  TileBasedMovement  tileMovement;
  CharacterAnimation charactedAnimation;
  public Character() throws SlickException {
    super();
    this.collidable = true;
    this.solid      = true;
    this.z = ENTITY_ZINDEX;
    
    charactedAnimation = new CharacterAnimation();
    addComponent(charactedAnimation);

    tileMovement = new TileBasedMovement();
    addComponent(tileMovement);
    
    addComponent(new HealthComponent(START_HEALTH));
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
  public void writeTo(Kryo kryo, Output output) {
    super.writeTo(kryo, output);
    
    output.writeShort(this.getHealth().getMaxHelath());
    output.writeShort(this.getHealth().getHealth());

    output.writeByte(tileMovement.getDirection());
  }

  @Override
  public void loadFrom(Kryo kryo, Input input) {
    super.loadFrom(kryo, input);
    
    this.getHealth().setMaxHelath(input.readShort());
    this.getHealth().setHealth(input.readShort());
    tileMovement.direction = input.readByte();
  }
}
