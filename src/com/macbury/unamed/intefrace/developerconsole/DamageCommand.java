package com.macbury.unamed.intefrace.developerconsole;

import org.newdawn.slick.SlickException;

import com.macbury.unamed.combat.Damage;
import com.macbury.unamed.level.Level;

public class DamageCommand extends ConsoleCommand {

  @Override
  public boolean parseCommand(String command) {
    if (command.equals("damage player")) {
      try {
        Level.shared().getPlayer().getHealth().applyDamage(new Damage(10));
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
    // TODO Auto-generated method stub
    return "damage player";
  }

  @Override
  public String getDescription() {
    return "apply 10 hp damage to player";
  }

}
