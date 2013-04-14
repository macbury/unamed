package com.macbury.unamed.intefrace.developerconsole;

public class ClearCommand extends ConsoleCommand {

  @Override
  public boolean parseCommand(String command) {
    if (command.equals("clear")) {
      this.console.commandsLog.clear();
      return true;
    } else {
      return false;
    }
  }

  @Override
  public String getExample() {
    return "clear";
  }

  @Override
  public String getDescription() {
    return "clear console output";
  }

}
