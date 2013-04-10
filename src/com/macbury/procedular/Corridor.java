package com.macbury.procedular;

public class Corridor {
  DungeonBSPNode exitCell;
  DungeonBSPNode startCell;
  
  public Corridor(DungeonBSPNode start, DungeonBSPNode end) {
    startCell = start;
    exitCell = end;
    startCell.setCorridor(this);
    exitCell.setCorridor(this);
  }
}
