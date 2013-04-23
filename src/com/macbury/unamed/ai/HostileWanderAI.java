package com.macbury.unamed.ai;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;

import com.macbury.unamed.Timer;
import com.macbury.unamed.TimerInterface;

public class HostileWanderAI extends WanderAI implements TimerInterface {
  private static final short LOOK_LOOP_TIME = 500;
  Timer lookIfICanSeePlayerTimer;
  
  public HostileWanderAI() {
    super();
    lookIfICanSeePlayerTimer = new Timer(LOOK_LOOP_TIME, this);
  }
  
  @Override
  public void update(int delta) throws SlickException {
    lookIfICanSeePlayerTimer.update(delta);
    super.update(delta);
  }

  @Override
  public void onStart() throws SlickException {
    super.onStart();
    lookIfICanSeePlayerTimer.start();
  }

  @Override
  public void onStop() throws SlickException {
    super.onStop();
    lookIfICanSeePlayerTimer.stop();
  }

  @Override
  public void onTimerFire(Timer timer) {
    if (canISeePlayer()) {
      Log.info("I see you :P");
      this.randomMovement.enabled = false;
    } else {
      randomMovement.enabled = true;
    }
  }
}
