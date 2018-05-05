package com.eagle.locker.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.eagle.locker.R;
import com.eagle.locker.util.DimenUtils;
/**
 * @Copyright (c) 下午5:13 miaopai
 * @Description
 * @author sundou sundu@yixia.com
 * @date 2018/5/5
 * @version version_code
 */
public class LockScreenView extends RelativeLayout {

    private static final String TAG = "MainActivity";

    private ImageView homeImageView;         //home
    private ImageView leftImageView;         //左边去赚钱的imageView
    private ImageView rightImageView;        //右边解锁的imageView

    private int width;                       //屏幕宽度
    private int mx, my;
    private int top;
    private int bottom;
    private int left;
    private int right;
    private int leftImageView_left;
    private int rightImageView_right;
    private boolean left_flag = false;
    private boolean right_flag = false;
    private int leftImageView_right;
    private int rightImageView_left;
    private Context mContext;

    private LockViewClickListener lockViewClickListener;

    //home键按下的坐标
    private float homeDownX, homeDownY;
    //home键抬起的坐标
    private float homeUpX, homeUpY;

    public interface LockViewClickListener{
        public void clickRight();
        public void clickLeft();
        public void clickHome();
    }

    public void setOnLockViewClickListener(LockViewClickListener lockViewClickListener){
        this.lockViewClickListener = lockViewClickListener;
    }

    public LockScreenView(Context context) {
        super(context);
        mContext = context;
        width = DimenUtils.getScreenWidth(context);
        LayoutInflater.from(context).inflate(R.layout.layout_lock_view, this);
        initViews();
        addListener();
    }

    public LockScreenView(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        mContext = context;
        width = DimenUtils.getScreenWidth(context);
        LayoutInflater.from(context).inflate(R.layout.layout_lock_view, this);
        initViews();
        addListener();
    }

    private void addListener() {
        homeImageView.setOnTouchListener(new OnTouchListener() {
            private int height;
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        homeDownX = event.getRawX();
                        homeDownY = event.getRawY();
                        homeImageView.setImageResource(R.drawable.tongqian_pressed);
                        leftImageView.setImageResource(R.drawable.xiazaizhong);
                        rightImageView.setImageResource(R.drawable.yaoshizhong);
                        leftImageView_left = leftImageView.getLeft();
                        leftImageView_right = leftImageView.getRight();
                        rightImageView_right = rightImageView.getRight();
                        rightImageView_left = rightImageView.getLeft();

                        height = (int) (event.getRawY() - 50);

                        left = v.getLeft();
                        right = v.getRight();
                        top = v.getTop();
                        bottom = v.getBottom();
                        break;
                    case MotionEvent.ACTION_MOVE:

                        mx = (int) (event.getRawX());
                        my = (int) (event.getRawY() - 50);

                        if (mx < width / 2) {
                            if (mx < leftImageView_right) {
                                v.layout(leftImageView_left, top, leftImageView_right, bottom);
                                left_flag = true;
                            } else {
                                v.layout(mx - homeImageView.getWidth() / 2, top, mx + homeImageView.getWidth() / 2, bottom);
                                left_flag = false;
                            }

                        } else if (mx > width / 2) {
                            if ((mx + homeImageView.getWidth() / 2) < rightImageView_right) {
                                v.layout(mx - homeImageView.getWidth() / 2, top, mx + homeImageView.getWidth() / 2, bottom);
                            }

                            if (mx > rightImageView_left) {
                                v.layout(rightImageView_left, top, rightImageView_right, bottom);
                                right_flag = true;
                            } else {
                                v.layout(mx - homeImageView.getWidth() / 2, top, mx + homeImageView.getWidth() / 2, bottom);
                                right_flag = false;
                            }
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                        homeImageView.setImageResource(R.drawable.home);
                        homeUpX = event.getRawX();
                        homeUpY = event.getRawY();
                        if (Math.abs(homeUpX - homeDownX) > 3){
                            //拖拽
                            leftImageView.setImageResource(R.drawable.xiazai);
                            rightImageView.setImageResource(R.drawable.yaoshi);
                            if (right_flag) {
                                lockViewClickListener.clickRight();
                                rightImageView.setImageResource(R.drawable.yaoshiok);
                            } else if (left_flag) {
                                lockViewClickListener.clickLeft();
                                leftImageView.setImageResource(R.drawable.xiazaiok);
                            }
                            right_flag = false;
                            left_flag = false;
                            v.layout(left, top, right, bottom);
                        }else {
                            //点击
                            lockViewClickListener.clickHome();
                        }
                        break;
                }
                return true;
            }

        });
    }

    private void initViews() {
        leftImageView = (ImageView) findViewById(R.id.image_lock_view_left);
        rightImageView = (ImageView) findViewById(R.id.image_lock_view_right);
        homeImageView = (ImageView) findViewById(R.id.image_lock_view_drag);
    }

}
