package com.macbury.unamed.entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.state.StateBasedGame;

import com.macbury.unamed.ImagesManager;
import com.macbury.unamed.component.AnimatedSprite;
import com.macbury.unamed.component.Light;
import com.macbury.unamed.component.Sprite;
import com.macbury.unamed.inventory.InventoryItem;
import com.macbury.unamed.inventory.TorchItem;

public class Torch extends BlockEntity {
  final static int TORCH_POWER       = 10;
  private AnimatedSprite animatedSprite;
  private Sprite offSprite;

  public Torch() throws SlickException {
    super();
    
    this.visibleUnderTheFog = true;
    
    Light light = new Light();
    light.setLightPower(TORCH_POWER);
    light.updateLight();
    addComponent(light);

    SpriteSheet spriteSheet = ImagesManager.shared().getSpriteSheet("torch.png",  AnimatedSprite.SPRITE_TILE_WIDTH, AnimatedSprite.SPRITE_TILE_HEIGHT);
    Image[] fireSequence    = new Image[] { spriteSheet.getSprite(0, 0), spriteSheet.getSprite(1, 0), spriteSheet.getSprite(2, 0) };
    
    this.animatedSprite = new AnimatedSprite(fireSequence);
    addComponent(animatedSprite);
    
    this.offSprite      = new Sprite(spriteSheet.getSprite(3, 0));
    offSprite.enabled   = false;
    addComponent(offSprite);
    
    this.hardness = 3;
  }

  @Override
  public boolean use() {
    Light light = this.getLight();
    light.setEnabled(!light.getEnabled());
    
    if (light.getEnabled()) {
      offSprite.enabled      = false;
      animatedSprite.enabled = true;
    } else {
      offSprite.enabled      = true;
      animatedSprite.enabled = false;
    }
    
    return true;
  }

  @Override
  public InventoryItem harvest(int power, Player byPlayer) {
    hardness -= power;
    if (hardness <= 0) {
      return new TorchItem(byPlayer);
    } else {
      return null;
    }
  }

}
