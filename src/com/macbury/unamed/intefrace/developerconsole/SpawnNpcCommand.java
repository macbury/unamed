package com.macbury.unamed.intefrace.developerconsole;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import com.macbury.unamed.entity.Npc;
import com.macbury.unamed.entity.Player;
import com.macbury.unamed.level.Level;

public class SpawnNpcCommand extends ConsoleCommand {

  @Override
  public boolean parseCommand(String command) {
    if (command.equals("spawn npc")) {
      Player player = Level.shared().getPlayer();
      Vector2f inFront = player.getTilePositionInFront();
      Npc npc = null;
      try {
        npc = new Npc();
        Level.shared().addEntity(npc);
        npc.setTilePosition((int)inFront.getX(), (int)inFront.getY());
        
      } catch (SlickException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      
      return true;
    } else {
      return false;
    }
    
  }

  @Override
  public String getExample() {
    return "spawn npc";
  }

  @Override
  public String getDescription() {
    return "spawn random npc in front of you";
  }

}
