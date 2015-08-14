package com.example.game2048.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.game2048.R;


public class MainActivity extends Activity {

    private TextView tvScore;
    private static MainActivity mainActivity = null;
    private int score = 0;

    public MainActivity(){
        mainActivity = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        tvScore = (TextView)findViewById(R.id.tvScore);
    }

    public static MainActivity getMainActivity(){
        return mainActivity;
    }

    public void clearScore(){
        score = 0;
        showScore();
    }

    public void showScore(){
        tvScore.setText(score + "");
    }

    public void addScore(int s){
        score = score + s;
        showScore();
    }
}
