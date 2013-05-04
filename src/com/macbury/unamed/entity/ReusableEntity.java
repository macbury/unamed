package com.macbury.unamed.entity;

public abstract class ReusableEntity extends Entity { // USE ONLY FOR PARTICLES AND EFFECTS THAT DOSENT NEED INTERACTION!!!! look Level.getEntityForTilePosition
  public abstract void onReuse();
  
  @Override
  public void destroy() {
    super.destroy();
    this.getLevel().pushEntityForReuse(this);
  }
}
