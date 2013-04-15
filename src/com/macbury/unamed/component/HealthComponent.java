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
import com.macbury.unamed.combat.Damage;

public class HealthComponent extends Component {
  private short health    = 1;
  private short maxHelath = 1;
  private ArrayList<Damage> damages;
  private Damage lastDamage = null;
  private final static short DAMAGE_ANIMATION_LIFESPAN = 500;
  private final static short DAMAGE_ANIMATION_OFFSET_Y = -34;
  
  public HealthComponent(short startHealth) {
    this.setMaxHelath(startHealth);
    this.setHealth(startHealth);
  }
  
  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
    lastDamage = null;
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
    }
  }

  @Override
  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) throws SlickException {
    if (this.damages != null && this.damages.size() > 0) {
      for (int i = 0; i < damages.size(); i++) {
        Damage damage = damages.get(i);
        Core.instance().getFont().drawString(0, DAMAGE_ANIMATION_OFFSET_Y - DAMAGE_ANIMATION_OFFSET_Y * ((float)damage.getLifeTime() / (float)DAMAGE_ANIMATION_LIFESPAN), "- "+damage.getPower());
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
    Log.debug("Apply damage: " + damage);
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
  }
  
  public float getHealthProgress() {
    return ((float)this.getHealth() / (float)this.getMaxHelath());
  }

}
