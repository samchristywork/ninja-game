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
  GraphicsContext graphics_context;
  KeyboardState keyboardState = new KeyboardState();
  Text text = new Text();
  Vec2 dimensions = new Vec2(640, 480);

  Image coinImage = new Image("file:./assets/coin.png");
  Image heartImage = new Image("file:./assets/heart.png");
  Image characterImage = new Image("file:./assets/character.png");
  Image tilesetImage = new Image("file:./assets/tileset.png");

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
    graphics_context.clearRect(0, 0, dimensions.x, dimensions.y);

    for (Tile tile : tiles) {
      tile.draw(graphics_context);
    }

    for (Item item : items) {
      item.draw(graphics_context);
      item.update();
    }

    character.draw(graphics_context, keyboardState);
    text.draw(graphics_context, "" + score, 10, 10);

    text.draw(graphics_context, "" + (int)health, dimensions.x - 50, 10);

    float angle = (float)(Math.sin(System.currentTimeMillis() / 100) * 10);
    drawRotatedImage(graphics_context, heartImage, angle, dimensions.x - 70, 17,
                     20, 20);
  }

  private void update() {
  }

  @Override
  public void start(Stage stage) {
    character = new Character(characterImage);
    character.position.x = dimensions.x / 2;
    character.position.y = dimensions.y / 2;

    Canvas canvas = new Canvas(dimensions.x, dimensions.y);
    graphics_context = canvas.getGraphicsContext2D();

    Scene scene = new Scene(new StackPane(canvas), dimensions.x, dimensions.y);

    stage.setOnCloseRequest((event) -> { System.exit(0); });

    stage.setScene(scene);
    stage.show();

    new Thread(() -> {
      int frame = 0;
      while (true) {
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

  public static void main(String[] args) { launch(); }
}
