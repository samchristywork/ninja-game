package org.game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Tile {
  Vec2 position = new Vec2(0, 0);
  Image spriteSheet;
  int frame = 0;
  Vec2 size = new Vec2(40, 40);
  Vec2 offset = new Vec2(0, 0);
  boolean collide = false;

  public Tile(Image image) {
    spriteSheet = image;
  }

  public void draw(GraphicsContext graphics_context) {
    graphics_context.setImageSmoothing(false);

    graphics_context.drawImage(spriteSheet, 0 + offset.x * 20,
        480 + offset.y * 20, 20, 20, position.x,
        position.y, size.x, size.y);
    frame++;
  }
}
