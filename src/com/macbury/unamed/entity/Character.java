package com.macbury.unamed.entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.macbury.unamed.ai.AI;
import com.macbury.unamed.component.CharacterAnimation;
import com.macbury.unamed.component.Direction;
import com.macbury.unamed.component.HealthComponent;
import com.macbury.unamed.component.KeyboardMovement;
import com.macbury.unamed.component.Light;
import com.macbury.unamed.component.TileBasedMovement;
import com.macbury.unamed.intefrace.InterfaceManager;

public abstract class Character extends Entity {
  public static final int ENTITY_ZINDEX    = Entity.ENTITY_BASE_LAYER+1;
  private static final short START_HEALTH   = 20;
  TileBasedMovement  tileMovement;
  CharacterAnimation charactedAnimation;
  private AI ai;
  
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
      case Down:
        dy += 1;
      break;

      case Top:
        dy -= 1;
      break;
      
      case Left:
        dx -= 1;
      break;
      
      case Right:
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
    kryo.writeObject(output, tileMovement.getDirection());
  }

  @Override
  public void loadFrom(Kryo kryo, Input input) throws SlickException {
    super.loadFrom(kryo, input);
    
    this.getHealth().setMaxHelath(input.readShort());
    this.getHealth().setHealth(input.readShort());
    tileMovement.direction = kryo.readObject(input, Direction.class);
  }

  public AI getAi() {
    return ai;
  }

  public void setAi(AI ai) throws SlickException {
    ai.setOwner(this);
    if (this.ai != null && this.ai != ai) {
      this.ai.onStop();
      ai.onStart();
    } else {
      ai.onStart();
    }
    
    this.ai = ai;
  }
  
  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
    if (getAi() != null && !InterfaceManager.shared().shouldBlockGamePlay()) {
      getAi().update(delta);
    }
    super.update(gc, sb, delta);
  }
}
