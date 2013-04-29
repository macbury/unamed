package com.macbury.unamed.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

import com.macbury.unamed.Core;
import com.macbury.unamed.Timer;
import com.macbury.unamed.TimerInterface;
import com.macbury.unamed.combat.Damage;
import com.macbury.unamed.intefrace.InterfaceManager;

public class HealthComponent extends Component implements TimerInterface {
  private short health    = 1;
  private short maxHelath = 1;
  private float regenerateFactor = 0.1f;
  private ArrayList<Damage> damages;
  private Damage lastDamage = null;
  private final static short DAMAGE_ANIMATION_LIFESPAN = 500;
  private final static short DAMAGE_ANIMATION_OFFSET_Y = -34;
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
    lastDamage         = null;
    boolean tookDamage = false;
    if (this.damages != null && this.damages.size() > 0) {
      for (int i = 0; i < damages.size(); i++) {
        Damage damage = damages.get(i);
        short time    = damage.getLifeTime();
        time -= delta;
        damage.setLifeTime(time);
        if (time < 0) {
          this.damages.remove(damage);
        }
        
      }
      tookDamage = true;
    } else {
      tookDamage = false;
    }
    
    regenerateTimer.setEnabled(!tookDamage);
  }

  @Override
  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) throws SlickException {
    if (this.damages != null && this.damages.size() > 0) {
      for (int i = 0; i < damages.size(); i++) {
        Damage damage = damages.get(i);
        Core.instance().getFont().drawString(0, (float)DAMAGE_ANIMATION_OFFSET_Y - DAMAGE_ANIMATION_OFFSET_Y * ((float)damage.getLifeTime() / (float)DAMAGE_ANIMATION_LIFESPAN), "- "+damage.getPower());
      }
    }
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
  
  public void applyDamage(Damage damage) {
    this.health -= damage.getPower();
    if (this.health < 0) {
      this.health = 0;
    }
    this.addDamage(damage);
    Log.debug("Apply damage: " + damage.getPower());
  }

  private void addDamage(Damage damage) {
    if (this.damages == null) {
      this.damages = new ArrayList<Damage>();
    }
    if (lastDamage != null) {
      lastDamage.addPower(damage.getPower());
    } else {
      lastDamage = damage;
      damage.setLifeTime(DAMAGE_ANIMATION_LIFESPAN);
      damages.add(damage);
    }
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
