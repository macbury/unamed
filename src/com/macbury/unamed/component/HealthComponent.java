package com.macbury.unamed.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

import com.macbury.unamed.Core;
import com.macbury.unamed.Timer;
import com.macbury.unamed.TimerInterface;
import com.macbury.unamed.combat.Damage;
import com.macbury.unamed.entity.HUDComponentInterface;
import com.macbury.unamed.entity.TextParticle;
import com.macbury.unamed.intefrace.InterfaceManager;
import com.macbury.unamed.level.Level;

public class HealthComponent extends Component implements TimerInterface {
  private short health    = 1;
  private short maxHelath = 1;
  private float regenerateFactor = 0.1f;
  private final static short REGENERATE_EVERY = 1500;
  private Timer regenerateTimer;
  
  public HealthComponent(short startHealth) {
    this.setMaxHelath(startHealth);
    this.setHealth(startHealth);
    regenerateTimer = new Timer(REGENERATE_EVERY, this);
    regenerateTimer.setIsPausableEvent(true);
  }
  
  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
    regenerateTimer.update(delta);
    regenerateTimer.setEnabled(true);
  }

  @Override
  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) throws SlickException {
    
  }

  public short getHealth() {
    return health;
  }

  public void setHealth(short health) {
    this.health = health;
  }
  
  public boolean isDead() {
    return this.health <= 0;
  }
  
  public void applyDamage(Damage damage) throws SlickException {
    this.health -= damage.getPower();
    if (this.health < 0) {
      this.health = 0;
    }
    
    Color color = (Level.shared().getPlayer() == this.owner) ? Color.orange : Color.red;
    TextParticle.spawnTextFor("-"+damage.getPower(), this.owner, color);
    Log.debug("Apply damage: " + damage.getPower());
  }

  public short getMaxHelath() {
    return maxHelath;
  }

  public void setMaxHelath(short maxHelath) {
    this.maxHelath = maxHelath;
    this.health    = this.maxHelath;
  }
  
  public float getHealthProgress() {
    return ((float)this.getHealth() / (float)this.getMaxHelath());
  }

  public float getRegenerateFactor() {
    return regenerateFactor;
  }

  public void setRegenerateFactor(float regenerateFactor) {
    this.regenerateFactor = regenerateFactor;
  }

  public short getHealthDiff() {
    return (short) (this.maxHelath - this.health);
  }
  
  @Override
  public void onTimerFire(Timer timer) {
    if (timer == regenerateTimer) {
      this.health += Math.min((float)this.maxHelath* this.regenerateFactor, 1);
    }
    if (this.health > this.maxHelath) {
      this.health = this.maxHelath;
    }
  }

}
