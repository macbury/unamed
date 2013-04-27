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
import com.macbury.unamed.component.RandomMovement;
import com.macbury.unamed.component.TilePathFollowComponent;
import com.macbury.unamed.npc.PlayerTriggers;
import com.macbury.unamed.util.MonsterManager;

public class Monster extends Character implements PlayerTriggers {
  private static final float MONSTER_DEFAULT_SPEED = 0.0020f;
  public RandomMovement randomMovement;
  public Monster() throws SlickException {
    super();
    randomMovement = new RandomMovement();
    this.addComponent(randomMovement);
    this.addComponent(new TilePathFollowComponent());
    
    tileMovement.speed = MONSTER_DEFAULT_SPEED;
    tileMovement.playSoundForStep = false;
    //setConfig();
  }

  @Override
  public void onActionButton(Player player) throws SlickException {
    this.getHealth().applyDamage(new Damage(10));
  }

  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
    super.update(gc, sb, delta);
    
    if(this.getHealth().isDead()) {
      this.destroy();
    }
  }


  public void setConfig(JSONObject jsonObject) throws SlickException {
    this.charactedAnimation.loadCharacterImage("chars/monster");
    this.getHealth().setMaxHelath((short) 10);
    MonsterManager.shared().push(this);
    this.setAi(new HostileWanderAI());
    
    tileMovement.speed = 0.0020f;
  }

  @Override
  public void writeTo(Kryo kryo, Output output) {
    super.writeTo(kryo, output);
    output.writeString("type");
  }

  @Override
  public void loadFrom(Kryo kryo, Input input) throws SlickException {
    super.loadFrom(kryo, input);
    String type = input.readString();
  }

  @Override
  public void destroy() {
    super.destroy();
    MonsterManager.shared().remove(this);
  }
  
  
}
