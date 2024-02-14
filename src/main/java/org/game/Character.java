package org.game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.transform.Rotate;

class Character {
  Vec2 position = new Vec2(0, 0);
  Image spriteSheet;
  Vec2 size = new Vec2(64, 64);

  int frame = 0;
  int speed = 5;
  float tickle = 0;

  public Character(Image image) {
    spriteSheet = image;
  }

  public void draw(GraphicsContext gc, KeyboardState keyboardState) {
    boolean moving = keyboardState.up || keyboardState.down ||
        keyboardState.left || keyboardState.right;

    gc.setImageSmoothing(false);

    gc.save();
    Rotate r = new Rotate(Math.sin(tickle * 10) * 15, position.x + size.x / 2,
        position.y + size.y / 2);
    gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(),
        r.getTy());

    if (moving) {
      gc.drawImage(spriteSheet, 0, 1 + ((frame / 10) % 4) * 16, 16, 16,
          position.x, position.y, size.x, size.y);
    } else {
      frame = 0;
      gc.drawImage(spriteSheet, 0, 0, 16, 16, position.x, position.y, size.x,
          size.y);
    }

    gc.restore();

    frame++;
  }

  public void update(KeyboardState keyboardState) {
    if (keyboardState.up) {
      position.y -= speed;
    }
    if (keyboardState.down) {
      position.y += speed;
    }
    if (keyboardState.left) {
      position.x -= speed;
    }
    if (keyboardState.right) {
      position.x += speed;
    }

    if (tickle > 0) {
      tickle -= 0.1;
    }
  }
}
