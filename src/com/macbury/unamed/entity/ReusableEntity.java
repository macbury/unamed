package com.macbury.unamed.entity;

public abstract class ReusableEntity extends Entity {
  public abstract void onReuse();
  
  @Override
  public void destroy() {
    super.destroy();
    this.getLevel().pushEntityForReuse(this);
  }
}
