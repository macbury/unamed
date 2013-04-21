package com.macbury.unamed.entity;

import java.sql.Time;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.macbury.unamed.AnimationManager;
import com.macbury.unamed.Timer;
import com.macbury.unamed.TimerInterface;
import com.macbury.unamed.component.AnimatedSprite;

public class DigEffectEntity extends Entity implements TimerInterface {
  public final static int ENTITY_ZINDEX = Character.ENTITY_ZINDEX;
  private Timer timer;
  public DigEffectEntity() throws SlickException {
    super();
    this.z = ENTITY_ZINDEX;
    this.timer = new Timer((short)Player.MAX_TAKING_TIME, this);
    addComponent(new AnimatedSprite(AnimationManager.shared().punchAnimation));
  }

  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
    super.update(gc, sb, delta);
    this.timer.update(delta);
  }

  @Override
  public void onTimerFire(Timer timer) {
    this.destroy();
  }
}
