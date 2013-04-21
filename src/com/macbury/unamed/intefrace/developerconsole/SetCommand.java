package com.macbury.unamed.intefrace.developerconsole;

public class SetCommand extends ConsoleCommand {

  @Override
  public boolean parseCommand(String command) {

    return false;
  }

  @Override
  public String getExample() {
    return "set <key> '<value>'";
  }

  @Override
  public String getDescription() {
    return "set value for key in config";
  }

}
