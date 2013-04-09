package com.macbury.procedular;

public class Corridor {
  DungeonCell exitCell;
  DungeonCell startCell;
  
  public Corridor(DungeonCell start, DungeonCell end) {
    startCell = start;
    exitCell = end;
    startCell.setCorridor(this);
    exitCell.setCorridor(this);
  }
}
