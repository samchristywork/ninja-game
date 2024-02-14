package org.game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Item {
  Vec2 position = new Vec2(0, 0);
  Image spriteSheet;
  int frame = 0;
  Vec2 size = new Vec2(10, 10);
  Vec2 velocity = new Vec2(0, 0);
  float drag = 0.9f;
  int value = 10;
  int health = 0;
  int numFrames = 4;
  Vec2 sourceSize = new Vec2(10, 10);

  public Item(Image image) {
    spriteSheet = image;
  }

  public void draw(GraphicsContext gc) {
    gc.setImageSmoothing(false);

    gc.drawImage(spriteSheet, ((frame / 3) % numFrames) * sourceSize.x, 0,
        sourceSize.x, sourceSize.y, position.x, position.y, size.x,
        size.y);
    frame++;
  }

  public void update() {
    position.x += velocity.x;
    position.y += velocity.y;

    velocity.x *= drag;
    velocity.y *= drag;

    if (Math.abs(velocity.x) < 0.1) {
      velocity.x = 0;
    }

    if (Math.abs(velocity.y) < 0.1) {
      velocity.y = 0;
    }
  }
}
