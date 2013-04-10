package com.macbury.procedular;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.util.Log;

public class DungeonBSPNode extends Rectangle {
  public final static int MAX_SPLIT_DEPTH   = 5; 
  public final static byte SPLIT_VERITICAL  = 0;
  public final static byte SPLIT_HORIZONTAL = 1;
  final static float MIN_SPLIT_FACTOR = 0.48f;
  final static float MAX_SPLIT_FACTOR = 0.52f;
  public DungeonBSPNode leftChild;
  public DungeonBSPNode rightChild;
  private Random random;
  private int depth = 0;
  private Room room;
  private DungeonBSPNode parent;
  private Corridor corridor;
  private byte splitDirection;
  
  public DungeonBSPNode(DungeonBSPNode parent, float x, float y, float width, float height, int depth, Random random) {
    super(x, y, width, height);
    this.setParent(parent);
    this.random = random;
    this.depth  = depth + 1;
    Log.debug("DungeonBSPNode: X: "+x + " Y: "+ y + " width: "+ width + " height: " +height + " depth: "+depth);
    if (!this.split()) {
      this.createRoom();
    }
  }
  
  public void connectTo(DungeonBSPNode dungeonBSPNode) {
    new Corridor(this, dungeonBSPNode);
  }
  
  public boolean isRoot() {
    return parent == null;
  }
  
  private void createRoom() {
    int baseWidth   = (int) (this.getWidth() / 2);
    int baseHeight  = (int) (this.getHeight() / 2);
    int roomWidth   = baseWidth + random.nextInt(baseWidth);
    int roomHeight  = baseHeight + random.nextInt(baseHeight);

    if (roomWidth <= 4 || roomHeight <= 4) {
      this.parent.randomSplit();
    } else {
      int ex          = random.nextInt((int) (this.getWidth()  - roomWidth));
      int ey          = random.nextInt((int) (this.getHeight() - roomHeight));
      
      this.room       = new Room(this.getX() + ex, this.getY() + ey, roomWidth, roomHeight);
    }
  }

  private int getSizeA(float size) {
    float factor = ((MAX_SPLIT_FACTOR - MIN_SPLIT_FACTOR) * random.nextFloat()) + MIN_SPLIT_FACTOR;
    return (int) (size * factor);
  }
  
  private void splitHorizontaly() {
    Log.info("Spliting horizontal on depth: "+this.depth);
    int sizeA      = getSizeA(this.getWidth());
    int sizeB      = (int)(this.getWidth() - sizeA);
    splitDirection = SPLIT_HORIZONTAL;
    leftChild          = new DungeonBSPNode(this, this.getX(), this.getY(), sizeA, this.getHeight(), this.depth, this.random);
    rightChild         = new DungeonBSPNode(this, this.getX()+sizeA, this.getY(), sizeB, this.getHeight(), this.depth, this.random);
  }

  private void splitVertical() {
    Log.info("Spliting vertical on depth: "+this.depth);
    int sizeA      = getSizeA(this.getHeight());
    int sizeB      = (int)(this.getHeight() - sizeA);
    splitDirection = SPLIT_VERITICAL;
    leftChild          = new DungeonBSPNode(this, this.getX(), this.getY(), this.getWidth(), sizeA, this.depth, this.random);
    rightChild         = new DungeonBSPNode(this, this.getX(), this.getY()+sizeA, this.getWidth(), sizeB, this.depth, this.random);
  }
  
  public boolean split() {
    if (this.depth >= MAX_SPLIT_DEPTH) {
      return false;
    } else {
      randomSplit();
      return true;
    }
  }
  
  private void randomSplit() {
    if (this.random.nextBoolean()) {
      splitHorizontaly();
    } else {
      splitVertical();
    }
  }
  
  public Random getRandom() {
    return random;
  }
  
  public Room getRoom() {
    return this.room;
  }
  
  public boolean haveRoom() {
    return this.room != null;
  }
  
  public int getDepth() {
    return depth;
  }

  public void setDepth(int depth) {
    this.depth = depth;
  }

  public void setRandom(Random r) {
    this.random = r;
  }

  public ArrayList<Room> getAllRooms() {
    ArrayList<Room> rooms = new ArrayList<Room>();
    return getRoomsFromLowerNode(rooms);
  }
  
  public boolean isBottomNode() {
    return (leftChild != null) && (rightChild != null);
  }
  
  private ArrayList<Room> getRoomsFromLowerNode(ArrayList<Room> rooms) {
    if (this.haveRoom()) {
      rooms.add(this.getRoom());
      return rooms;
    } else {
      rooms = this.leftChild.getRoomsFromLowerNode(rooms);
      rooms = this.rightChild.getRoomsFromLowerNode(rooms);
      return rooms;
    }
  }

  public DungeonBSPNode getParent() {
    return parent;
  }

  public void setParent(DungeonBSPNode parent) {
    this.parent = parent;
  }

  public Corridor getCorridor() {
    return corridor;
  }

  public void setCorridor(Corridor corridor) {
    this.corridor = corridor;
  }
  
  public void gatherLeaves(ArrayList<DungeonBSPNode> leaves) {
    if (leftChild == null) {
      return;
    }
    leaves.add(leftChild);
    leaves.add(rightChild);
    leftChild.gatherLeaves(leaves);
    rightChild.gatherLeaves(leaves);
  }
  
  public void getLeavesWithDepth(int depth, ArrayList<DungeonBSPNode> leaves) {
    if (this.depth == depth) {
      leaves.add(this);
    }
    if (leftChild != null) {
      leftChild.getLeavesWithDepth(depth, leaves);
      rightChild.getLeavesWithDepth(depth, leaves);
    }
  }
  
  public void bottomsUpByLevelEnumerate(DungeonBSPNodeCorridorGenerateCallback corridorCallback, DungeonBSPNodeRoomGenerateCallback roomCallback) {
    Stack<DungeonBSPNode> stack1 = new Stack<DungeonBSPNode>();
    Stack<DungeonBSPNode> stack2 = new Stack<DungeonBSPNode>();
    
    stack1.push(this);
    
    while (stack1.size() > 0) {
      DungeonBSPNode currentNode = stack1.pop();
      stack2.push(currentNode);
      
      if (currentNode.leftChild != null) {
        stack1.push(currentNode.leftChild);
      }
      
      if (currentNode.rightChild != null) {
        stack1.push(currentNode.rightChild);
      }
    }
    
    while(stack2.size() > 0) {
      DungeonBSPNode currentNode = stack2.pop();
      
      if (currentNode.leftChild == null && currentNode.rightChild == null) {
        if (!currentNode.haveRoom() && roomCallback != null) {
          roomCallback.onGenerateRoom(currentNode);
        }
      } else {
        if (corridorCallback != null && (currentNode.leftChild.haveRoom() || currentNode.rightChild.haveRoom())) {
          corridorCallback.onGenerateCorridor(currentNode);
        }
      }
    }
  }

}
