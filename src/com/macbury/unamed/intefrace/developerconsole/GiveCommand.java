package com.macbury.unamed.intefrace.developerconsole;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.newdawn.slick.SlickException;


import com.macbury.unamed.inventory.InventoryItem;
import com.macbury.unamed.inventory.InventoryManager;
import com.macbury.unamed.level.Level;

public class GiveCommand extends ConsoleCommand {
  private final static String REGEXP = "give\\s+([a-zA-Z]+)\\s+(\\d{1,2})";
  @Override
  public boolean parseCommand(String command) {
    Pattern pattern = Pattern.compile(REGEXP, Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(command);
    if (matcher.find()) {
      
      String itemName  = matcher.group(1);
      int itemQuantity = Integer.parseInt(matcher.group(2));
      try {
        InventoryManager manager = InventoryManager.shared();
        Class<?> klass     = InventoryItem.class.forName("com.macbury.unamed.inventory."+itemName+"Item");
        InventoryItem item = (InventoryItem) klass.newInstance();
        item.setItemCount(itemQuantity);
        manager.addItem(item);
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
    return "give <item name> <quantity>";
  }

  @Override
  public String getDescription() {
    return "Adds item to player inventory";
  }

}
