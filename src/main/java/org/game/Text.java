package org.game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Text {
  Image spriteSheet = new Image("file:./assets/font_black_30x30.png");
  Image whiteSpriteSheet = new Image("file:./assets/font_white_30x30.png");

  public void draw(GraphicsContext graphics_context, String text, int x,
      int y) {
    graphics_context.setImageSmoothing(false);

    for (int i = 0; i < text.length(); i++) {
      int ascii = (int) text.charAt(i) - '0';
      ascii += 16;
      int ascii_x = ascii % 15;
      int ascii_y = ascii / 15;

      graphics_context.drawImage(spriteSheet, ascii_x * 30, ascii_y * 30, 30,
          30, x + i * 20 - 2, y + 2, 30, 30);
      graphics_context.drawImage(spriteSheet, ascii_x * 30, ascii_y * 30, 30,
          30, x + i * 20 + 2, y - 2, 30, 30);
      graphics_context.drawImage(spriteSheet, ascii_x * 30, ascii_y * 30, 30,
          30, x + i * 20 + 2, y + 2, 30, 30);
      graphics_context.drawImage(spriteSheet, ascii_x * 30, ascii_y * 30, 30,
          30, x + i * 20 - 2, y - 2, 30, 30);
      graphics_context.drawImage(whiteSpriteSheet, ascii_x * 30, ascii_y * 30,
          30, 30, x + i * 20, y, 30, 30);
    }
  }
}
