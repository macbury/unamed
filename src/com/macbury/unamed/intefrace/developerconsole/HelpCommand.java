package com.macbury.unamed.intefrace.developerconsole;


public class HelpCommand extends ConsoleCommand {

  @Override
  public boolean parseCommand(String command) {
    if (command.equals("help")) {
      this.console.print("Avalible commands:");
      
      for (ConsoleCommand c : this.console.commands) {
        this.console.print(c.getExample() + " - " + c.getDescription());
      }
      
      return true;
    } else {
      return false;
    }
  }

  @Override
  public String getExample() {
    return "help";
  }

  @Override
  public String getDescription() {
    return "show avalible comands";
  }


}
