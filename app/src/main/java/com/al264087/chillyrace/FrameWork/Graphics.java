package com.al264087.chillyrace.FrameWork;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.al264087.chillyrace.R;

import static android.graphics.Bitmap.Config;

/**
 * Created by jvilar on 29/03/16.
 * Modified by jcamen on 15/01/17 and 09/03/19
 */
public class Graphics {
    Bitmap frameBuffer;
    Canvas canvas;
    Paint paint;
   Context context;

    public Graphics(Context context, int width, int height) {

        this.frameBuffer = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        canvas = new Canvas(frameBuffer);
        paint = new Paint();
        this.context = context;
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(20);
        // Replace the last statement with the following ones if you want
        // to use a custom font
        /* Typeface typeface = ResourcesCompat.getFont(context, R.font.CUSTOM_FONT_HERE);
        Typeface typefaceBold = Typeface.create(typeface, Typeface.BOLD);
        paint.setTypeface(typefaceBold); */
        paint.setTypeface(Typeface.DEFAULT_BOLD);

    }

    public Bitmap getFrameBuffer() {
        return frameBuffer;
    }

    public void clear(int color) {
        canvas.drawRGB((color & 0xff0000) >> 16, (color & 0xff00) >> 8, color & 0xff);
    }
    public Bitmap changeColor(Bitmap sourceBitmap, int color) {

        Bitmap resultBitmap = sourceBitmap.copy(sourceBitmap.getConfig(),true);
        Paint paint = new Paint();
       // ColorFilter filter = new PorterDuffColorFilter(ContextCompat.getColor(context, color), PorterDuff.Mode.SRC_ATOP);
        ColorFilter filter = new LightingColorFilter(color,250);
        paint.setColorFilter(filter);
        paint.setAlpha(150);
        Canvas canvas = new Canvas(resultBitmap);
        canvas.drawBitmap(resultBitmap, 0, 0, paint);
        return resultBitmap;

    }

    public void drawRect(float x, float y, int width, int height, int color) {
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(x, y, x + width - 1, y + height - 1, paint);
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }


    public void drawBitmap(Bitmap bitmap, float x, float y, boolean flip) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        if (flip) {
            Matrix flipHorizontal = new Matrix();
            flipHorizontal.setScale(-1.0f, 1.0f);
            flipHorizontal.postTranslate(bitmap.getWidth(), 0);
            Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), flipHorizontal, true);
            canvas.drawBitmap(bmp, x, y, paint);
        }
        else
            canvas.drawBitmap(bitmap, x, y, paint);
    }

    public void drawAnimatedBitmap(Bitmap bitmap, Rect frameToDraw, RectF whereToDraw, boolean flip) {
        if (flip) {
            Matrix flipHorizontal = new Matrix();
            flipHorizontal.setScale(-1.0f, 1.0f);
            flipHorizontal.postTranslate(bitmap.getWidth(), 0);
            Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), flipHorizontal, true);
            canvas.drawBitmap(bmp, frameToDraw, whereToDraw, null);
        }
        else
            canvas.drawBitmap(bitmap, frameToDraw, whereToDraw, null);
    }

    public void drawLine(float startX, float startY, float stopX, float stopY, int color, float width) {
        paint.setColor(color);
        paint.setStrokeWidth(width);
        canvas.drawLine(startX, startY, stopX, stopY, paint);
    }

    public void drawText(String s, float x, float y, int color, int fontSize) {
        paint.setColor(color);
        paint.setTextSize(fontSize);
        canvas.drawText(s, x, y, paint);
    }

    public int getWidth() {
        return frameBuffer.getWidth();
    }

    public int getHeight() {
        return frameBuffer.getHeight();
    }
}
