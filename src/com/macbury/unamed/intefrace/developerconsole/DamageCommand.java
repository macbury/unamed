package com.macbury.unamed.intefrace.developerconsole;

import com.macbury.unamed.combat.Damage;
import com.macbury.unamed.level.Level;

public class DamageCommand extends ConsoleCommand {

  @Override
  public boolean parseCommand(String command) {
    if (command.equals("damage player")) {
      Level.shared().getPlayer().getHealth().applyDamage(new Damage(10));
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
