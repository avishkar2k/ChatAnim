package com.app.chatanim;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TextAdapter extends RecyclerView.Adapter<TextAdapter.ChatViewHolder> {
    private static final String TAG = "TextAdapter";
    private final ArrayList<String> mListMsg;
    private final Activity mActivity;
    private final DisplayMetrics dm;
    private final AnimationChatEnd animationChatEnd;

    public TextAdapter(Activity mActivity, ArrayList<String> mListMsg, AnimationChatEnd anim) {
        this.mListMsg = mListMsg;
        this.mActivity = mActivity;
        dm = new DisplayMetrics();
        animationChatEnd = anim;
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        holder.bind(mListMsg.get(position));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (!payloads.isEmpty() && (boolean) payloads.get(0)) holder.scaleDown();
        else super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public int getItemCount() {
        return mListMsg.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        private TextView tvChatMsg, tvDelivered;
        private FrameLayout fl;

        public ChatViewHolder(@NonNull ViewGroup parent, int viewType) {
            super(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_chat_bubble, parent, false));
            fl = itemView.findViewById(R.id.fl);
            tvChatMsg = itemView.findViewById(R.id.tv_chat_msg);
            tvDelivered = itemView.findViewById(R.id.tv_delivered);
        }

        private void anim() {
            float x = tvChatMsg.getMeasuredWidth() + itemView.getContext().getResources()
                    .getDimensionPixelSize(R.dimen._10dp);
            float y = fl.getY() - getAdapterPosition() == 0 ? 0 : tvChatMsg.getMeasuredHeight();
            Log.d(TAG, "anim: x " + x + " y " + y);
            Log.d(TAG, "anim: dm width " + dm.widthPixels + " height " + dm.heightPixels);
            chatAnim(x, y);
        }


        private void chatAnim(float x, float y) {
            tvChatMsg.clearAnimation();
            float toX = dm.widthPixels - x;
            float fromX = (dm.widthPixels) / 2;
            float fromY = dm.heightPixels - 50;
            float toY = (float) (dm.heightPixels - (fromY * 0.45));
            int duration = 800;
            //animation listener
            AnimationSet.AnimationListener animationListener = new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    tvChatMsg.clearAnimation();//check
                    ((FrameLayout.LayoutParams) tvChatMsg.getLayoutParams()).gravity = Gravity.END;
                    animationChatEnd.animEnd(getAdapterPosition());
                    scaleUp();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            };

            //translate Y
            TranslateAnimation translateY = new TranslateAnimation(toX, toX, toY, y);
            translateY.setDuration(duration / 2);
            translateY.setFillEnabled(true);
            translateY.setFillAfter(true);
            translateY.setInterpolator(new AccelerateDecelerateInterpolator());
            translateY.setAnimationListener(animationListener);
            //translate X
            TranslateAnimation translateXY = new TranslateAnimation(fromX, toX, fromY, toY);
            translateXY.setDuration(duration / 2);
            translateXY.setFillEnabled(true);
            translateXY.setFillAfter(true);
            translateXY.setInterpolator(new AccelerateInterpolator());
            translateXY.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    tvChatMsg.setAnimation(translateY);
                    translateY.start();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            //set animation set and start the animation
            tvChatMsg.setAnimation(translateXY);
            translateXY.start();
        }

        private void scaleUp() {
            //translate the message up by height of delivery text
            tvChatMsg.clearAnimation();
            fl.clearAnimation();
            Log.d(TAG, "scaleUp: y " + tvChatMsg.getY() + " height " + tvDelivered.getMeasuredHeight());
            if (getAdapterPosition() > 0) {
                float x = tvChatMsg.getX();
                float fromY = tvChatMsg.getY();
                float toY = fromY - (tvChatMsg.getMeasuredHeight() / 1.8f);
                TranslateAnimation translationYBy = new TranslateAnimation(x, x, fromY, toY);
                translationYBy.setDuration(1400);
                translationYBy.setInterpolator(new DecelerateInterpolator());
                translationYBy.setFillAfter(true);
                translationYBy.setFillEnabled(true);
                fl.setAnimation(translationYBy);
                translationYBy.start();
            }
            //scale up the delivery text
            tvDelivered.clearAnimation();
            tvDelivered.setVisibility(View.VISIBLE);
            ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 1f, 0f, 1f,
                    tvDelivered.getMeasuredWidth() / 2, tvDelivered.getMeasuredHeight() / 2);
            scaleAnimation.setDuration(1200);
            scaleAnimation.setInterpolator(new DecelerateInterpolator());
            scaleAnimation.setFillAfter(true);
            scaleAnimation.setFillEnabled(true);
            tvDelivered.setAnimation(scaleAnimation);
            scaleAnimation.start();
            //tvDelivered.setVisibility(View.VISIBLE);
        }

        private void scaleDown() {
            //scale the text duration
            tvDelivered.clearAnimation();
            ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 1f, 1f, 0f,
                    tvDelivered.getMeasuredWidth() / 2, tvDelivered.getMeasuredHeight() / 2);
            scaleAnimation.setDuration(1200);
            scaleAnimation.setInterpolator(new DecelerateInterpolator());
            scaleAnimation.setFillAfter(true);
            scaleAnimation.setFillEnabled(true);
            tvDelivered.setAnimation(scaleAnimation);
            scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    tvDelivered.setVisibility(View.GONE);
                    tvDelivered.post(() -> {
                        fl.setMinimumHeight(tvChatMsg.getMeasuredHeight());
                        fl.invalidate();
                    });
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            scaleAnimation.start();
        }

        private void bind(String s) {
            tvChatMsg.setText(s);
            tvChatMsg.post(() -> {
                tvDelivered.setVisibility(View.INVISIBLE);
                fl.setMinimumHeight((int) (tvChatMsg.getMeasuredHeight() * 1.5f));
                tvDelivered.setVisibility(View.GONE);
                mActivity.runOnUiThread(this::anim);
            });
        }
    }

    public interface AnimationChatEnd {
        void animEnd(int position);
    }
}
