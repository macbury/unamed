package com.macbury.unamed.entity;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.macbury.unamed.Timer;
import com.macbury.unamed.TimerInterface;
import com.macbury.unamed.component.TextComponent;
import com.macbury.unamed.intefrace.InterfaceManager;
import com.macbury.unamed.level.Level;

public class TextParticle extends ReusableEntity implements TimerInterface  {
  private static final short PARTICLE_LIFE_TIME = 600;
  static private float SPEED = 0.1f; 
  TextComponent textComponent;
  
  Timer destroyTimer;
  
  public float xa, ya, za;
  public float xx, yy, zz;
  private boolean bootstrap;
  
  public TextParticle() throws SlickException {
    super();
    destroyTimer = new Timer(PARTICLE_LIFE_TIME, this);
    textComponent = new TextComponent();
    addComponent(textComponent);
    destroyTimer.start();
    destroyTimer.setIsPausableEvent(true);
    this.bootstrap = true;
  } 
  
  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
    super.update(gc, sb, delta);
    destroyTimer.update(delta);
    if (InterfaceManager.shared().isNormalGameplay()) {
      if (bootstrap) {
        bootstrap = false;
        xx = getX();
        yy = getY();
        zz = 2;
        
        xa = (float) (Level.shared().random.nextGaussian() * 0.3);
        ya = (float) (Level.shared().random.nextGaussian() * 0.2);
        za = (float) (Level.shared().random.nextFloat() * 0.7 + 2);
      }
      
      xx += xa * delta * SPEED;
      yy += ya * delta * SPEED;
      zz += za * delta * SPEED;
      if (zz < 0) {
        zz = 0;
        za *= -0.5;
        xa *= 0.6;
        ya *= 0.6;
      }
      za -= 0.15 * delta * SPEED;
      this.setX(xx);
      this.setY(yy);
      this.textComponent.setY(-zz);
    }
  }

  @Override
  public void onReuse() {
    this.destroyTimer.restart();
    bootstrap = true;
    this.textComponent.setX(0);
    this.textComponent.setY(0);
  }

  public String getMsg() {
    return textComponent.getText();
  }

  public void setMsg(String msg) {
    this.textComponent.setText(msg);
  }
  
  public void setColor(Color color) {
    this.textComponent.setColor(color);
  }
  
  @Override
  public void onTimerFire(Timer timer) throws SlickException {
    this.destroy();
  }
  
  public static void spawnTextFor(String string, Entity target, Color color) throws SlickException {
    TextParticle.spawnTextAt(string, (int)target.getCenteredPosition().getX(), (int)target.getCenteredPosition().getY(), color);
  }
  
  public static void spawnTextAt(String string, int x, int y, Color color) throws SlickException {
    TextParticle particle = (TextParticle)Level.shared().getUsedEntity(TextParticle.class);
    particle.setMsg(string);
    particle.setPosition(x, y);
    particle.setColor(color);
    Level.shared().addEntity(particle);
  }

  @Override
  public boolean allowInteraction() {
    return false;
  }


}
