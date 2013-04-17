package com.macbury.unamed;

public class Timer {
  private short maxTime;
  private short time;
  private TimerInterface delegate;
  private boolean enabled = true;
  public Timer(short fireEveryMiliseconds, TimerInterface delegate) {
    this.maxTime  = fireEveryMiliseconds;
    this.delegate = delegate;
  }
  
  public void update(int delta) {
    if (this.enabled) {
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
}
