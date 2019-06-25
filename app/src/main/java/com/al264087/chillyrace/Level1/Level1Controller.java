package com.al264087.chillyrace.Level1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.RectF;

import com.al264087.chillyrace.Assets;
import com.al264087.chillyrace.FrameWork.Graphics;
import com.al264087.chillyrace.FrameWork.IGameController;
import com.al264087.chillyrace.FrameWork.TouchHandler;
import com.al264087.chillyrace.R;
import com.al264087.chillyrace.model.Sprite;

import java.util.List;

import static com.al264087.chillyrace.FrameWork.TouchHandler.TouchType.LONG_TOUCH;
import static com.al264087.chillyrace.FrameWork.TouchHandler.TouchType.TOUCH_DOWN;
import static com.al264087.chillyrace.FrameWork.TouchHandler.TouchType.TOUCH_UP;
import static com.al264087.chillyrace.Level1.Level1Model.PARALLAX_HEIGHT;
import static com.al264087.chillyrace.Level1.Level1Model.PARALLAX_LAYERS;
import static com.al264087.chillyrace.Level1.Level1Model.STAGE_HEIGHT;
import static com.al264087.chillyrace.Level1.Level1Model.STAGE_WIDTH;

public class Level1Controller implements IGameController {

    Context context;
    Graphics graphics;
    private static final float PLAYER_WIDTH_PERCENT = 0.06f;
    float scaleX;
    float scaleY;
    int screenWidth;
    int screenHeight;
    Level1Model model;
    private int playerWidth;
    private int playerHeight;

    int color;
    RectF ballAnimationRectF;

    Level1Model.SoundPlayer soundPlayer;


    int colorObstacle;
    private int rockWidth;

    public Level1Controller(Context context, int width, int height) {
        this.context = context;
        this.screenWidth = width;
        this.screenHeight = height;
        this.graphics = new Graphics(context, STAGE_WIDTH, STAGE_HEIGHT);
        scaleX = (float) STAGE_WIDTH / screenWidth;
        scaleY = (float) STAGE_HEIGHT / screenHeight;
        playerWidth = (int) (STAGE_WIDTH * PLAYER_WIDTH_PERCENT);
        playerHeight=(STAGE_HEIGHT*playerWidth)/STAGE_WIDTH;
        rockWidth =  (int) (STAGE_WIDTH * PLAYER_WIDTH_PERCENT);

        Assets.createAssets(context, playerWidth, STAGE_WIDTH, PARALLAX_HEIGHT);
        model = new Level1Model(playerWidth,playerHeight, rockWidth);

        model.setSoundPlayer(new Level1Model.SoundPlayer() {
            @Override
            public void choque() {
                Assets.wood.play(0.8f);
            }

            @Override
            public void ding() {
                Assets.ding.play(0.8f);
            }

            @Override
            public void bolaSound() {
                Assets.bolasound.play(0.4f);
            }

        }) ;
    }

