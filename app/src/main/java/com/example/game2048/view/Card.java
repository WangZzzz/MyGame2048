package com.example.game2048.view;

import android.content.Context;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by WangZ on 2015/8/14.
 */
public class Card extends FrameLayout {

    //卡片上的数字Text
    private TextView label;

    //数字大小
    private int num = 0;

    public Card(Context context) {
        super(context);

        label = new TextView(getContext());
        label.setTextSize(32);
        label.setBackgroundColor(0x33ffffff);
        label.setGravity(Gravity.CENTER);

        LayoutParams lp = new LayoutParams(-1, -1);
        lp.setMargins(10, 10, 0, 0);
        addView(label, lp);

        setNum(0);
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
        if(num <= 0){
            label.setBackgroundColor(0x33ffffff);
            label.setText("");
        }else{
            label.setBackgroundColor(0xffe6c99e);
//            label.setTextColor(Color.WHITE);
            label.setText(num + "");
        }
    }

    //判断两个卡片是否相同
    public boolean equals(Card c){
        return getNum() == c.getNum();
    }
}
