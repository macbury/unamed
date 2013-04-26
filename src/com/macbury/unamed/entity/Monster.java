package com.macbury.unamed.entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.macbury.unamed.ai.HostileWanderAI;
import com.macbury.unamed.combat.Damage;
import com.macbury.unamed.component.RandomMovement;
import com.macbury.unamed.component.TilePathFollowComponent;
import com.macbury.unamed.npc.PlayerTriggers;

public class Monster extends Character implements PlayerTriggers {
  private static final float MONSTER_DEFAULT_SPEED = 0.0020f;
  public RandomMovement randomMovement;
  
  public Monster() throws SlickException {
    super();
    this.charactedAnimation.loadCharacterImage("chars/monster");
    randomMovement = new RandomMovement();
    this.addComponent(randomMovement);
    this.addComponent(new TilePathFollowComponent());
    
    tileMovement.speed = MONSTER_DEFAULT_SPEED;
    tileMovement.playSoundForStep = false;

    this.setAi(new HostileWanderAI());
    this.getHealth().setMaxHelath((short) 50);
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
  
  

}
