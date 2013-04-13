package com.macbury.unamed.level;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;


import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.InputChunked;
import com.esotericsoftware.kryo.io.Output;
import com.macbury.unamed.component.Light;

public abstract class Block {
  public static final int VISITED_ALPHA             = 210;
  public static final int MIN_LIGHT_POWER           = 200;
  public static int gid                             = 0;
  int id                                            = 0;
  
  public static final byte FLAG_NEED_COMPUTATION    = 0;
  public static final byte FLAG_NONE                = 1;
  public static final byte FLAG_WALL                = 2;
  
  public static final byte RESOURCE_SIDEWALK    = 0;
  public static final byte RESOURCE_DIRT        = 1;
  public static final byte RESOURCE_COPPER      = 2;
  public static final byte RESOURCE_SAND        = 3;
  public static final byte RESOURCE_WATER       = 4;
  public static final byte RESOURCE_STONE       = 5;
  public static final byte RESOURCE_LAVA        = 6;
  public static final byte RESOURCE_COAL        = 7;
  public static final byte RESOURCE_DIAMOND     = 8;
  public static final byte RESOURCE_GOLD        = 9;
  public static final byte RESOURCE_BEDROCK     = 10;
  public static final byte RESOURCE_COBBLESTONE = 11;
  public static final Color HARVESTED_SIDEWALK_COLOR = new Color(100,100,100);
  
  public  boolean harvestable         = false;
  public  boolean solid               = false;
  private boolean visited             = false;
  private boolean visible             = false;
  protected short hardness            = -1;
  private int     lightPower          = 255;
  private byte    flags               = FLAG_NEED_COMPUTATION;
  
  HashMap<Light,Integer> lightMapping;
  public int x;
  public int y;
  
  public Block() {
    this.id = Block.gid++;
  }
  
  public Block(int x, int y) {
    this.id = Block.gid++;
    this.x = x;
    this.y = y;
  }
  
  public void markAsVisible() {
    this.visited = true;
    this.visible = true;
  }

  public boolean isVisible() {
    return this.visible;
  }
  
  public boolean isVisited() {
    return this.visited;
  }
  
  public boolean isVisibleOrVisited() {
    return isVisible() || isVisited();
  }

  public void markAsInvisibleBlock() {
    this.visible = false;
  }

  public boolean haveBeenVisited() {
    return this.visited;
  }
  
  public HashMap<Light, Integer> getLightMapping() {
    return lightMapping;
  }
  
  public void copyLightsFromBlock(Block block) {
    this.lightMapping    = block.getLightMapping();
    if (lightMapping != null) {
      Set<Light> lightsSet = this.lightMapping.keySet();
      List<Light> lights   = new ArrayList<Light>(lightsSet); 
      for (int i = 0; i < lights.size(); i++) {
        lights.get(i).refresh();
      }
    }
  }
  
  public void markByLightPower() {
    if(lightMapping == null || lightMapping.size() == 0) {
      markAsInvisibleBlock();
    } else {
      markAsVisible();
    }
  }
  
  public void applyLight(Light light, int power) {
    if (lightMapping == null) {
      lightMapping = new HashMap<Light, Integer>();
    } else {
      lightMapping.remove(light);
    }
    lightMapping.put(light, power);
    calculateLightPower();
  }
  
  public void calculateLightPower() {
    if (lightMapping == null || lightMapping.size() == 0) {
      lightPower = 255;
    } else {
      Collection<Integer> values = lightMapping.values();
      lightPower = 255;
      for (Integer a : values) {
        lightPower = Math.min(a, lightPower);
      }
    }
  }
  
  public int getLightPower() {
    return lightPower;
  }

  public void popLight(Light light) {
    if (lightMapping != null) {
      lightMapping.remove(light);
      
      if (lightMapping.size() == 0) {
        lightMapping = null;
      }
    }
    calculateLightPower();
  }
  
  public int getId() {
    return this.id;
  }
  
  public void setId(int nid) {
    this.id = nid;
  }
  
  public PassableBlock getAsPassableBlock() {
    if (PassableBlock.class.isInstance(this)) {
      return (PassableBlock) this;
    } else {
      return null;
    }
  }
  
  public LiquidBlock getAsLiquidBlock() {
    if (LiquidBlock.class.isInstance(this)) {
      return (LiquidBlock) this;
    } else {
      return null;
    }
  }

