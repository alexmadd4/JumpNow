package com.example.jumpnow;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PandaRun extends Application {

    private Scene gameScene;
    private boolean hasSpeedBoost = false;
    private ImageView speedBoostView;

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;

    private static final int boostX = 2450;
    private static final int boostY = 570;




    private Image[] frameLeft;
    private Image[] frameRight;
    private Image[] frameJump;
    private Image[] frameStandLeft;
    private Image[] frameStandRight;

    private ImageView charView;
    private double charX;
    private double charY;
    private double charVelX;
    private double charVelY;

    private boolean moveLeft;
    private boolean moveRight;
    private boolean isJumping;
    private double prevCharX;
    private double prevCharY;

    private Rectangle platforma;

    private Rectangle lborderwall;
    private  Rectangle tree;
    private Rectangle platforma2;
    private Rectangle platforma3;
    private Rectangle platforma4;
    private Rectangle platforma5;
    private Rectangle rborderwall;
    private Rectangle tree2;
    private Rectangle tree3;
    private boolean reachedPlatform = false;
    private Rectangle mob1;
    private boolean touchedMob1 = false;
    private boolean touchedMob2 = false;
    private Rectangle mob2;

    private ImageView coinView;
    private ImageView coinView2;
    private ImageView coinView3;
    private ImageView coinView4;
    private ImageView coinView5;
    private ImageView coinView6;

    private int score = 0;

    private Rectangle platformaflag;

    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {



        Button playButton = new Button();
        Image pbimage = new Image("play_blue.png");
        ImageView pb = new ImageView(pbimage);
        pb.setFitWidth(250);
        pb.setFitHeight(250);
        playButton.setGraphic(pb);
        playButton.setPrefWidth(100);
        playButton.setPrefHeight(100);
        playButton.setStyle("-fx-background-color: transparent;");


        playButton.setOnAction(event -> primaryStage.setScene(gameScene));


        Image img = new Image("main.png");
        ImageView imageView = new ImageView(img);
        imageView.setOpacity(0.9);

        Pane pane = new Pane();

        pane.getChildren().add(imageView);

        VBox vbox = new VBox();
        vbox.getChildren().add(playButton);
        vbox.setAlignment(Pos.BOTTOM_CENTER);

        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(pane);
        stackPane.getChildren().add(vbox);

        Scene scene = new Scene(stackPane, WIDTH, HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setWidth(WIDTH);
        primaryStage.setHeight(HEIGHT);
        primaryStage.setTitle("PandaRun");
        primaryStage.show();

        gameScene = createGameScene();

        //ktu behen load frames
        frameLeft = new Image[4];
        frameRight = new Image[4];
        frameJump = new Image[2];
        frameStandLeft = new Image[1];
        frameStandRight = new Image[1];

        for (int i = 0; i < frameLeft.length; i++) {
            frameLeft[i] = new Image("leftrun.gif");
            frameRight[i] = new Image("run.gif");
        }
        for (int i = 0; i < frameJump.length; i++) {
            frameJump[i] = new Image("run.gif");
        }

        frameStandLeft[0] = new Image("leftstill.gif");
        frameStandRight[0] = new Image("sstill.gif");

        //esht pamja qe fokusohet vetem te Panda
        charView = new ImageView(frameRight[0]);
        charView.setFitWidth(52);
        charView.setFitHeight(52);
        charVelX = 0;
        charVelY = 0;

        //panda behet spawn ne platformen e pare
        charX = platforma.getX();
        charY = platforma.getY() - charView.getFitHeight();

        // butonat qe kontrollojne panden
        gameScene.setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();
            if (keyCode == KeyCode.LEFT) {
                moveLeft = true;
                charView.setImage(frameLeft[0]);
            } else if (keyCode == KeyCode.RIGHT) {
                moveRight = true;
                charView.setImage(frameRight[0]);
            } else if (keyCode == KeyCode.SPACE) {
                if (!isJumping) {
                    isJumping = true;
                    charVelY = -18; //shpejtsia e hedhjes pandes  (-15 normal)
                    charView.setImage(frameJump[0]);
                }
            }
        });

        Pane gamePane = (Pane) gameScene.getRoot();
        gamePane.getChildren().addAll(charView, coinView, coinView2, coinView3, coinView4, coinView5, coinView6);

        gameScene.setOnKeyReleased(event -> {
            KeyCode keyCode = event.getCode();
            if (keyCode == KeyCode.LEFT) {
                moveLeft = false;
                if (!moveRight) {
                    charView.setImage(frameStandLeft[0]); //e vendos panden pa leviz duke pare majtas
                }
            } else if (keyCode == KeyCode.RIGHT) {
                moveRight = false;
                if (!moveLeft) {
                    charView.setImage(frameStandRight[0]); //e vendos panden pa leviz duke pare djathtas
                }
            }
        });

        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                charX += charVelX;
                charY += charVelY;

                // graviteti lojes
                charVelY += 1;

                //event handler per levizjen e pozicionit pandes
                if (moveLeft && !moveRight) {
                    charVelX = -5; //shpejtsia pandes  (normal eshte 3)
                    charView.setImage(frameLeft[0]);
                } else if (!moveLeft && moveRight) {
                    charVelX = 5; // shpejtesia pandes ana tjeter (normal eshte 3)
                    charView.setImage(frameRight[0]);
                } else {
                    charVelX = 0;
                    if (!isJumping) {
                        if (charView.getImage() == frameLeft[0]) {
                            charView.setImage(frameStandLeft[0]); //panda rri pa leviz duke pa majtas
                        } else if (charView.getImage() == frameRight[0]) {
                            charView.setImage(frameStandRight[0]); //panda nuk leviz sheh djathtas
                        }
                    }
                }
                //muri majtas/djathas collision
                if (charX <= lborderwall.getX() + lborderwall.getWidth()) {
                    charX = lborderwall.getX() + lborderwall.getWidth();
                } else if (charX + charView.getFitWidth() >= rborderwall.getX()) {
                    charX = rborderwall.getX() - charView.getFitWidth();
                }

                //collision i platformave
                boolean collided = false;

                if (charY + charView.getFitHeight() >= platforma.getY() && charY <= platforma.getY() + platforma.getHeight()) {
                    if (charX + charView.getFitWidth() >= platforma.getX() && charX <= platforma.getX() + platforma.getWidth()) {
                        collided = true;
                    }
                }
                if (charY + charView.getFitHeight() >= platforma2.getY() && charY <= platforma2.getY() + platforma2.getHeight()) {
                    if (charX + charView.getFitWidth() >= platforma2.getX() && charX <= platforma2.getX() + platforma2.getWidth()) {
                        collided = true;
                    }
                }

                if (charY + charView.getFitHeight() >= platforma3.getY() && charY <= platforma3.getY() + platforma3.getHeight()) {
                    if (charX + charView.getFitWidth() >= platforma3.getX() && charX <= platforma3.getX() + platforma3.getWidth()) {
                        collided = true;
                    }
                }
                if (charY + charView.getFitHeight() >= platforma4.getY() && charY <= platforma4.getY() + platforma4.getHeight()) {
                    if (charX + charView.getFitWidth() >= platforma4.getX() && charX <= platforma4.getX() + platforma4.getWidth()) {
                        collided = true;
                    }
                }

                if (charY + charView.getFitHeight() >= platforma5.getY() && charY <= platforma5.getY() + platforma5.getHeight()) {
                    if (charX + charView.getFitWidth() >= platforma5.getX() && charX <= platforma5.getX() + platforma5.getWidth()) {
                        collided = true;

                    }
                }

                if (charY + charView.getFitHeight() >= platformaflag.getY() && charY <= platformaflag.getY() + platformaflag.getHeight()) {
                    if (charX + charView.getFitWidth() >= platformaflag.getX() && charX <= platformaflag.getX() + platformaflag.getWidth()) {
                        collided = true;
                        reachedPlatform = true;
                    }
                }

                if (reachedPlatform) {
                    Text message = new Text("Congratulations,You made it!");
                    Text hs1 = new Text("Score: "+ score);
                    hs1.setFont(Font.font("Arial", FontWeight.BOLD, 24));
                    hs1.setFill(Color.WHITE);
                    hs1.setX(590);
                    hs1.setY(390);

                    message.setFont(Font.font("Arial", FontWeight.BOLD, 24));
                    message.setFill(Color.WHITE);
                    message.setX(WIDTH / 2 - message.getLayoutBounds().getWidth() / 2);
                    message.setY(HEIGHT / 2 - message.getLayoutBounds().getHeight() / 2);

                    Pane messagePane = new Pane();
                    Button playagain = new Button("Play Again!");
                    messagePane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);");
                    messagePane.getChildren().add(message);
                    messagePane.getChildren().add(playagain);
                    messagePane.getChildren().add(hs1);
                    playagain.setLayoutX(610);
                    playagain.setLayoutY(440);
                    playagain.setScaleX(2);
                    playagain.setScaleY(2);

                    EventHandler<ActionEvent> eventHandler = event -> {
                        resetGameState();
                        primaryStage.setScene(gameScene);
                    };

                    playagain.setOnAction(eventHandler);

                    Scene messageScene = new Scene(messagePane, WIDTH, HEIGHT);

                    primaryStage.setScene(messageScene);

                    resetGameState();
                }
                if (charY + charView.getFitHeight() >= mob1.getY() && charY <= mob1.getY() + mob1.getHeight()) {
                    if (charX + charView.getFitWidth() >= mob1.getX() && charX <= mob1.getX() + mob1.getWidth()) {
                        collided = true;
                        touchedMob1 = true;
                    }
                }
                if (touchedMob1) {
                    Text message2 = new Text(" GameOver :(");
                    Text hs2 = new Text("Score: "+ score);
                    hs2.setFont(Font.font("Arial", FontWeight.BOLD, 24));
                    hs2.setFill(Color.WHITE);
                    hs2.setX(590);
                    hs2.setY(390);

                    message2.setFont(Font.font("Arial", FontWeight.BOLD, 30));
                    message2.setFill(Color.WHITE);
                    message2.setX(WIDTH / 2 - message2.getLayoutBounds().getWidth() / 2);
                    message2.setY(HEIGHT / 2 - message2.getLayoutBounds().getHeight() / 2);

                    Pane messagePane2 = new Pane();
                    Button playagain2 = new Button("Play Again!");
                    playagain2.setLayoutX(610);
                    playagain2.setLayoutY(440);
                    playagain2.setScaleX(2);
                    playagain2.setScaleY(2);

                    messagePane2.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);");
                    messagePane2.getChildren().add(message2);
                    messagePane2.getChildren().add(playagain2);
                    messagePane2.getChildren().add(hs2);
                    EventHandler<ActionEvent> eventHandler2 = event -> {
                        resetGameState();
                        primaryStage.setScene(gameScene);

                    };

                    playagain2.setOnAction(eventHandler2);

                //skena me mesazhe
                Scene messageScene2 = new Scene(messagePane2,1280,720);
                primaryStage.setScene(messageScene2);
                resetGameState();

            }
                if (charY + charView.getFitHeight() >= mob2.getY() && charY <= mob2.getY() + mob2.getHeight()) {
                    if (charX + charView.getFitWidth() >= mob2.getX() && charX <= mob2.getX() + mob2.getWidth()) {
                        collided = true;
                        touchedMob2 = true;
                    }
                }
                if (touchedMob2) {
                    Text message3 = new Text("You died :( " + "Your Score is : " + score);
                    message3.setFont(Font.font("Arial", FontWeight.BOLD, 30));
                    message3.setFill(Color.WHITE);
                    message3.setX(WIDTH / 2 - message3.getLayoutBounds().getWidth() / 2);
                    message3.setY(HEIGHT / 2 - message3.getLayoutBounds().getHeight() / 2);

                    Pane messagePane3 = new Pane();
                    Button playagain3 = new Button("Play Again!");
                    playagain3.setLayoutX(600);
                    playagain3.setLayoutY(400);
                    playagain3.setScaleX(2);
                    playagain3.setScaleY(2);

                    messagePane3.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);");
                    messagePane3.getChildren().add(message3);
                    messagePane3.getChildren().add(playagain3);

                    EventHandler<ActionEvent> eventHandler2 = event -> {
                        resetGameState();
                        primaryStage.setScene(gameScene);

                    };

                    playagain3.setOnAction(eventHandler2);

                    Scene messageScene3 = new Scene(messagePane3, WIDTH, HEIGHT);

                    primaryStage.setScene(messageScene3);

                    resetGameState();

                }

                //check per perplasjen me pickup item
                if (charY + charView.getFitHeight() >= coinView.getY() && charY <= coinView.getY() + coinView.getFitHeight()) {
                    if (charX + charView.getFitWidth() >= coinView.getX() && charX <= coinView.getX() + coinView.getFitWidth()) {
                        coinView.setVisible(false);
                        score += 5;
                    }
                }
                coinView.setX(500);
                coinView.setY(560);

                if (charY + charView.getFitHeight() >= coinView2.getY() && charY <= coinView2.getY() + coinView2.getFitHeight()) {
                    if (charX + charView.getFitWidth() >= coinView2.getX() && charX <= coinView2.getX() + coinView2.getFitWidth()) {
                        coinView2.setVisible(false);
                        score += 5;
                    }
                }
                coinView2.setX(1050);
                coinView2.setY(560);

                if (charY + charView.getFitHeight() >= coinView3.getY() && charY <= coinView3.getY() + coinView3.getFitHeight()) {
                    if (charX + charView.getFitWidth() >= coinView3.getX() && charX <= coinView3.getX() + coinView3.getFitWidth()) {
                        coinView3.setVisible(false);
                        score += 5;
                    }
                }
                coinView3.setX(1500);
                coinView3.setY(560);

                if (charY + charView.getFitHeight() >= coinView4.getY() && charY <= coinView4.getY() + coinView4.getFitHeight()) {
                    if (charX + charView.getFitWidth() >= coinView4.getX() && charX <= coinView4.getX() + coinView4.getFitWidth()) {
                        coinView4.setVisible(false);
                        score += 5;
                    }
                }
                coinView4.setX(2000);
                coinView4.setY(560);

                if (charY + charView.getFitHeight() >= coinView5.getY() && charY <= coinView5.getY() + coinView5.getFitHeight()) {
                    if (charX + charView.getFitWidth() >= coinView5.getX() && charX <= coinView5.getX() + coinView5.getFitWidth()) {
                        coinView5.setVisible(false);
                        score += 5;
                    }
                }
                coinView5.setX(2500);
                coinView5.setY(560);

                if (charY + charView.getFitHeight() >= coinView6.getY() && charY <= coinView6.getY() + coinView6.getFitHeight()) {
                    if (charX + charView.getFitWidth() >= coinView6.getX() && charX <= coinView6.getX() + coinView6.getFitWidth()) {
                        coinView6.setVisible(false);
                        score += 5;
                    }
                }
                coinView6.setX(3100);
                coinView6.setY(560);

                //peplasja midis karakterit dhe speed boostit
                if (charY + charView.getFitHeight() >= speedBoostView.getY() &&
                        charY <= speedBoostView.getY() + speedBoostView.getFitHeight()) {
                    if (charX + charView.getFitWidth() >= speedBoostView.getX() &&
                            charX <= speedBoostView.getX() + speedBoostView.getFitWidth()) {
                        hasSpeedBoost = true;
                        gamePane.getChildren().remove(speedBoostView);
                    }
                }

                if(hasSpeedBoost){
                    if (moveLeft && !moveRight) {
                        charVelX = hasSpeedBoost ? -10 : -5;
                        charView.setImage(frameLeft[0]);
                    } else if (!moveLeft && moveRight) {
                        charVelX = hasSpeedBoost ? 10 : 5;
                        charView.setImage(frameRight[0]);
                    }

                }

                //teston nese collision nga ndodhur
                if (collided) {
                    //karakteri behet reset te lokacioni i meparshem
                    charX = prevCharX;
                    charY = prevCharY;
                    charVelY = 0;
                    isJumping = false;
                }

                //behet update lokacioni i meparshem
                prevCharX = charX;
                prevCharY = charY;

                //updatohet pozicioni i karakterit
                charView.setLayoutX(charX);
                charView.setLayoutY(charY);

                // Camera follows the character
                double cameraX = charView.getLayoutX() - (double) WIDTH /2;
                double cameraY = charView.getLayoutY() - (double) HEIGHT /2;

                gamePane.setLayoutX(-cameraX);
                gamePane.setLayoutY(-cameraY);

                //update pozicionin e game pane
                gamePane.relocate(-charX + WIDTH / 2, -charY + HEIGHT / 2);

            }
        };
        gameLoop.start();

    }
    private Scene createGameScene() {

        Pane gamePane = new Pane();
        gamePane.setPrefSize(WIDTH, HEIGHT);
        gamePane.setStyle("-fx-background-color: #87CEEB;");

        //dekorimet
        Image image = new Image("cloud2.png");
        ImageView imageView = new ImageView(image);
        imageView.setX(800);
        imageView.setY(100);
        imageView.setFitWidth(400);
        imageView.setFitHeight(400);

        Image image1 = new Image("cloud1.png");
        ImageView imageView1 = new ImageView(image1);
        imageView1.setX(300);
        imageView1.setY(200);
        imageView1.setFitWidth(400);
        imageView1.setFitHeight(400);

        Image image4 = new Image("cloud2.png");
        ImageView imageView4 = new ImageView(image4);
        imageView4.setX(2900);
        imageView4.setY(200);
        imageView4.setFitWidth(400);
        imageView4.setFitHeight(400);

        Image image5 = new Image("cloud1.png");
        ImageView imageView5 = new ImageView(image5);
        imageView5.setX(3400);
        imageView5.setY(220);
        imageView5.setFitWidth(400);
        imageView5.setFitHeight(400);

        Image image6 = new Image("cloud3.png");
        ImageView imageView6 = new ImageView(image6);
        imageView6.setX(4100);
        imageView6.setY(200);
        imageView6.setFitWidth(400);
        imageView6.setFitHeight(400);

        Image image7 = new Image("Bush (1).png");
        ImageView imageView7 = new ImageView(image7);
        imageView7.setX(300);
        imageView7.setY(539);
        imageView7.setFitWidth(80);
        imageView7.setFitHeight(65);

        Image image8 = new Image("Bush (3).png");
        ImageView imageView8 = new ImageView(image8);
        imageView8.setX(820);
        imageView8.setY(642);
        imageView8.setFitWidth(80);
        imageView8.setFitHeight(65);

        Image coinImage = new Image("bamboo.png");
        coinView = new ImageView(coinImage);
        coinView.setFitWidth(40);
        coinView.setFitHeight(40);

        Image coinImage2 = new Image("bamboo.png");
        coinView2 = new ImageView(coinImage2);
        coinView2.setFitWidth(40);
        coinView2.setFitHeight(40);

        Image coinImage3 = new Image("bamboo.png");
        coinView3 = new ImageView(coinImage3);
        coinView3.setFitWidth(40);
        coinView3.setFitHeight(40);

        Image coinImage4 = new Image("bamboo.png");
        coinView4 = new ImageView(coinImage4);
        coinView4.setFitWidth(40);
        coinView4.setFitHeight(40);

        Image coinImage5 = new Image("bamboo.png");
        coinView5 = new ImageView(coinImage5);
        coinView5.setFitWidth(40);
        coinView5.setFitHeight(40);

        Image coinImage6 = new Image("bamboo.png");
        coinView6 = new ImageView(coinImage6);
        coinView6.setFitWidth(40);
        coinView6.setFitHeight(40);


        tree = new Rectangle(200,200);
            String treeimg = "tree.png";
            ImagePattern treeimgp = new ImagePattern(new Image(treeimg));
            tree.setFill(treeimgp);
            tree.setX(150);
            tree.setY(405);

        tree2 = new Rectangle(200,200);
        String treeimg2 = "tree.png";
        ImagePattern treeimgp2 = new ImagePattern(new Image(treeimg2));
        tree2.setFill(treeimgp2);
        tree2.setX(1100);
        tree2.setY(505);

        tree3 = new Rectangle(200,200);
        String treeimg3 = "tree.png";
        ImagePattern treeimgp3 = new ImagePattern(new Image(treeimg3));
        tree3.setFill(treeimgp3);
        tree3.setX(2200);
        tree3.setY(405);

        //mobs
        mob1 = new Rectangle(50,50);
        String mobs = "bee_idle.gif";
        ImagePattern mobpt = new ImagePattern(new Image(mobs));
        mob1.setFill(mobpt);
        mob1.setX(1050);
        mob1.setY(630);

        mob2 = new Rectangle(50,50);
        String mobs2 = "bee_idle.gif";
        ImagePattern mobpt2 = new ImagePattern(new Image(mobs2));
        mob2.setFill(mobpt2);
        mob2.setX(3100);
        mob2.setY(630);

        lborderwall = new Rectangle(100,500);
        lborderwall.setFill(Color.DARKGREEN);
        lborderwall.setOpacity(0);
        lborderwall.setX(0);
        lborderwall.setY(300);

        platforma = new Rectangle(700, 1200);
        String imagePath = "Untitled.png";
        ImagePattern imagePattern = new ImagePattern(new Image(imagePath));
        platforma.setFill(imagePattern);
        platforma.setX(100);
        platforma.setY(600);

        platforma2 = new Rectangle(700,1200);
        String imagePath1 = "mid.png";
        ImagePattern imagePattern1 = new ImagePattern(new Image(imagePath1));
        platforma2.setFill(imagePattern1);
        platforma2.setX(750);
        platforma2.setY(700);

        platforma3 = new Rectangle(1400,600);
        String imagePath3 = "wide.jpg";
        ImagePattern imagePattern3 = new ImagePattern(new Image(imagePath3));
        platforma3.setFill(imagePattern3);
        platforma3.setX(1400);
        platforma3.setY(600);

        platforma4 = new Rectangle(700,1200);
        String imagePath4 = "mid.png";
        ImagePattern imagePattern4 = new ImagePattern(new Image(imagePath4));
        platforma4.setFill(imagePattern4);
        platforma4.setX(2780);
        platforma4.setY(700);


        platforma5 = new Rectangle(1400,600);
        String imagePath5 = "wide.jpg";
        ImagePattern imagePattern5 = new ImagePattern(new Image(imagePath5));
        platforma5.setFill(imagePattern5);
        platforma5.setX(3450);
        platforma5.setY(600);

        platformaflag = new Rectangle(200,200);
        String imagePath6 = "flag.png";
        ImagePattern imagePattern6 = new ImagePattern(new Image(imagePath6));
        platformaflag.setFill(imagePattern6);
        platformaflag.setX(4700);
        platformaflag.setY(440);

        rborderwall = new Rectangle(100,500);
        rborderwall.setFill(Color.DARKGREEN);
        rborderwall.setOpacity(0);
        rborderwall.setX(4800);
        rborderwall.setY(260);

        speedBoostView = new ImageView(new Image("powerup.png"));
        speedBoostView.setFitWidth(40);
        speedBoostView.setFitHeight(40);
        speedBoostView.setX(boostX);
        speedBoostView.setY(boostY);

        gamePane.getChildren().addAll(platforma2,
                platforma,lborderwall,imageView,
                tree,platforma4,platforma3,platforma5,rborderwall,
                imageView1,imageView4,imageView5,
                imageView6,imageView7,imageView8,tree2,tree3,mob1,mob2,platformaflag,speedBoostView

        );

        Scene scene = new Scene(gamePane, WIDTH*3 , HEIGHT);
        scene.setFill(Color.SKYBLUE);

        return scene;
    }
        private void resetGameState() {
            //reset pozicionin e pandes
            charX = platforma.getX();
            charY = platforma.getY() - charView.getFitHeight();
            charVelX = 0;
            charVelY = 0;

            //reset variablat
            moveLeft = false;
            moveRight = false;
            isJumping = false;
            prevCharX = charX;
            prevCharY = charY;

            hasSpeedBoost=false;
            //reset platformen mbyllese
            reachedPlatform = false;
            touchedMob1= false;
            touchedMob2=false;

            resetScore();
            //reset panda
            charView.setImage(frameRight[0]);
        }

    private void resetScore() {
        score = 0;
        coinView.setVisible(true);
        coinView2.setVisible(true);
        coinView3.setVisible(true);
        coinView4.setVisible(true);
        coinView5.setVisible(true);
        coinView6.setVisible(true);
        speedBoostView.setVisible(true);
    }

}