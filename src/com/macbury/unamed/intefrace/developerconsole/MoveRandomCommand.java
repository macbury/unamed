package com.macbury.unamed.intefrace.developerconsole;

import com.macbury.unamed.block.Sidewalk;
import com.macbury.unamed.entity.Player;
import com.macbury.unamed.level.Level;

public class MoveRandomCommand extends ConsoleCommand {

  @Override
  public boolean parseCommand(String command) {
    if (command.equals("move random")) {
      Player player     = Level.shared().getPlayer();
      Sidewalk sidewalk = Level.shared().findRandomSidewalk();
      player.setTileX(sidewalk.x);
      player.setTileY(sidewalk.y);
      player.getLight().updateLight();
      return true;
    } else {
      return false;
    }
  }

  @Override
  public String getExample() {
    return "move random";
  }

  @Override
  public String getDescription() {
    return "move to random location on map";
  }

}
