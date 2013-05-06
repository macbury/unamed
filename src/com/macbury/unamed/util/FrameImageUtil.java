package com.macbury.unamed.util;


import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class FrameImageUtil {

  public static void render(Image windowSkinImage, Graphics gr, float width, float height, float x, float y) {
    gr.pushTransform();
    gr.translate(x, y);
    windowSkinImage.getSubImage(0, 0, 16, 16).draw();//top left corner
    windowSkinImage.getSubImage(8, 0, 8, 16).draw(16, 0, width - 32, 16); // top border
    windowSkinImage.getSubImage(8, 8, 8, 8).draw(16,16, width - 32, height - 32); // background
    windowSkinImage.getSubImage(0, 8, 16, 8).draw(0, 16, 16, height - 32); // left border
    windowSkinImage.getSubImage(0, 16, 16, 16).draw(0, height - 16, 16,16); // down left corner
    windowSkinImage.getSubImage(8, 16, 16, 16).draw(16, height - 16, width - 32, 16); // bottom border
    windowSkinImage.getSubImage(16, 16, 16, 16).draw(width - 16, height - 16, 16, 16); // bottom right corner
    windowSkinImage.getSubImage(16, 0, 16, 16).draw(width - 16, 0, 16,16); //top right corner
    windowSkinImage.getSubImage(16, 8, 16, 16).draw(width - 16, 16, 16,height - 32); // right border
    gr.popTransform();
  }

  public static void render(Image windowSkinImage, Graphics gr, float width, float height, float x, float y, Color color) {
    gr.pushTransform();
    gr.translate(x, y);
    windowSkinImage.getSubImage(0, 0, 16, 16).draw(0,0, color);//top left corner
    windowSkinImage.getSubImage(8, 0, 8, 16).draw(16, 0, width - 32, 16, color); // top border
    windowSkinImage.getSubImage(8, 8, 8, 8).draw(16,16, width - 32, height - 32, color); // background
    windowSkinImage.getSubImage(0, 8, 16, 8).draw(0, 16, 16, height - 32, color); // left border
    windowSkinImage.getSubImage(0, 16, 16, 16).draw(0, height - 16, 16,16, color); // down left corner
    windowSkinImage.getSubImage(8, 16, 16, 16).draw(16, height - 16, width - 32, 16, color); // bottom border
    windowSkinImage.getSubImage(16, 16, 16, 16).draw(width - 16, height - 16, 16, 16, color); // bottom right corner
    windowSkinImage.getSubImage(16, 0, 16, 16).draw(width - 16, 0, 16,16, color); //top right corner
    windowSkinImage.getSubImage(16, 8, 16, 16).draw(width - 16, 16, 16,height - 32, color); // right border
    gr.popTransform();
  }
  
}
