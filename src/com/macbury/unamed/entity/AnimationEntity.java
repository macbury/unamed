package com.macbury.unamed.entity;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.macbury.unamed.AnimationManager;
import com.macbury.unamed.Timer;
import com.macbury.unamed.TimerInterface;
import com.macbury.unamed.component.AnimatedSprite;
import com.macbury.unamed.inventory.PickItem;

public class AnimationEntity extends ReusableEntity implements TimerInterface {
  public final static int ENTITY_ZINDEX = Character.ENTITY_ZINDEX + 1;
  private Timer timer;
  
  public AnimationEntity() throws SlickException {
    super();
    
    this.z = ENTITY_ZINDEX;
    this.timer = new Timer((short)250, this);
    
    addComponent(new AnimatedSprite(AnimationManager.shared().biteAnimation));
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

  @Override
  public void onReuse() {
    this.timer.restart();
  }
  
  public void setAnimation(Animation animation) {
    AnimatedSprite sprite = (AnimatedSprite) getComponent(AnimatedSprite.class);
    sprite.setCurrentAnimation(animation);
    //his.timer.setTime((short) animation.getDuration(0));
  }
}
