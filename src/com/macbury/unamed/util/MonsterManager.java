package com.macbury.unamed.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;

import com.macbury.unamed.Core;
import com.macbury.unamed.Timer;
import com.macbury.unamed.TimerInterface;
import com.macbury.unamed.block.Block;
import com.macbury.unamed.block.Cobblestone;
import com.macbury.unamed.entity.Entity;
import com.macbury.unamed.entity.Monster;
import com.macbury.unamed.level.Level;

public class MonsterManager implements TimerInterface {
  private static final int MAX_MONSTER_POPULATION      = 200;
  private static final int MAX_RESPAWN_MONSTER_AT_ONCE = 5;
  private static final int MIN_MONSTER_DENSITY         = 10;
  
  private static final short UPDATE_POPULATION_EVERY = 7500;
  
  private static MonsterManager shared;
  private ArrayList<Monster> population;
  private HashMap<String, JSONObject> monsterConfigs;

  private int lastIndex;
  
  private Timer updatePopulationTimer;
  
  public MonsterManager() {
    this.population = new ArrayList<Monster>(MAX_MONSTER_POPULATION);
    this.monsterConfigs = new HashMap<String, JSONObject>();
    Core.log(this.getClass(),"Initializing Monster Manager!");
    File folder = new File("res/entities/");
    
    JSONParser parser = new JSONParser();
    
    for (final File fileEntry : folder.listFiles()) {
      try {
        JSONObject object = (JSONObject)parser.parse(readFile(fileEntry));
        Core.log(this.getClass(),"Loading: " + fileEntry.getName() + " and it is: "+(String)object.get("name"));
        monsterConfigs.put((String)object.get("name"), object);
      } catch (ParseException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    
    this.lastIndex = -1;
    this.updatePopulationTimer = new Timer(UPDATE_POPULATION_EVERY, this);
    this.updatePopulationTimer.start();
    this.updatePopulationTimer.setIsPausableEvent(true);
  }
  

  public String readFile(File f) {
    try {
        byte[] bytes = Files.readAllBytes(f.toPath());
        return new String(bytes,"UTF-8");
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
    return "";
  }
  
  public void clear() {
    this.population.clear();
  }
  
  public static MonsterManager shared() {
    if (shared == null) {
      shared = new MonsterManager();
    }
    
    return shared;
  }
  
  public JSONObject getRandomConfig() {
    int index = lastIndex;
    
    while(index == lastIndex) {
      index = (int) Math.round((this.monsterConfigs.keySet().size() - 1) * Math.random());
    }
    
    lastIndex = index;
    
    for (String key : this.monsterConfigs.keySet()) {
      if (index == 0) {
        return this.monsterConfigs.get(key);
      } else {
        index--;
      }
    }
    return null;
  }

  public JSONObject get(String readString) {
    for (String key : this.monsterConfigs.keySet()) {
      if (key.equals(readString)) {
        return this.monsterConfigs.get(key);
      }
    }
    return null;
  }
  
  public void push(Monster monster){
    this.population.add(monster);
  }

  public void remove(Monster monster) {
    this.population.remove(monster);
  }
  
  public Monster getRandomMonster() throws SlickException {
    if (this.population.size() < MAX_MONSTER_POPULATION) {
      return createNewMonster();
    } else {
      return reuseRandomMonster();
    }
  }

  private Monster createNewMonster() throws SlickException {
    Monster monster = new Monster();
    monster.setConfig(getRandomConfig());
    Level.shared().addEntity(monster);
    return monster;
  }

  private Monster reuseRandomMonster() throws SlickException {
    for (Monster monster : this.population) {
      if (!monster.isOnVisibleBlock()) {
        return monster;
      }
    }
    return createNewMonster();
  }
  
  public void update(int delta) throws SlickException {
    this.updatePopulationTimer.update(delta);
  }

  @Override
  public void onTimerFire(Timer timer) throws SlickException {
    ArrayList<Entity> visibleMonsters = Level.shared().entitiesInRect(Level.shared().getUpdateArea(), Monster.class);
    int needToSpawn = MIN_MONSTER_DENSITY - visibleMonsters.size();
    Core.log(this.getClass(),"There are "+ visibleMonsters.size() + " on screen, will spawn another: "+ needToSpawn);
    while(needToSpawn >= 0) {
      spawnMonster();
      needToSpawn--;
    }
    
    timer.setTime(UPDATE_POPULATION_EVERY + Level.shared().random.nextInt(UPDATE_POPULATION_EVERY));
  }
  
  public void fillWorldWithMonsters() throws SlickException {
    for (int i = 0; i < MAX_MONSTER_POPULATION - MAX_RESPAWN_MONSTER_AT_ONCE * 2; i++) {
      Block block = Level.shared().findRandomSidewalk();
      Monster monster = createNewMonster();
      monster.setTileX(block.x);
      monster.setTileY(block.y);
      Core.log(this.getClass(),"Spawning monster on position: " + block.toString());
    }
  }

  private void spawnMonster() throws SlickException {
    Block block = Level.shared().getPassableInvisibleBlockInRect(Level.shared().getUpdateArea());
    
    if (block != null) {
      Core.log(this.getClass(),"Spawning monster on pos: " + block.toString());
      Monster monster = getRandomMonster();
      monster.setTileX(block.x);
      monster.setTileY(block.y);
    } else {
      Core.log(this.getClass(),"No free space found!");
    }
  }
}
