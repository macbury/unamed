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
    this.setTime(fireEveryMiliseconds);
    this.setDelegate(delegate);
  }
  
  public Timer() {
    // TODO Auto-generated constructor stub
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
        fire();
      }
    }
  }

  public void fire() throws SlickException {
    delegate.onTimerFire(this);
    time = 0;
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

  public void startAndFire() throws SlickException {
    this.start();
    this.fire();
  }

  public void setTime(short speed) {
    this.maxTime  = speed;
    this.time = 0;
  }

  public void setTime(long val) {
    this.setTime((short)val);
  }

  public boolean running() {
    return this.enabled;
  }

  public void startAndFireUnlessRunning() throws SlickException {
    if (!this.running()) {
      this.fire();
    }
    this.start();
  }
}
