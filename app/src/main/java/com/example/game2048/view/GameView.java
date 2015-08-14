package com.example.game2048.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;

import com.example.game2048.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WangZ on 2015/8/14.
 *  游戏的主要布局类
 */
public class GameView extends GridLayout {

    //记录16张卡片的数组
    private Card[][] cardsMap = new Card[4][4];
    //空点
    private List<Point> emptyPoints = new ArrayList<Point>();

    public GameView(Context context) {
        super(context);
        initGameView();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGameView();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initGameView();
    }

    /**
     * 初始化方法，不管是用哪种构造方法，都会执行这个方法
     */
    private void initGameView(){
        Log.i("GameView", "initGameView");

        setColumnCount(4);
        setBackgroundColor(0xffbbada0);

        //判断用户的操作和意图
        //记录下用户按下的位置和离开的位置，就可以知道用户的意图
        setOnTouchListener(new OnTouchListener() {

            private float startX, startY, offsetX, offsetY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                Log.i("GameView", "onTouch");
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
//                        Log.i("GameView", "MotionEvent.ACTION_DOWN");
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        offsetX = event.getX() - startX;
                        offsetY = event.getY() - startY;

                        if(Math.abs(offsetX) > Math.abs(offsetY)){
                            //如果x轴方向上的差距大于y轴方向上的差距，说明是在水平方向上移动
                            //再判断向左还是向右
                            if(offsetX < -5){
                                swipeLeft();
                            }else if(offsetX > 5){
                                swipeRight();
                            }
                        }else{
                            //垂直方向上移动
                            //判断是向上还是向下
                            if(offsetY < -5){
                                swipeUp();
                            }else if(offsetY > 5){
                                swipeDown();
                            }
                        }
                        break;
                    default:
                        break;
                }
                //必须返回true，如果返回false，只会侦听到touchdown的事件，而touchup,touchmove等事件监听不到
                return true;
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        //计算每张卡片的宽高
        /*
        两边留出10像素，并且一排有4个卡片
         */
        int cardWidth = (Math.min(w, h) - 10) / 4;
        addCards(cardWidth, cardWidth);

        startGame();
    }

    private void addCards(int cardWidth, int cardHeight){
        //4列4行
        Card c;
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                c = new Card(getContext());
                c.setNum(0);
                addView(c, cardWidth, cardHeight);

                cardsMap[j][i] = c;
            }
        }
    }

    private void startGame(){
        MainActivity.getMainActivity().clearScore();
        for(int y = 0; y < 4; y++){
            for(int x = 0; x < 4; x++){
                cardsMap[x][y].setNum(0);
            }
        }

        addRandomNum();
        addRandomNum();
    }

    //初始化，随机选择两个卡片，添加数字
    private void addRandomNum(){

        emptyPoints.clear();

        for(int y = 0; y < 4; y++){
            for(int x = 0; x < 4; x++){
                if(cardsMap[x][y].getNum() <= 0){
                    //则为空点
                    emptyPoints.add(new Point(x, y));
                }
            }
        }

        Point p = emptyPoints.remove((int)(Math.random() * emptyPoints.size()));
        cardsMap[p.x][p.y].setNum(Math.random() > 0.1 ? 2 : 4);
    }

    //向左滑动的方法
    private void swipeLeft(){

        boolean merge = false;//默认不添加新的项
//        Toast.makeText(getContext(), "向左", Toast.LENGTH_SHORT).show();
        for(int y = 0; y < 4; y++){
            for(int x = 0; x < 4; x++){
                for(int x1 = x + 1; x1 < 4; x1++){
                    if(cardsMap[x1][y].getNum() > 0){
                        if(cardsMap[x][y].getNum() <= 0){
                            cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
                            cardsMap[x1][y].setNum(0);
                            merge = true;
                            x--;
                        }else if(cardsMap[x][y].equals(cardsMap[x1][y])){
                            merge = true;
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x1][y].setNum(0);
                            MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                        }
                        break;
                    }
                }
            }
        }

        if(merge){
            addRandomNum();
            checkComplete();
        }
    }


    //向右滑动的方法
    private void swipeRight(){

        boolean merge = false;//默认不添加新的项
//        Toast.makeText(getContext(), "向右", Toast.LENGTH_SHORT).show();
        for(int y = 0; y < 4; y++){
            for(int x = 3; x >= 0; x--){
                for(int x1 = x - 1; x1 >= 0; x1--){
                    if(cardsMap[x1][y].getNum() > 0){
                        if(cardsMap[x][y].getNum() <= 0){
                            cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
                            cardsMap[x1][y].setNum(0);
                            merge = true;
                            x++;
                        }else if(cardsMap[x][y].equals(cardsMap[x1][y])){
                            merge = true;
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x1][y].setNum(0);

                            MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                        }
                        break;
                    }
                }
            }
        }
        if(merge){
            addRandomNum();
            checkComplete();
        }
    }


    //向上滑动的方法
    private void swipeUp(){
        boolean merge = false;//默认不添加新的项
//        Toast.makeText(getContext(), "向上", Toast.LENGTH_SHORT).show();
        for(int x = 0; x < 4; x++){
            for(int y = 0; y < 4; y++){
                for(int y1 = y + 1; y1 < 4; y1++){
                    if(cardsMap[x][y1].getNum() > 0){
                        if(cardsMap[x][y].getNum() <= 0){
                            cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
                            cardsMap[x][y1].setNum(0);
                            merge = true;
                            y--;
                        }else if(cardsMap[x][y].equals(cardsMap[x][y1])){
                            merge = true;
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x][y1].setNum(0);

                            MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                        }
                        break;
                    }
                }
            }
        }
        if(merge){
            addRandomNum();
            checkComplete();
        }
    }


    //向下滑动的方法
    private void swipeDown(){
        boolean merge = false;//默认不添加新的项
//        Toast.makeText(getContext(), "向下", Toast.LENGTH_SHORT).show();
        for(int x = 0; x < 4; x++){
            for(int y = 3; y >= 0; y--){
                for(int y1 = y - 1; y1 >= 0; y1--){
                    if(cardsMap[x][y1].getNum() > 0){
                        if(cardsMap[x][y].getNum() <= 0){
                            cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
                            cardsMap[x][y1].setNum(0);
                            merge = true;
                            y++;
                        }else if(cardsMap[x][y].equals(cardsMap[x][y1])){
                            merge = true;
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x][y1].setNum(0);

                            MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                        }
                        break;
                    }
                }
            }
        }
        if(merge){
            addRandomNum();
            checkComplete();
        }
    }

    private void checkComplete(){
        boolean gameover = true;
        boolean succeed = false;
        ALL:
        for(int y = 0; y < 4; y++){
            for(int x = 0; x < 4; x++){
                if(cardsMap[x][y].getNum() >= 2048){
                    succeed = true;
                    break ALL;
                }else if(cardsMap[x][y].getNum() == 0 ||
                        (x > 0 && cardsMap[x][y].equals(cardsMap[x - 1][y])) ||
                        (x < 3 && cardsMap[x][y].equals(cardsMap[x + 1][y])) ||
                        (y > 0 && cardsMap[x][y].equals(cardsMap[x][y - 1])) ||
                        (y < 3 && cardsMap[x][y].equals(cardsMap[x][y + 1]))) {
                    gameover = false;
                    break ALL;
                }
            }
        }

        if(gameover){
            gameover = true;
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setCancelable(false);
            builder.setTitle("您好").setMessage("游戏结束").setPositiveButton("重新开始", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startGame();
                }
            });
            builder.show();
        }

        if(succeed){
            succeed = true;
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setCancelable(false);
            builder.setTitle("恭喜您").setMessage("游戏胜利").setPositiveButton("重新开始", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startGame();
                }
            });
            builder.show();
        }
    }
}
