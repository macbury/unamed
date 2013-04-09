package com.macbury.unamed.entity;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Vector2f;

import com.macbury.unamed.ImagesManager;
import com.macbury.unamed.ParticleManager;
import com.macbury.unamed.SoundManager;
import com.macbury.unamed.component.AnimatedSprite;
import com.macbury.unamed.component.Light;
import com.macbury.unamed.component.ParticleEmitter;
import com.macbury.unamed.component.Sprite;
import com.macbury.unamed.inventory.InventoryItem;
import com.macbury.unamed.inventory.TorchItem;

public class Torch extends BlockEntity {
  final static int TORCH_POWER       = 10;
  private AnimatedSprite animatedSprite;
  private Sprite offSprite;
  private ParticleEmitter emitter;

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
    
    this.emitter = new ParticleEmitter(ParticleManager.shared().getTorchParticleSystem());
    emitter.setPosition(new Vector2f(16, 16));
    emitter.enabled = true;
    addComponent(emitter);
  }

  @Override
  public boolean use() {
    Light light = this.getLight();
    light.setEnabled(!light.getEnabled());
    
    if (light.getEnabled()) {
      emitter.enabled        = true;
      offSprite.enabled      = false;
      animatedSprite.enabled = true;
    } else {
      emitter.enabled        = false;
      offSprite.enabled      = true;
      animatedSprite.enabled = false;
    }
    
    SoundManager.shared().igniteSound.playAsSoundEffect(1.0f, 1.0f, false);
    
    return true;
  }

  @Override
  public InventoryItem harvestedByPlayer(Player byPlayer) {
    return new TorchItem(byPlayer);
  }

  @Override
  public int getHardness() {
    return 3;
  }

}
