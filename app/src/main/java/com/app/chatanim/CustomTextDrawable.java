package com.app.chatanim;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

public class CustomTextDrawable extends Drawable {

    private final Paint textPaint;
    private final String text;
    private final int fontSize;
    private final int width, height;

    private CustomTextDrawable(Builder builder) {

        width = builder.width;
        height = builder.height;
        // text and color
        text = builder.text;

        // text paint settings
        fontSize = builder.fontSize;
        textPaint = new Paint();
        textPaint.setColor(builder.textColor);
        textPaint.setAntiAlias(true);
        textPaint.setFakeBoldText(builder.isBold);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTypeface(builder.font);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setStrokeWidth(builder.borderThickness);

    }

    @Override
    public void draw(Canvas canvas) {
        Rect r = getBounds();

        int count = canvas.save();
        canvas.translate(r.left, r.top);

        // draw text
        int width = this.width < 0 ? r.width() : this.width;
        int height = this.height < 0 ? r.height() : this.height;
        int fontSize = this.fontSize < 0 ? (int) (Math.min(width, height) * 0.75) : this.fontSize;
        textPaint.setTextSize(fontSize);
        //noinspection IntegerDivisionInFloatingPointContext
        canvas.drawText(text, width / 2, height / 2 - ((textPaint.descent() + textPaint.ascent()) / 2), textPaint);

        canvas.restoreToCount(count);

    }

    @Override
    public void setAlpha(int alpha) {
        textPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        textPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @SuppressWarnings({"unused"})
    public static class Builder {

        private String text;

        private int borderThickness;

        private int width;

        private int height;

        private Typeface font;

        public int textColor;

        private int fontSize;

        private boolean isBold;

        public float radius;

        public Builder() {
            text = "";
            textColor = Color.BLACK;
            borderThickness = 0;
            width = -1;
            height = -1;
            font = Typeface.create("sans-serif-light", Typeface.NORMAL);
            fontSize = -1;
            isBold = false;
        }

        public Builder width(int width) {
            this.width = width;
            return this;
        }

        public Builder height(int height) {
            this.height = height;
            return this;
        }

        public Builder textColor(int color) {
            this.textColor = color;
            return this;
        }

        public Builder useFont(Typeface font) {
            this.font = font;
            return this;
        }

        public Builder fontSize(int size) {
            this.fontSize = size;
            return this;
        }

        public Builder bold() {
            this.isBold = true;
            return this;
        }

        public CustomTextDrawable build(String text) {
            this.text = text;
            return new CustomTextDrawable(this);
        }
    }
}