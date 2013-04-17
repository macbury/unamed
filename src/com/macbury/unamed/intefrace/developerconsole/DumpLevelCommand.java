package com.macbury.unamed.intefrace.developerconsole;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import org.newdawn.slick.SlickException;

import com.macbury.unamed.level.Level;
import com.macbury.unamed.level.LevelLoader;

public class DumpLevelCommand extends ConsoleCommand {

  @Override
  public boolean parseCommand(String command) {
    if (command.equals("dump")) {
      LevelLoader ll = new LevelLoader(Level.shared());
      try {
        ll.dumpTo("dump.png");
        Desktop desktop = Desktop.getDesktop();
        desktop.open(new File("dump.png"));
        this.console.print("Saved as dump.png!");
      } catch (SlickException e) {
        this.console.print(e.toString());
        e.printStackTrace();
      } catch (IOException e) {
        this.console.print(e.toString());
        e.printStackTrace();
      }
      return true;
    } else {
      return false;
    }
  }

  @Override
  public String getExample() {
    return "dump";
  }

  @Override
  public String getDescription() {
    return "dump level layout to image";
  }

}
