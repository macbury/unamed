package com.macbury.unamed;

import java.util.Iterator;
import java.util.Stack;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Path;
import org.newdawn.slick.util.pathfinding.PathFinder;
import org.newdawn.slick.util.pathfinding.PathFindingContext;
import org.newdawn.slick.util.pathfinding.TileBasedMap;
import org.newdawn.slick.util.pathfinding.heuristics.ManhattanHeuristic;

import com.macbury.unamed.entity.Entity;
import com.macbury.unamed.level.Level;

public class PathFindingQueue implements TileBasedMap, Runnable {
  public  static PathFindingQueue shared    = null;
  private static final int MAX_PATH_LENGTH  = 50;
  private static final long SLEEP_TIME      = 100;
  private PathFinder pathFinder             = null;
  private Thread currentThread;
  
  private Stack<PathFindQuery> queue;
  
  public static PathFindingQueue shared() {
    if (shared == null) {
      shared = new PathFindingQueue();
    }
    return shared;
  }
  
  public PathFindingQueue() {
    this.queue = new Stack<PathFindQuery>();
    this.currentThread = new Thread(this);
    this.currentThread.setPriority(Thread.MIN_PRIORITY);
    this.currentThread.start();
  }
  
  public PathFinder getPathFinder() {
    if (this.pathFinder == null) {
      this.pathFinder = new AStarPathFinder(this, MAX_PATH_LENGTH, false, new ManhattanHeuristic(1));
    }
    return this.pathFinder;
  }
  
  public void findPathToPlayer(Entity fromEntity, PathFindingCallback callback) {
    findPathToEntity(fromEntity, Level.shared().getPlayer(), callback);
  }
  
  public void findPathToEntity(Entity fromEntity, Entity targetEntity, PathFindingCallback callback) {
    findPathToPosition(fromEntity, targetEntity.getPosition(), callback);
  }
  
  public void findPathToPosition(Entity fromEntity, Position targetPosition, PathFindingCallback callback) {
    boolean foundQuery = false;
    for (int i = 0; i < this.queue.size(); i++) {
      PathFindQuery query = this.queue.get(i);
      if (query.callback == callback) {
        foundQuery = true;
        break;
      }
    }
    
    if (!foundQuery) {
      this.queue.push(new PathFindQuery(fromEntity.getTileX(), fromEntity.getTileY(), targetPosition.getTileX(), targetPosition.getTileY(), callback));
    }
  }
  
  public Path findPath(int sx, int sy, int ex, int ey) {
    Log.debug("Searching path from: "+ sx + "x" + sy + " to " + ex + "x" + ey);
    return getPathFinder().findPath(null, sx, sy, ex, ey);
  }

  @Override
  public boolean blocked(PathFindingContext context, int tx, int ty)  {
    return !Level.shared().getBlockForPosition(tx, ty).isPassable();
  }

  @Override
  public float getCost(PathFindingContext context, int tx, int ty)  {
    return Level.shared().getBlockForPosition(tx, ty).getCost();
  }

  @Override
  public int getHeightInTiles() {
    return Level.shared().mapTileHeight;
  }

  @Override
  public int getWidthInTiles() {
    return Level.shared().mapTileWidth;
  }

  @Override
  public void pathFinderVisited(int tx, int ty) {
    
  }

  @Override
  public void run() {
    Core.log(this.getClass(),"Initializing path finder...");
    getPathFinder();
    Core.log(this.getClass(),"Wating for data");
    while(true) {
      try {
        Thread.sleep(SLEEP_TIME);
        computeStack();
      } catch (InterruptedException e) {
        e.printStackTrace();
        Core.log(this.getClass(),"Stoping path finding queue");
      } catch (Exception e) {
        e.printStackTrace();
        exit();
      }
    }
  }
  
  private void computeStack() throws SlickException {
    if (this.queue.size() > 0) {
      Log.debug("Paths to find: "+ this.queue.size());
    }
    while(this.queue.size() > 0) {
      PathFindQuery query = this.queue.peek();
      Path path = findPath((int)query.fromPosition.getX(), (int)query.fromPosition.getY(), (int)query.toPosition.getX(), (int)query.toPosition.getY());
      query.callback.onPathFound(path);
      this.queue.remove(query);
    }
  }

  public static void exit() {
    Core.log(PathFindingQueue.class,"Exiting queue");
    if (shared != null) {
      shared.currentThread.interrupt();
      shared = null;
    }
  }

  
}
