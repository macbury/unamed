package com.macbury.unamed;

import org.newdawn.slick.geom.Point;

import com.macbury.unamed.entity.Entity;

public class RaycastHitResult extends Position {
  private Entity entity;
  public Position startPosition;
  public RaycastHitResult(int x, int y) {
    super(x, y);
  }

  public RaycastHitResult(float x, float y) {
    super(x, y);
  }

  public RaycastHitResult(Point p) {
    super(p.getX(), p.getY());
  }

  public RaycastHitResult(Point p, Entity e) {
    super(p.getX(), p.getY());
    setEntity(e);
  }

  public Entity getEntity() {
    return entity;
  }

  public void setEntity(Entity entity) {
    this.entity = entity;
  }

  public void setStartPosition(Position start) {
    this.startPosition = start;
  }

}
