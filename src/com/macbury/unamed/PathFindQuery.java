package com.macbury.unamed;

public class PathFindQuery {
  public Position fromPosition;
  public Position toPosition;
  public PathFindingCallback callback;
  
  public PathFindQuery(int sx, int sy, int ex, int ey, PathFindingCallback callback) {
    this.fromPosition = new Position(sx, sy);
    this.toPosition   = new Position(ex, ey);
    this.callback     = callback;
  }
}
