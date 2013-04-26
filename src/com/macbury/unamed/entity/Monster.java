package com.macbury.unamed.entity;

import org.ini4j.Wini;
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
  private Wini config;
  
  public Monster() throws SlickException {
    super();
    randomMovement = new RandomMovement();
    this.addComponent(randomMovement);
    this.addComponent(new TilePathFollowComponent());
    
    tileMovement.speed = MONSTER_DEFAULT_SPEED;
    tileMovement.playSoundForStep = false;

    this.setAi(new HostileWanderAI());
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

  public Wini getConfig() {
    return config;
  }

  public void setConfig(Wini config) throws SlickException {
    this.config = config;
    this.charactedAnimation.loadCharacterImage("chars/"+config.get(MonsterManager.BASE_GROUP, MonsterManager.BASE_IMAGE));
    this.getHealth().setMaxHelath(config.get(MonsterManager.BASE_GROUP, MonsterManager.BASE_HEALTH, Short.class));
    MonsterManager.shared().push(this);
  }

  @Override
  public void writeTo(Kryo kryo, Output output) {
    super.writeTo(kryo, output);
    output.writeString(this.getConfig().getFile().getName());
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
