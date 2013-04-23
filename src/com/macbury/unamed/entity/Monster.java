package com.macbury.unamed.entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.util.pathfinding.Mover;
import org.newdawn.slick.util.pathfinding.Path;

import com.macbury.unamed.PathFindingCallback;
import com.macbury.unamed.PathFindingQueue;
import com.macbury.unamed.Timer;
import com.macbury.unamed.TimerInterface;
import com.macbury.unamed.ai.WanderAI;
import com.macbury.unamed.component.RandomMovement;

public class Monster extends Character implements TimerInterface, PathFindingCallback {
  private static final byte STATE_WANDER           = 0;
  private static final byte STATE_FOLLOW_PLAYER    = 1;
  private static final byte STATE_SEEK_PLAYER      = 2;
  private static final float MONSTER_DEFAULT_SPEED = 0.0020f;
  private static final short FIND_PATH_EVERY       = 250;
  private byte state = STATE_FOLLOW_PLAYER;
  public RandomMovement randomMovement;
  
  private Timer findPathTimer;
  private Path currentPath;
  
  public Monster() throws SlickException {
    super();
    this.charactedAnimation.loadCharacterImage("chars/monster");
    randomMovement = new RandomMovement();
    this.addComponent(randomMovement);
    
    tileMovement.speed = MONSTER_DEFAULT_SPEED;
    tileMovement.playSoundForStep = false;
    
    findPathTimer = new Timer(FIND_PATH_EVERY, this);
    findPathTimer.stop();
    
    this.setAi(new WanderAI());
  }

  @Override
  public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
    super.update(gc, sb, delta);
    findPathTimer.update(delta);
  }
  
  @Override
  public void onTimerFire(Timer timer) {
    timer.stop();
    PathFindingQueue.shared().findPathToPlayer(this, this);
  }

  @Override
  public void onPathFound(Path path) {
    if (path == null) {
      this.setState(STATE_WANDER);
    } else {
      Log.info("Path lenght: " + path.getLength());
      this.setState(STATE_FOLLOW_PLAYER);
    }
    
    currentPath = path;
  }
  
  public void setState(byte state) {
    this.state = state;
    
    if (this.state != STATE_FOLLOW_PLAYER) {
      currentPath = null;
    }
    
    if (this.state == STATE_SEEK_PLAYER) {
      findPathTimer.setEnabled(true);
    }
  }
}
