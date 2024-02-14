package org.game;

import java.util.ArrayList;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

public class App extends Application {
  ArrayList<Item> items = new ArrayList<Item>();
  ArrayList<Tile> tiles = new ArrayList<Tile>();

  Character character;
  GraphicsContext gc;
  KeyboardState keyboardState = new KeyboardState();
  Text text = new Text();
  Vec2 dimensions = new Vec2(640, 480);

  Image coinImage = new Image("file:./assets/coin.png");
  Image heartImage = new Image("file:./assets/heart.png");
  Image characterImage = new Image("file:./assets/character.png");
  Image tilesetImage = new Image("file:./assets/tileset.png");
  Image gradientImage = new Image("file:./assets/gradient_black.png");

  int score = 0;
  float health = 99;

  public void drawRotatedImage(GraphicsContext gc, Image image, double angle,
      double x, double y, double width,
      double height) {
    gc.save();
    rotate(gc, angle, x + width / 2, y + height / 2);
    gc.drawImage(image, x, y, width, height);
    gc.restore();
  }

  public void rotate(GraphicsContext gc, double angle, double px, double py) {
    Rotate r = new Rotate(angle, px, py);
    gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(),
        r.getTy());
  }

  private void render() {

    // Draw tiles
    for (Tile tile : tiles) {
      tile.draw(gc);
    }

    // Draw shadow
    gc.setGlobalAlpha(0.2);
    gc.drawImage(gradientImage, 0, 0, 100, 100,
        character.position.x - character.size.x / 2,
        character.position.y + character.size.y - character.size.y / 4,
        character.size.x * 2, character.size.y / 2);
    gc.setGlobalAlpha(1);

    // Draw items
    for (Item item : items) {
      item.draw(gc);
      item.update();
    }

    // Draw character
    character.draw(gc, keyboardState);

    // Draw score
    text.draw(gc, "" + score, 10, 10);

    // Draw health
    text.draw(gc, "" + (int) health, dimensions.x - 50, 10);

    float angle = (float) (Math.sin(System.currentTimeMillis() / 100) * 10);
    drawRotatedImage(gc, heartImage, angle, dimensions.x - 70, 17, 20, 20);
  }

  private void update() {
    Vec2 old_position = new Vec2(0, 0);
    old_position.x = character.position.x;
    old_position.y = character.position.y;

    character.update(keyboardState);

    health -= 0.1;

    for (Item item : items) {
      if (character.position.x < item.position.x + item.size.x &&
          character.position.x + character.size.x > item.position.x &&
          character.position.y < item.position.y + item.size.y &&
          character.position.y + character.size.y > item.position.y) {

        // Dangerous as fuck
        items.remove(item);

        score += item.value;

        health += item.health;
        if (health > 99) {
          health = 99;
        }
        break;
      }
    }

    for (Tile tile : tiles) {
      if (tile.collide) {
        if (character.position.x < tile.position.x + tile.size.x &&
            character.position.x + character.size.x > tile.position.x &&
            character.position.y < tile.position.y + tile.size.y &&
            character.position.y + character.size.y > tile.position.y) {
          character.position.x = old_position.x;
          character.position.y = old_position.y;
          break;
        }
      }
    }
  }

  @Override
  public void start(Stage stage) {
    character = new Character(characterImage);
    character.position.x = dimensions.x / 2;
    character.position.y = dimensions.y / 2;

    // Add corner tile
    {
      Tile t = new Tile(tilesetImage);
      tiles.add(t);
      t.collide = true;
    }

    // Add top tiles
    for (int i = 0; i < 20; i++) {
      Tile t = new Tile(tilesetImage);
      tiles.add(t);
      t.offset.x = 1;
      t.position.x = 40 + i * 40;
      t.position.y = 0;
      t.collide = true;
    }

    // Add left tiles
    for (int i = 0; i < 20; i++) {
      Tile t = new Tile(tilesetImage);
      tiles.add(t);
      t.offset.y = 1;
      t.position.x = 0;
      t.position.y = 40 + i * 40;
      t.collide = true;
    }

    // Add floor tiles
    for (int i = 0; i < 20; i++) {
      for (int j = 0; j < 20; j++) {
        Tile t = new Tile(tilesetImage);
        tiles.add(t);
        t.offset.x = 1;
        t.offset.y = 1;
        t.position.x = 40 + i * 40;
        t.position.y = 40 + j * 40;
      }
    }

    Canvas canvas = new Canvas(dimensions.x, dimensions.y);
    gc = canvas.getGraphicsContext2D();

    Scene scene = new Scene(new StackPane(canvas), dimensions.x, dimensions.y);

    scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
      switch (key.getCode()) {
        case ESCAPE:
          System.exit(0);
          break;
        case UP:
          keyboardState.up = true;
          break;
        case DOWN:
          keyboardState.down = true;
          break;
        case LEFT:
          keyboardState.left = true;
          break;
        case RIGHT:
          keyboardState.right = true;
          break;
        default:
          System.out.println("Key Pressed: " + key.getCode());
          break;
      }
    });

    scene.addEventHandler(KeyEvent.KEY_RELEASED, (key) -> {
      switch (key.getCode()) {
        case UP:
          keyboardState.up = false;
          break;
        case DOWN:
          keyboardState.down = false;
          break;
        case LEFT:
          keyboardState.left = false;
          break;
        case RIGHT:
          keyboardState.right = false;
          break;
        default:
          System.out.println("Key Released: " + key.getCode());
          break;
      }
    });

    scene.addEventHandler(MouseEvent.MOUSE_CLICKED, (mouse) -> {
      if (mouse.getX() > character.position.x &&
          mouse.getX() < character.position.x + character.size.x &&
          mouse.getY() > character.position.y &&
          mouse.getY() < character.position.y + character.size.y) {
        character.tickle = 1;
        health += 10;
        if (health > 99) {
          health = 99;
        }
      }
    });

    stage.setOnCloseRequest((event) -> {
      System.exit(0);
    });

    stage.setScene(scene);
    stage.show();

    new Thread(() -> {
      int frame = 0;
      while (true) {
        if (frame % 30 == 0) {

          Item i = new Item(heartImage);
          items.add(i);
          i.numFrames = 1;

          int margin = 100;
          i.position.x = (int) (Math.random() * (dimensions.x - margin * 2) + margin);
          i.position.y = (int) (Math.random() * (dimensions.y - margin * 2) + margin);
          i.sourceSize.x = 9;
          i.sourceSize.y = 8;
          i.size.x = 20;
          i.size.y = 20;

          i.value = 0;
          i.health = 10;
        }

        if (frame % 30 == 0) {
          Vec2 position = new Vec2(0, 0);
          int margin = 100;
          position.x = (int) (Math.random() * (dimensions.x - margin * 2) + margin);
          position.y = (int) (Math.random() * (dimensions.y - margin * 2) + margin);

          for (int n = 0; n < 20; n++) {
            Item i = new Item(coinImage);
            items.add(i);

            i.position.x = position.x;
            i.position.y = position.y;

            float spread = 30;
            i.velocity.x = (int) (Math.random() * spread) - (int) spread / 2;
            i.velocity.y = (int) (Math.random() * spread) - (int) spread / 2;

            i.frame = (int) (Math.random() * 100);

            int random = (int) (Math.random() * 100);
            if (random < 10) {
              i.value = 100;
              i.size.x = 20;
              i.size.y = 20;
            }
          }
        }
        frame++;

        update();
        render();
        try {
          Thread.sleep(1000 / 30);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }).start();
  }

  public static void main(String[] args) {
    launch();
  }
}
