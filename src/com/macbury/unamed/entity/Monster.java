package com.macbury.unamed.entity;

import org.json.simple.JSONObject;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.macbury.unamed.ai.HostileWanderAI;
import com.macbury.unamed.combat.Damage;
import com.macbury.unamed.component.Light;
import com.macbury.unamed.component.RandomMovement;
import com.macbury.unamed.component.TilePathFollowComponent;
import com.macbury.unamed.npc.PlayerTriggers;
import com.macbury.unamed.util.MonsterManager;

public class Monster extends Character {
  private static final float MONSTER_DEFAULT_SPEED = 0.0020f;
  public RandomMovement randomMovement;
  private String name;
  
  
  public Monster() throws SlickException {
    super();
    randomMovement = new RandomMovement();
    this.addComponent(randomMovement);
    this.addComponent(new TilePathFollowComponent());
    
    tileMovement.speed = MONSTER_DEFAULT_SPEED;
    tileMovement.playSoundForStep = false;
  }

  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
    super.update(gc, sb, delta);
    
    if(this.getHealth().isDead()) {
      this.destroy();
    }
  }

  public void setConfig(JSONObject jsonObject) throws SlickException {
    this.name = (String)jsonObject.get("name");
    
    JSONObject base = (JSONObject) jsonObject.get("base");
    long health = (long) base.get("health");
    
    this.charactedAnimation.loadCharacterImage("chars/"+base.get("image"));
    
    this.getHealth().setMaxHelath((short)health);
    MonsterManager.shared().push(this);
    HostileWanderAI ai = new HostileWanderAI();
    ai.setConfig(jsonObject);
    this.setAi(ai);
    
    JSONObject move = (JSONObject) jsonObject.get("move");
    double speed = (double) move.get("speed");
    tileMovement.speed = (float)speed;
    tileMovement.flying = (boolean) move.get("fly");
  }

  @Override
  public void writeTo(Kryo kryo, Output output) {
    super.writeTo(kryo, output);
    output.writeString(this.name);
  }

  @Override
  public void loadFrom(Kryo kryo, Input input) throws SlickException {
    super.loadFrom(kryo, input);
    this.setConfig(MonsterManager.shared().get(input.readString()));
  }

  @Override
  public void destroy() {
    super.destroy();
    MonsterManager.shared().remove(this);
  }
}