  public boolean isCopper() {
    return CopperOre.class.isInstance(this);
  }
  
  public boolean isDirt() {
    return Dirt.class.isInstance(this);
  }
  
  public boolean isRock() {
    return Rock.class.isInstance(this);
  }
  
  public boolean isWater() {
    return Water.class.isInstance(this);
  }
  
  public boolean isLava() {
    return Lava.class.isInstance(this);
  }
  
  public boolean isGold() {
    return GoldOre.class.isInstance(this);
  }
  
  public boolean isDiamond() {
    return DiamondOre.class.isInstance(this);
  }
  
  public boolean isBedrock() {
    return Bedrock.class.isInstance(this);
  }
  
  public boolean isSand() {
    return Sand.class.isInstance(this);
  }
  
  public boolean isCoal() {
    return CoalOre.class.isInstance(this);
  }
  
  public boolean isAir() {
    return Sidewalk.class.isInstance(this);
  }

  public boolean isWall() {
    return flags == FLAG_WALL;
  }
  
  public void setX(int tx) {
    this.x = tx;
  }

  public void setY(int ty) {
    this.y = ty;
  }

  public boolean isCobbleStone() {
    return Cobblestone.class.isInstance(this);
  }

  public void computeWallShadow(Level level) {
    if (this.flags == FLAG_NEED_COMPUTATION) {
      Block bottomBlock = level.getBlockForPosition(this.x, this.y + 1);
      if (bottomBlock != null && (!bottomBlock.solid && this.solid)) {
        this.flags = FLAG_WALL;
      } else {
        this.flags = FLAG_NONE;
      }
    }
  }

  public void refreshFlags() {
    this.flags = FLAG_NEED_COMPUTATION;
  }

  public void setVisible(boolean readBoolean) {
    this.visible = readBoolean;
  }

  public void setVisited(boolean readBoolean) {
    this.visited = readBoolean;
  }

  public boolean isPassable() {
    return PassableBlock.class.isInstance(this);
  }
  
  public abstract byte getBlockTypeId();
  
  public static Block blockByTypeId(byte resourceType, int x, int y) throws SlickException {
    Block block = null;
    switch (resourceType) {
      case Block.RESOURCE_DIRT:
        block = new Dirt(x, y);
      break;
      case Block.RESOURCE_COPPER:
        block = new CopperOre(x, y);
      break;
  
      case Block.RESOURCE_COAL:
        block = new CoalOre(x, y);
      break;
      
      case Block.RESOURCE_GOLD:
        block = new GoldOre(x, y);
      break;
      
      case Block.RESOURCE_WATER:
        block = new Water(x, y);
      break;
      
      case Block.RESOURCE_DIAMOND:
        block = new DiamondOre(x, y);
      break;
      
      case Block.RESOURCE_LAVA:
        block = new Lava(x, y);
      break;
      
      case Block.RESOURCE_SAND:
        block = new Sand(x, y);
      break;
      
      case Block.RESOURCE_STONE:
        block = new Rock(x, y);
      break;
      
      case Block.RESOURCE_SIDEWALK:
        block = new Sidewalk(x, y);
      break;
      
      case Block.RESOURCE_BEDROCK:
        block = new Bedrock(x, y);
      break;
      
      case Block.RESOURCE_COBBLESTONE:
        block = new Cobblestone(x, y);
      break;
      
      default:
        throw new SlickException("Undefined block type: "+ resourceType);
    }
    
    return block;
  }
  
  public void saveTo(Output out) {
    out.write(this.getBlockTypeId());
    out.write(this.x);
    out.write(this.y);
    out.write(this.getId());
    out.writeBoolean(this.isVisited());
  }

  public static Block readFrom(InputChunked input) {
    Block block = null;
    try {
      byte blockType = input.readByte();
      block = Block.blockByTypeId(blockType, input.readInt(), input.readInt());
      block.setId(input.readInt());
      block.setVisited(input.readBoolean());
    } catch (KryoException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (SlickException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return block;
  }

  public boolean isSidewalk() {
    return Sidewalk.class.isInstance(this);
  }

  public Sidewalk asSidewalk() {
    return (Sidewalk)this;
  }
  
  public String toString() {
    return getClass().getSimpleName() + " at: " + x + "x" + y;
  }
}
