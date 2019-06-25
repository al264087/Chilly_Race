package com.al264087.chillyrace;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;


import com.al264087.chillyrace.FrameWork.ScalingUtilities;

import com.al264087.chillyrace.FrameWork.Sound;


public class Assets {

    private static final int MAX_SIMULT_STREAMS = 10;
    public static Sound wood;
    public static Sound bolasound;
    public static Sound ding;

    public static final int BALL_FRAME_WIDTH = 20;
    public static final int BALL_FRAME_HEIGHT = 20;

    public static int  playerWidth;
    public static int playerHeight;
    public static Bitmap[] bgLayers;
    public static Bitmap ball;
    public static Bitmap tree;
    public static Bitmap treeShadow;


    public static Bitmap rockRolling;
    public static Bitmap ballTurning;
    public static Bitmap ballTurningFlip;
    public static Bitmap endGame;
    public static Bitmap decal;
    public static Bitmap empty;
    public static Bitmap goal;

    public static int TREE_FRAME_WIDTH = 80;
    public static int TREE_FRAME_HEIGHT = 144;
    public static int ballSourceId;

    public static int  rockWidth = 40;//Tamaño en pixeles de la roca , no del asset
    public static int rockHeight = 40;
    public static int obstacleWidth=20;

    public static int ballTurnWidth=493;
    public static int ballTurnHeight=127;

    public static int BUTTON_WIDH = 47;
    public static int BUTTON_HEIGHT = 47;

    public static int BALL_TURN_NUMBER_OF_FRAMES=4;
    public static int BALL_TURN_FRAME_WIDTH=123;
    public static int BALL_TURN_FRAME_HEIGHT=127;

    public static int ROCK_NUMBER_OF_FRAMES = 5;
    public static final int ROCK_FRAME_WIDTH = 57;
    public static final int ROCK_FRAME_HEIGHT = 50;

    public static final int GOALWIDTH = 442;
    public static final int GOALHEIGHT = 182;
    public static int goalWidth;
    public static int goalHeight;

    public  static  MediaPlayer mediaPlayer;
    public static int decalSourceId;
    public static int decalWidth=13;
    public static int decalHeight=13;



    private float lastFrameChangeTime = 0;



    public static void createAssets(Context context, int playerWidth,  int stageWidth, int parallaxHeight) {

        Resources resources = context.getResources();
        ballSourceId = R.drawable.ball17;
        decalSourceId=R.drawable.decal;



        if (bgLayers != null)
            for (Bitmap bitmap : bgLayers)
                bitmap.recycle();

        int[] bgLayersResources = {
                R.drawable.bg1,
                R.drawable.bg2
        };


        bgLayers = new Bitmap[bgLayersResources.length];
        for (int i = 0; i < bgLayers.length; i++) {
            bgLayers[i] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, bgLayersResources[i]),
                    stageWidth, parallaxHeight, true);
        }


        if(rockRolling != null)
        {
            Bitmap bitmap = rockRolling;
            bitmap.recycle();
        }

        if (endGame != null) {
            Bitmap bitmap = endGame;
            bitmap.recycle();
        }

        rockHeight = (ROCK_FRAME_HEIGHT * rockWidth)/ ROCK_FRAME_WIDTH;
        rockRolling = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.drawable.roca),ROCK_FRAME_WIDTH *
                ROCK_NUMBER_OF_FRAMES, rockHeight, true);


        playerHeight = (BALL_FRAME_HEIGHT * playerWidth) / BALL_FRAME_WIDTH;

        int goalResources = R.drawable.goal;
        goal = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources,goalResources),GOALWIDTH, GOALHEIGHT/2,true);

        ballTurning=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.drawable.ballturningv2), ballTurnWidth, ballTurnHeight, true);
        ballTurningFlip=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.drawable.ballturningv2flipv2), ballTurnWidth, ballTurnHeight, true);
        empty= Bitmap.createScaledBitmap
                (BitmapFactory.decodeResource(resources, R.drawable.ballturnaux),playerWidth,playerHeight,true );


        endGame = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources,R.drawable.endgame),BUTTON_WIDH,BUTTON_HEIGHT, true);

        tree = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.drawable.treescaled),TREE_FRAME_WIDTH,TREE_FRAME_HEIGHT,true );

        treeShadow = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.drawable.treescaledshadow),130,64,true );

        mediaPlayer = MediaPlayer.create(context,R.raw.backgroundmusic);
        mediaPlayer.setLooping(true);


        SoundPool soundPool = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                ? createNewSoundPool() : createOldSoundPool();
        wood = new Sound(soundPool.load(context, R.raw.wood, 0),
                soundPool);
        bolasound = new Sound(soundPool.load(context, R.raw.bolasound,0),soundPool);
        ding = new Sound(soundPool.load(context,R.raw.ding,0),soundPool);

        Bitmap unscaledBitmap = ScalingUtilities.decodeResource(resources, ballSourceId,
                playerWidth, playerHeight, ScalingUtilities.ScalingLogic.FIT);

        Bitmap scaledBitmap = ScalingUtilities.createScaledBitmap(unscaledBitmap, playerWidth,
                playerHeight, ScalingUtilities.ScalingLogic.FIT);
        unscaledBitmap.recycle();
        ball=scaledBitmap;

        Bitmap unscaledDecalBitmap = ScalingUtilities.decodeResource(resources, decalSourceId,
                decalWidth, decalHeight, ScalingUtilities.ScalingLogic.FIT);
        Bitmap scaledDecalBitmap = ScalingUtilities.createScaledBitmap(unscaledDecalBitmap, decalWidth,
                decalHeight, ScalingUtilities.ScalingLogic.FIT);
        unscaledDecalBitmap.recycle();
        decal=scaledDecalBitmap;

       /* Bitmap unscaledTreeBitmap = ScalingUtilities.decodeResource(resources, treeSourceId,
                obstacleWidth, obstacleHeight, ScalingUtilities.ScalingLogic.FIT);
        Bitmap scaledTreeBitmap = ScalingUtilities.createScaledBitmap(unscaledTreeBitmap, decalWidth,
                decalHeight, ScalingUtilities.ScalingLogic.FIT);
        unscaledTreeBitmap.recycle();
        tree=scaledTreeBitmap;*/


    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static SoundPool createNewSoundPool() {
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        return new SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .setMaxStreams(MAX_SIMULT_STREAMS)
                .build();
    }
    private static SoundPool createOldSoundPool() {
        return new SoundPool(MAX_SIMULT_STREAMS, AudioManager.STREAM_MUSIC, 0);
    }

}








