package com.macbury.unamed.intefrace;

public class MenuItem {
  String name;
  int    id;
  
  public MenuItem(String name, int id) {
    this.name = name;
    this.id   = id;
  }
  
  public int getId() {
    return id;
  }
  
  public String getName() {
    return name;
  }
}
