package com.macbury.unamed.component;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

public class HealthComponent extends Component {
  private short health = 0;
  
  public HealthComponent(short startHealth) {
    this.setHealth(startHealth);
  }
  
  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
    
  }

  @Override
  public void render(GameContainer gc, StateBasedGame sb, Graphics gr) {
    
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
  
  public void applyDamage(short damage) {
    this.health -= damage;
    Log.debug("Apply damage: " + damage);
  }

}
