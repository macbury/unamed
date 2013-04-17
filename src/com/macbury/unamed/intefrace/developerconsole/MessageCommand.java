package com.macbury.unamed.intefrace.developerconsole;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.newdawn.slick.SlickException;

import com.macbury.unamed.intefrace.InterfaceManager;
import com.macbury.unamed.npc.MessagesQueue;

public class MessageCommand extends ConsoleCommand {
  private final static String REGEXP = "talk.+\\'(.+)\\'";
  @Override
  public boolean parseCommand(String command) {
    Pattern pattern = Pattern.compile(REGEXP, Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(command);
    if (matcher.find()) {
      MessagesQueue dialog = new MessagesQueue();
      dialog.add(matcher.group(1));
      
      try {
        InterfaceManager.shared().pop();
        InterfaceManager.shared().showDialogue(dialog, null);
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
    return "talk '<message>'";
  }

  @Override
  public String getDescription() {
    return "show npc message on screen";
  }

}
