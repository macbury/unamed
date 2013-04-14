package com.macbury.unamed.intefrace.developerconsole;

import java.util.regex.Pattern;


public abstract class ConsoleCommand {
  protected DeveloperConsole console;

  public abstract boolean parseCommand(String command);
  public abstract String getExample();
  public abstract String getDescription();
  public DeveloperConsole getConsole() {
    return console;
  }

  public void setConsole(DeveloperConsole console) {
    this.console = console;
  }
}
