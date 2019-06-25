package com.al264087.chillyrace.Level1;

import android.graphics.Rect;
import android.util.Log;

import com.al264087.chillyrace.Assets;
import com.al264087.chillyrace.FrameWork.Graphics;
import com.al264087.chillyrace.model.Animation;
import com.al264087.chillyrace.model.Sprite;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Level1Model {

        public static int STAGE_WIDTH= 320;
        public static int STAGE_HEIGHT=480;
        public static int PARALLAX_HEIGHT =500;
        public static int PARALLAX_LAYERS=1;
        public static int POOL_OBSTACLES_SIZE = 25;
        public static int POOL_ROCK_SIZE = 20;

        private  float TIME_BETWEEN_OBSTACLES;
        private  float TIME_BETWEEN_ROCK ;

        private static final float UNIT_TIME = 1f / 30;

        private float tickTime;
        private float timeSinceLastObstacle;
        private float timeSinceLastRock;


        private Sprite[] bgParallax;
        private Sprite[] shiftedBgParallax;
        private Sprite ball;
        private Sprite restartButton;
        private List<Sprite> ballPath;
        private Sprite goalSprite;

        private Sprite[]poolRocks;
        private int rockX;
        private int rockY;
        private int poolRocksIndex;
        private Sprite rock;
        private List<Sprite> rockList;
        private int rockWidth;
        private int rockHeight;
        Animation rockAnimation;

        private Sprite[] poolObstacles;
        private int poolObstaclesIndex;
        private List <Sprite> Obstacles;

        Animation ballAnimation;


        private int playerWidth;
        private int playerHeight;


        public String textPoints ;
        public String textCompleteScore;
        public int completeScore;
        public  int points;

        private int maxPath=20;
        private int backgroundspeed=110;
        private int obstacleSpeed = 70;
        private Sprite ballTurn;


        private enum BallState {LEFT, RIGHT}
        private Level1Model.BallState ballState;
        private enum RockState {ROLLING}
        private RockState rockState;
        private Graphics graphics;

        private SoundPlayer soundPlayer;

        private enum EnemyState {CHECK, UNCHECK}
        private  EnemyState enemyState ;


        private enum GameState {WAITING, PLAYING, DIE, END_GAME}
        private GameState gameState;

        private enum GameLevel{EASY, MEDIUM, HARD}
        private GameLevel gameLevel;
        private float levelTime;

        float  xPlayAgain = STAGE_WIDTH/2-20;
        float  yPlayAgain = STAGE_HEIGHT/2 -40;
        float  xEndPlayAgain = STAGE_WIDTH/2 +25;
        float  yEndPlayAgain = STAGE_HEIGHT/2 +20;

        float progressLevel;
        float timeToGoal;
        private int contador = 0;
        private int randomSize;
        boolean activeGoal = false;


    public Level1Model(int playerWidth, int playerHeight, int rockWidth)
    {
        start(playerWidth,playerHeight,rockWidth,graphics, contador);
    }
    public interface SoundPlayer {
        void choque();
        void ding();
        void bolaSound();

    }
    public void restartGame() {

        start( playerWidth,  playerHeight,  rockWidth,  graphics, contador-1);
    }

    public void start(int playerWidth, int playerHeight, int rockWidth, Graphics graphics, int contador)
    {
        gameState = GameState.PLAYING;
        gameLevel = GameLevel.EASY;
        this.contador = contador+=1;
        this.playerWidth = playerWidth;
        this.playerHeight=playerHeight;
        this.rockWidth = rockWidth;
        rockHeight = 50;
        rockX = 0;
        rockY = 0;

        this.tickTime=0f;
        this.bgParallax=new Sprite[PARALLAX_LAYERS];
        this.shiftedBgParallax=new Sprite[PARALLAX_LAYERS];
        poolObstacles = new Sprite[POOL_OBSTACLES_SIZE];
        poolRocks = new Sprite[POOL_ROCK_SIZE];

        rockList = new ArrayList<>();
        Obstacles = new ArrayList<>();
        ballPath=new ArrayList<>(maxPath);

        timeSinceLastObstacle = 0f;
        timeSinceLastRock = 0f;






        if(gameLevel == GameLevel.EASY)//Esto permite que el ball path se mueva a la misma velocidad que el background, que a su vez se mueve
                                        //A la misma velocidad que los obstaculos, cuya velocidad se incrementa con el tiempo
        {
            backgroundspeed = 70;
        }

        else if(gameLevel == GameLevel.MEDIUM)
        {
            backgroundspeed = 100;
        }
        else if(gameLevel == GameLevel.HARD)
        {
            backgroundspeed = 130;
        }


        ballPath.add(new Sprite(Assets.decal, false, STAGE_WIDTH/2, STAGE_HEIGHT/4, 0, -backgroundspeed, Assets.decalWidth, Assets.decalHeight));
        ball = new Sprite(Assets.ball, false, STAGE_WIDTH/2, STAGE_HEIGHT/4,0,0,playerWidth, playerHeight);
        ballState = null;

        restartButton = new Sprite(Assets.endGame,false,xPlayAgain, yPlayAgain, 0, 0,(int) xEndPlayAgain,(int) yEndPlayAgain);
        goalSprite = new Sprite(Assets.goal, true, 0,STAGE_HEIGHT,0,-backgroundspeed, Assets.GOALWIDTH,Assets.GOALHEIGHT);

        ballAnimation=new Animation(0, Assets.BALL_TURN_NUMBER_OF_FRAMES, Assets.BALL_TURN_FRAME_WIDTH, Assets.BALL_TURN_FRAME_HEIGHT, Assets.ballTurnWidth, 15);

        ballTurn = new Sprite(Assets.empty, false, 0, 0,0,0,0, 0);
        ballTurn.addAnimation(ballAnimation);

        rockAnimation = new Animation(1,
                Assets.ROCK_NUMBER_OF_FRAMES,Assets.ROCK_FRAME_WIDTH,Assets.ROCK_FRAME_HEIGHT,
                Assets.ROCK_FRAME_WIDTH*Assets.ROCK_NUMBER_OF_FRAMES, 1);

        for (int i = 0 ; i < POOL_ROCK_SIZE; i++)
        {
            rockState = RockState.ROLLING;
            poolRocks[i] = new Sprite (Assets.rockRolling, false,
                    0, 0, 0, 0, Assets.rockWidth, Assets.rockHeight);
            poolRocks[i].addAnimation(rockAnimation);
        }
        poolRocksIndex = 0;

        for(int i = 0; i< POOL_OBSTACLES_SIZE; i++)
        {
            randomSize = new Random().nextInt(Assets.obstacleWidth+1) + Assets.obstacleWidth-5;
            float randomPositionX = new Random().nextFloat()*(STAGE_WIDTH-25 )+5 ;
            poolObstacles[i] = new Sprite(Assets.tree, false, randomPositionX, STAGE_HEIGHT, 0,-obstacleSpeed,randomSize, randomSize);
            enemyState = EnemyState.UNCHECK;

        }

        poolObstaclesIndex = 0;

        textPoints = "0";
        points = 0;
        textCompleteScore = "0";
        completeScore = 0;

        this.graphics = graphics;
        Assets.mediaPlayer.seekTo(0);
        Assets.mediaPlayer.start();


        levelTime = 0f;

        TIME_BETWEEN_OBSTACLES = new Random().nextFloat()* (2f)+0.7f;
        TIME_BETWEEN_ROCK = new Random().nextInt(10)+ 6;

        progressLevel = 0;
        timeToGoal = 50;
        activeGoal = false;



    }

    public void update(float deltaTime) {

        switch (gameState) {
            case WAITING:

                break;
            case PLAYING:
                playGame(deltaTime);
                break;
            case DIE:
                //Die effects

                gameState = GameState.DIE;
                Assets.mediaPlayer.pause();
                break;
            case END_GAME:


                gameState = GameState.END_GAME;
                Assets.mediaPlayer.pause();
                //activeGoal = true;
                if(touched(xPlayAgain,yPlayAgain) && isOver())
                {
                    contador+= 1;
                    start(playerWidth,  playerHeight,  rockWidth,  graphics, contador);
                }
                break;

        }
    }

    public void playGame(float deltaTime)
    {
        tickTime += deltaTime;
        while (tickTime >= UNIT_TIME) {
            tickTime -= UNIT_TIME;

                if (ballTurn.getAnimation().hasRun()) {

                    ballTurn.setBitmapToRender(Assets.empty);

                }

            updateLevel(deltaTime);
            //updateParallaxBg();
            updateBallPath();
            updateBall();
            updateRock();
            activateObstacles();
            updateObstacles();
            checkCollisions();
            checkScore();
            levelProgress();
            updateGoal(deltaTime);
        }
    }

    public void updateLevel(float deltaTime)
    {
        levelTime += deltaTime;

        if(levelTime >= 40 )
        {
            gameLevel = GameLevel.HARD;
        }

        else if(levelTime >= 20)
        {
            gameLevel = GameLevel.MEDIUM;
        }

       /* else if(levelTime>= timeToGoal)
        {
            gameState = GameState.END_GAME;
        }
        */

    }

    public void updateGoal(float deltaTime)
    {
        timeToGoal -= deltaTime;
        if(timeToGoal < ((STAGE_HEIGHT- ball.getY()) / backgroundspeed)-5)
        {
            goalSprite.moveObstacle(deltaTime);
            activeGoal = true;

        }

        if(ball.getY() >= goalSprite.getY())
        {
            gameState = GameState.END_GAME;

        }

    }

    private void updateObstacles() {

        for (int i = 0 ; i< Obstacles.size(); i++)
        {
            if(gameLevel == GameLevel.MEDIUM)
            {
                Obstacles.get(i).setSpeedY(-100);
            }

            else if(gameLevel == GameLevel.HARD)
            {
                Obstacles.get(i).setSpeedY(-130);
            }


            Obstacles.get(i).moveObstacle(UNIT_TIME);
            if ( Obstacles.get(i).getX()+ Obstacles.get(i).getSizeX()<0){
                Obstacles.remove( Obstacles.get(i));
                i--;
            }

            if( Obstacles.get(i).getY() <=-Obstacles.get(i).getSizeY())
            {
                Obstacles.remove( Obstacles.get(i));
                i--;
            }
        }

    }

    private void updateRock() {

        for(int i = 0; i < poolRocks.length;i++)
        {
            poolRocks[i].moveObstacle(UNIT_TIME);
            Rect frameToBeDrawn = poolRocks[i].getAnimation().getCurrentFrame(UNIT_TIME);
            poolRocks[i].setCurrentFrame(frameToBeDrawn);
        }


        for (int i = 0; i < rockList.size();i++)
        {
            rockList.get(i).moveObstacle(UNIT_TIME);
            if(rockList.get(i).getX() < 0 )
            {
                rockList.remove(rockList.get(i));
                i--;
            }
            else if(rockList.get(i).getX() +rockList.get(i).getSizeX() > STAGE_WIDTH + rockList.get(i).getSizeX())
            {
                rockList.remove(rockList.get(i));
                i--;
            }
        }
    }

    private void checkCollisions(){
        for (int i = 0;i<Obstacles.size();i++)
        {
            if (ball.overlapBoundingBoxes(Obstacles.get(i))){

                if (soundPlayer != null)
                    soundPlayer.choque();
                gameState = GameState.DIE;
            }
        }
        for (int i = 0 ; i < rockList.size();i++)
        {
            if(ball.overlapBoundingBoxes((rockList.get(i))))
            {
                rockList.remove(i);//Modificar, la roca debe matar al jugador.
                if (soundPlayer != null)
                    soundPlayer.choque();

                gameState = GameState.DIE;
            }
        }
        for(int i = 0; i < rockList.size();i++)
        {
            if(rockList.get(i).overlapBoundingBoxes(Obstacles.get(i)))
            {
                Obstacles.remove( Obstacles.get(i));
                i--;
                if (soundPlayer != null)
                    soundPlayer.choque();
            }
        }
    }
    private void activateObstacles() {

        if(gameLevel == GameLevel.MEDIUM)
        {
            TIME_BETWEEN_OBSTACLES =  new Random().nextFloat()* (1.8f)+0.7f;
            TIME_BETWEEN_ROCK = new Random().nextInt(11)+ 5;
        }
        else if(gameLevel == GameLevel.HARD)
        {
            TIME_BETWEEN_OBSTACLES =  new Random().nextFloat()* (1.7f)+0.6f;
            TIME_BETWEEN_ROCK = new Random().nextInt(10)+ 5;
        }
        else
        {
            TIME_BETWEEN_OBSTACLES =  new Random().nextFloat()* (2f)+0.8f;
            TIME_BETWEEN_ROCK = new Random().nextInt(12)+ 6;
        }
        timeSinceLastObstacle += UNIT_TIME;
        timeSinceLastRock += UNIT_TIME;

        if (timeSinceLastObstacle >= TIME_BETWEEN_OBSTACLES) {
            Sprite obstacle = poolObstacles[poolObstaclesIndex];
            poolObstaclesIndex++;
            timeSinceLastObstacle = 0;
            if (poolObstaclesIndex >= POOL_OBSTACLES_SIZE) {
                poolObstaclesIndex = 0;

            }
            obstacle.setY(STAGE_HEIGHT);
            Obstacles.add(obstacle);
        }

        if (timeSinceLastRock >= TIME_BETWEEN_ROCK) {

            Sprite rock = poolRocks[poolRocksIndex];
            poolRocksIndex ++;

            timeSinceLastRock = 0;

            if(poolRocksIndex >= poolRocks.length)
            {
                poolRocksIndex = 0;
            }

            int randomRockX = new Random().nextInt(4 )+1; //Posición Random
            int randomRockY = new Random().nextInt(40 )+STAGE_HEIGHT/2; //Posición Random

            if(gameLevel == GameLevel.EASY)
            {
                int randomSpeedX = new Random().nextInt(40)+30;
                int randomSpeedY = new Random().nextInt(35)+20;

                if (randomRockX <= 4 && randomRockX >= 3) {
                    rock.setX(10);
                    rock.setSpeedX(randomSpeedX);
                } else {
                    rock.setX(STAGE_WIDTH-10);
                    rock.setSpeedX(-randomSpeedX);
                }
                rock.setSpeedY(randomSpeedY);
            }

            else if(gameLevel == GameLevel.MEDIUM)
            {
                int randomSpeedX = new Random().nextInt(40)+30;
                int randomSpeedY = new Random().nextInt(32)+25;
                //Log.d("status", "Acelerandooooo");
                if (randomRockX <= 4 && randomRockX >= 3) {
                    rock.setX(10);
                    rock.setSpeedX(randomSpeedX);
                } else {
                    rock.setX(STAGE_WIDTH-10);
                    rock.setSpeedX(-randomSpeedX);
                }
                rock.setSpeedY(randomSpeedY);
            }
            else
            {
                int randomSpeedX = new Random().nextInt(45)+30;
                int randomSpeedY = new Random().nextInt(40)+30;

                if (randomRockX <= 4 && randomRockX >= 3) {
                    rock.setX(10);
                    rock.setSpeedX(randomSpeedX);
                } else {
                    rock.setX(STAGE_WIDTH-10);
                    rock.setSpeedX(-randomSpeedX);
                }
                rock.setSpeedY(randomSpeedY);

            }
            if(rock.isAnimated())
            {
                rock.getAnimation(0).resetAnimation();
            }

            rockList.add(rock);
        }
    }


    private void updateBallPath(){
        for(int i =0; i< ballPath.size();i++){
            ballPath.get(i).move(UNIT_TIME);
        }
    }

    private void updateBall()
    {
        ballPath.add(new Sprite(Assets.decal, false, ball.getX(), ball.getY(), 0, -backgroundspeed, Assets.decalWidth, Assets.decalHeight));
        ball.move(UNIT_TIME);
        Rect frameToBeDrawn =ballAnimation.getCurrentFrame(UNIT_TIME);
        ballTurn.setCurrentFrame(frameToBeDrawn);

        if (ball.getX()<= -6 || ball.getX()+ball.getSizeX() > STAGE_WIDTH+20)
        {
            gameState = GameState.DIE;
        }
    }

    private void updateScore( )
    {
        if(enemyState == EnemyState.CHECK) {
            points = 5;
            textPoints = Integer.toString(points);

            completeScore = completeScore + points;
            textCompleteScore = Integer.toString(completeScore);
            if (soundPlayer != null)
                soundPlayer.ding();

            enemyState = EnemyState.UNCHECK;
        }
    }


    private int checkScore()
    {
        for(int i = 0; i < poolObstacles.length; i++)
        {
            enemyState = EnemyState.UNCHECK;
            if(ball.getX()<= poolObstacles[i].getX()+poolObstacles[i].getSizeX() +5 &&
                    ball.getX() + ball.getSizeX() >= (poolObstacles[i].getX())-5)
            {
                if(ball.getY()<= poolObstacles[i].getY()+poolObstacles[i].getSizeY() +5 &&
                        ball.getY() + ball.getSizeY() >= (poolObstacles[i].getY())-5) {

                        enemyState = EnemyState.CHECK;
                        updateScore();
                }
            }

        }
        return points;
    }

    /*private void updateParallaxBg() {

        for(int i =0; i<PARALLAX_LAYERS;i++){

            bgParallax[i].move(UNIT_TIME);
            shiftedBgParallax[i].move(UNIT_TIME);
            if(bgParallax[i].getY()<=-PARALLAX_HEIGHT){
                bgParallax[i].setY(PARALLAX_HEIGHT);

            }
            if(shiftedBgParallax[i].getY()<=-PARALLAX_HEIGHT){
                shiftedBgParallax[i].setY(PARALLAX_HEIGHT);
            }

        }
    }*/
    public boolean touched(float scaleX, float scaleY)
    {

        if((scaleX < xEndPlayAgain && scaleX> xPlayAgain))
        {
            if((scaleY < yEndPlayAgain && scaleY > yPlayAgain))
            {
                return true;
            }
        }
        return false;
    }



    public void onTouch(float scaleX, float scaleY) {


        if(isDead() && touched( scaleX , scaleY) || ((isOver() && touched(scaleX, scaleY))))
        {
            gameState = GameState.WAITING;
            start(playerHeight,playerHeight,rockWidth,graphics,contador);
        }

        else if (isPlaying())
        {

            if (ballState==null) {
                if (scaleX >= STAGE_WIDTH / 2){
                    ballState= Level1Model.BallState.RIGHT;
                    ball.setSpeedX(115);

                    ballTurn.setBitmapToRender(Assets.ballTurningFlip);

                    ballTurn.getAnimation().resetAnimation();
                    if (soundPlayer != null)
                        soundPlayer.bolaSound();
                }
                else{
                    ballState= Level1Model.BallState.LEFT;

                    ballTurn.setBitmapToRender(Assets.ballTurning);

                    ballTurn.getAnimation().resetAnimation();
                    ball.setSpeedX(-115);
                    if (soundPlayer != null)
                        soundPlayer.bolaSound();
                }

            }
            else{
                if(ballState== Level1Model.BallState.RIGHT){
                    ball.setSpeedX(-115);


                    ballTurn.setBitmapToRender(Assets.ballTurning);


                    ballTurn.getAnimation().resetAnimation();
                    if (soundPlayer != null)
                        soundPlayer.bolaSound();
                    ballState= Level1Model.BallState.LEFT;
                }
                else{
                    ball.setSpeedX(115);


                    ballTurn.setBitmapToRender(Assets.ballTurningFlip);


                    ballTurn.getAnimation().resetAnimation();
                    if (soundPlayer != null)
                        soundPlayer.bolaSound();
                    ballState= Level1Model.BallState.RIGHT;
                }
            }
        }
        if (isWaiting())
            gameState = GameState.PLAYING;
    }

    public void onLongTouch(float scaleX, float scaleY) {


        if (ballState==null) {
            if (scaleX >= STAGE_WIDTH / 2){
                ballState= Level1Model.BallState.RIGHT;
                ball.setSpeedX(60);

                ballTurn.setBitmapToRender(Assets.ballTurningFlip);
                ballTurn.getAnimation().resetAnimation();
                if (soundPlayer != null)
                    soundPlayer.bolaSound();
            }
            else{
                ballState= Level1Model.BallState.LEFT;
                ball.setSpeedX(-60);

                ballTurn.setBitmapToRender(Assets.ballTurning);
                ballTurn.getAnimation().resetAnimation();
                if (soundPlayer != null)
                    soundPlayer.bolaSound();
            }

        }
        else{
            if(ballState== Level1Model.BallState.RIGHT){
                ball.setSpeedX(-60);

                ballTurn.setBitmapToRender(Assets.ballTurning);
                ballTurn.getAnimation().resetAnimation();
                if (soundPlayer != null)
                    soundPlayer.bolaSound();
                ballState= Level1Model.BallState.LEFT;
            }
            else{
                ball.setSpeedX(60);

                ballTurn.setBitmapToRender(Assets.ballTurningFlip);
                ballTurn.getAnimation().resetAnimation();
                if (soundPlayer != null)
                    soundPlayer.bolaSound();
                ballState= Level1Model.BallState.RIGHT;
            }
        }
    }


    public boolean isWaiting()
    {
        if(gameState == GameState.WAITING)
        {
            return true;
        }
        else
            return false;
    }

    public boolean isPlaying()
    {
        if(gameState == GameState.PLAYING)
        {
            return true;
        }
        else
            return false;
    }

    public boolean isDead()
    {
        if(gameState == GameState.DIE)
        {
            return true;
        }
        else
            return false;
    }

    public boolean isOver()
    {
        if(gameState == GameState.END_GAME)
        {
            return true;
        }
        else
            return false;
    }

    public boolean isEasy()
    {
        if(gameLevel == GameLevel.EASY)
        {
            return true;
        }
        else
            return false;
    }
    public boolean isMedium()
    {
        if(gameLevel == GameLevel.MEDIUM)
        {
            return true;
        }
        else
            return false;
    }

    public boolean isHard()
    {
        if(gameLevel == GameLevel.HARD)
        {
            return true;
        }
        else
            return false;
    }

    public  boolean chargeGoal()
    {
        if(activeGoal == true)
        {
            return true;
        }
        return false;
    }

    public float levelProgress()
    {
        if(ball.getY() >= goalSprite.getY())
        {
            gameState = GameState.END_GAME;
        }

        return levelTime ;
    }




    public int getContador()
    {
        return contador;
    }

    public String getStringLevelTime()
    {
        return String.valueOf((int)levelTime);
    }

    public float getLevelTime()
    {
        return levelTime;
    }

    public Sprite getButton()
    {
        return restartButton;
    }

    public Sprite getBall()
    {
        return ball;
    }


    public List<Sprite> getRocks()
    {
        return rockList;
    }
    public List<Sprite> getBallPath(){return ballPath;}


    public Sprite[] getBgParallax() {
        return bgParallax;
    }

    public Sprite[] getShiftedBgParallax() {
        return shiftedBgParallax;
    }

    public List<Sprite> getObstacles() {
        return Obstacles;
    }
    public void setSoundPlayer(SoundPlayer soundPlayer) {
        this.soundPlayer = soundPlayer;
    }


    public float getPositionXEndButton()
    {
        return xPlayAgain;
    }

    public float getPositionYEndButton()
    {
        return yPlayAgain;
    }

    public float getPositionXEndButtonWITDH()
    {
        return xEndPlayAgain;
    }

    public float getPositionYEndButtonHEIGHT()
    {
        return yEndPlayAgain;
    }

    public Sprite getBallTurn() {
        return ballTurn;
    }

    public float getGoalHEIGHT(){ return goalSprite.getY();}

    public Sprite getGoalSprite(){return goalSprite;}

}