    @Override
    public void onUpdate(float deltaTime, List<TouchHandler.TouchEvent> touchEvents) {

        model.update(deltaTime);
        for (TouchHandler.TouchEvent event : touchEvents) {
            if (event.type == TOUCH_DOWN) {
                model.onTouch(event.x * scaleX, event.y * scaleY);

            }
            else if(event.type==LONG_TOUCH){
                model.onLongTouch(event.x * scaleX, event.y * scaleY);
            }
        }

        for (TouchHandler.TouchEvent event: touchEvents) {
            float xScaled = event.x * scaleX;
            float yScaled = event.y * scaleY;
            if (event.type == TOUCH_UP) {
                if (model.isOver() && model.getPositionXEndButton() <= xScaled && xScaled <=
                        model.getPositionXEndButtonWITDH() && model.getPositionYEndButton() <= yScaled && yScaled <=
                        model.getPositionYEndButtonHEIGHT()) {
                    model.restartGame();
                    break;
                }
            }
        }
    }
    @Override
    public Bitmap onDrawingRequested() {
        graphics.clear(0xFFFFFFFF);

        Sprite ball = model.getBall();
        List<Sprite> Obstacles = model.getObstacles();
        List<Sprite> Rocks = model.getRocks();
        Sprite endGame = model.getButton();
        Sprite goal = model.getGoalSprite();
        List<Sprite> BallPath = model.getBallPath();
        Sprite ballTurn=model.getBallTurn();

        color = R.color.colorAccent;


        Bitmap decalColor=graphics.changeColor(Assets.decal,color);
        for (int i = 0; i < BallPath.size(); i++) {


            graphics.drawBitmap(decalColor, BallPath.get(i).getX(), BallPath.get(i).getY(), false);
        }

        if(ballTurn.getBitmapToRender()==Assets.ballTurningFlip){

            ballAnimationRectF = new RectF(ball.getX()-15, ball.getY()-15,ball.getX()+ ball.getSizeX()-15, ball.getY()+ball.getSizeY()-15);

        }
        else{
            ballAnimationRectF = new RectF(ball.getX()+15, ball.getY()-15,ball.getX()+ ball.getSizeX()+15, ball.getY()+ball.getSizeY()-15);

        }

        graphics.drawAnimatedBitmap(ballTurn.getBitmapToRender(), ballTurn.getCurrentFrame(), ballAnimationRectF, true);

        if(model.chargeGoal() == true)
        {
            graphics.drawBitmap(goal.getBitmapToRender(), 0, model.getGoalHEIGHT(), false);
        }

        if (model.getContador()== 0)
        {
             color = R.color.colorAccent;
        }
        else if (model.getContador() ==1)
        {
             color = Color.GREEN;
             colorObstacle = Color.RED;
        }
        else if ((model.getContador() ==2))
        {
             color = Color.RED;
             colorObstacle = Color.YELLOW;
        }

        Bitmap ballColor=graphics.changeColor(ball.getBitmapToRender(), color );
        graphics.drawBitmap(ballColor, ball.getX(), ball.getY(), false);

        Bitmap obstacleColor=graphics.changeColor(Assets.tree, color );
        for (Sprite sprite : Obstacles)
        {
            Bitmap spriteCollor = graphics.changeColor(sprite.getBitmapToRender(), colorObstacle);
            RectF rectFObstacle=new RectF(sprite.getX(), sprite.getY(), sprite.getX()+sprite.getSizeX(), sprite.getY()+sprite.getSizeY());
            graphics.drawAnimatedBitmap(obstacleColor, ball.getCurrentFrame(), rectFObstacle, false);

        }
        for (int i = 0 ; i < Rocks.size();i++)
        {
            RectF rectf = new RectF(Rocks.get(i).getX(), Rocks.get(i).getY(),Rocks.get(i).getX()+ Rocks.get(i).getSizeX(), Rocks.get(i).getY()+Rocks.get(i).getSizeY());
            graphics.drawAnimatedBitmap(Rocks.get(i).getBitmapToRender(), Rocks.get(i).getCurrentFrame(), rectf, false);
        }
        if(model.isDead())
        {
            graphics.clear(Color.WHITE);
            //RectF rectFEndButton = new RectF(xPlayAgain, yPlayAgain, xEndPlayAgain, yEndPlayAgain);
            graphics.drawText("Total Points" + model.textCompleteScore,STAGE_WIDTH/2 -75 , STAGE_HEIGHT/2+40, Color.RED,30);
            graphics.drawText("TIME:        " +model.getLevelTime(),STAGE_WIDTH/2 -75 , STAGE_HEIGHT/2 -40 , Color.RED, 25);
            graphics.drawBitmap(endGame.getBitmapToRender(), model.getPositionXEndButton(),model.getPositionYEndButton(),false);

        }

        if(model.getLevelTime()  < 56 && (model.isOver() == false || model.isDead() == false))
        {
            graphics.drawRect(55, 10, 4* (int)model.levelProgress(),20, Color.MAGENTA);
        }



       if(model.isOver())
        {
            graphics.clear(Color.WHITE);
            //RectF rectFEndButton = new RectF(xPlayAgain, yPlayAgain, xEndPlayAgain, yEndPlayAgain);
            graphics.drawText("NIVEL SUPERADOOOOO: " +model.getContador(),5 , STAGE_HEIGHT/2+40, Color.RED,20);
            graphics.drawText("TIME:        " +model.getLevelTime(),STAGE_WIDTH/2 -75 , STAGE_HEIGHT/2 -40 , Color.RED, 25);
            graphics.drawBitmap(endGame.getBitmapToRender(), model.getPositionXEndButton(),model.getPositionYEndButton(),false);
        }




        if(model.isPlaying() || model.isWaiting())
        {
            graphics.drawText("" + model.getContador(), 55, 20, Color.BLUE,20);
            graphics.drawText("" + (model.getContador()+1), 270, 20, Color.BLUE,20);
            graphics.drawText("Total Points" + model.textCompleteScore, 25, 10, Color.BLACK,10);
            //graphics.drawText("TIME:        " +model.getLevelTime(),150, 10, Color.BLACK, 10);
        }
        //graphics.drawText("Total Points" + model.textCompleteScore, STAGE_WIDTH/2, STAGE_HEIGHT-30, android.R.color.white,200);

        return graphics.getFrameBuffer();
    }
}




