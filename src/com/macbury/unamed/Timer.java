package com.macbury.unamed;

import org.newdawn.slick.SlickException;

import com.macbury.unamed.intefrace.InterfaceManager;

public class Timer {
  private short maxTime;
  private short time;
  private TimerInterface delegate;
  private boolean enabled = true;
  private boolean isPausableEvent = false;
  
  public Timer(short fireEveryMiliseconds, TimerInterface delegate) {
    this.maxTime  = fireEveryMiliseconds;
    this.delegate = delegate;
  }
  
  public void setIsPausableEvent(boolean flag) {
    this.isPausableEvent = flag;
  }
  
  public boolean canUpdate() throws SlickException {
    if (this.isPausableEvent) {
      return !InterfaceManager.shared().shouldBlockGamePlay();
    } else {
      return true;
    }
  }
  
  public void update(int delta) throws SlickException {
    if (this.enabled && canUpdate()) {
      time += delta;
      if (time > maxTime) {
        delegate.onTimerFire(this);
        time = 0;
      }
    }
  }

  public TimerInterface getDelegate() {
    return delegate;
  }

  public void setDelegate(TimerInterface delegate) {
    this.delegate = delegate;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
    if (!this.enabled) {
      this.time = 0;
    }
  }

  public void stop() {
    setEnabled(false);
  }
  
  public void restart() {
    setEnabled(false);
    setEnabled(true);
  }
  
  public void start() {
    setEnabled(true);
  }

  public short getTime() {
    return time;
  }
}
