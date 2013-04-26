package com.macbury.unamed.intefrace.developerconsole;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import com.macbury.unamed.entity.Entity;
import com.macbury.unamed.entity.Monster;
import com.macbury.unamed.entity.Npc;
import com.macbury.unamed.entity.Player;
import com.macbury.unamed.inventory.InventoryItem;
import com.macbury.unamed.inventory.InventoryManager;
import com.macbury.unamed.level.Level;
import com.macbury.unamed.util.MonsterManager;

public class SpawnNpcCommand extends ConsoleCommand {

  private final static String REGEXP = "spawn\\s+([a-zA-Z]+)";
  @Override
  public boolean parseCommand(String command) {
    Pattern pattern = Pattern.compile(REGEXP, Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(command);
    if (matcher.find()) {
      
      String itemName  = matcher.group(1);
      try {
        InventoryManager manager = InventoryManager.shared();
        Class<?> klass     = Entity.class.forName("com.macbury.unamed.entity."+itemName);
        Entity entity = (Entity) klass.newInstance();
        
        if (Monster.class.isInstance(entity)) {
          Monster monster = (Monster) entity;
          monster.setConfig(MonsterManager.shared().getRandomConfig());
        }
        
        Player player = Level.shared().getPlayer();
        Vector2f inFront = player.getTilePositionInFront();
        
        Level.shared().addEntity(entity);
        entity.setTilePosition((int)inFront.getX(), (int)inFront.getY());
        
        return true;
      } catch (ClassNotFoundException e) {
        this.console.print("Undefined item: " + itemName);
        e.printStackTrace();
        return true;
      } catch (InstantiationException e) {
        this.console.print("Error: " + e.toString());
        e.printStackTrace();
        return true;
      } catch (IllegalAccessException e) {
        this.console.print("Error: " + e.toString());
        e.printStackTrace();
        return true;
      } catch (SlickException e) {
        this.console.print("Error: " + e.toString());
        e.printStackTrace();
        return true;
      } catch (NoClassDefFoundError e) {
        this.console.print("Error: " + e.toString());
        e.printStackTrace();
        return true;
      }
      
    } else {
      return false;
    }
  }

  @Override
  public String getExample() {
    return "spawn <entity>";
  }

  @Override
  public String getDescription() {
    return "spawn entity in front of you";
  }

}
