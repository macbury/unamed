package com.macbury.unamed.ai;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.util.pathfinding.Path;

import com.macbury.unamed.PathFindingCallback;
import com.macbury.unamed.PathFindingQueue;
import com.macbury.unamed.Position;
import com.macbury.unamed.Timer;
import com.macbury.unamed.TimerInterface;
import com.macbury.unamed.level.Level;

public class HostileWanderAI extends WanderAI implements TimerInterface, PathFindingCallback {
  private static final short LOOK_LOOP_TIME = 100;
  Timer lookIfICanSeePlayerTimer;
  private Position lastSeenTargetAt;
  private Path pathToLastSeenTargetPosition;
  
  public HostileWanderAI() {
    super();
    lookIfICanSeePlayerTimer = new Timer(LOOK_LOOP_TIME, this);
  }
  
  @Override
  public void update(int delta) throws SlickException {
    lookIfICanSeePlayerTimer.update(delta);
    super.update(delta);
    
    if (this.getTarget() != null) {
      this.randomMovement.enabled = false;
      if (!this.tileMovement.isMoving()) {
        
        //if (this.getOwner().distanceTo(this.getTarget()) > 4) {
          //PathFindingQueue.shared().findPathToEntity(this.getOwner(), this.getTarget(), this);
       // } else {
          this.tileMovement.lookAt(this.getTarget());
          this.tileMovement.moveForward();
       // }
      }
    } else {
      if (this.pathToLastSeenTargetPosition != null) {
        this.randomMovement.enabled = false;
        
      } if (this.lastSeenTargetAt != null) {
        PathFindingQueue.shared().findPathToPosition(this.getOwner(), this.lastSeenTargetAt, this);
      } else {
        this.randomMovement.enabled = true;
      }
    }
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
    if (lookIfICanSeePlayerTimer == timer) {
      if (canISeePlayer()) {
        this.pathToLastSeenTargetPosition = null;
        this.lastSeenTargetAt             = null;
        setTarget(Level.shared().getPlayer());
      } else {
        if (getTarget() != null) {
          this.lastSeenTargetAt = this.getTarget().getPosition();
        }
        setTarget(null);
      }
    }
  }

  @Override
  public void onPathFound(Path path) {
    this.lastSeenTargetAt = null;
    if (path != null) {
      this.pathToLastSeenTargetPosition = path;
      Log.info("Found path: " + path.getLength());
    } else {
      Log.info("No path found!");
    }
  }
}
