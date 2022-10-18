package com.example.finalwork;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class DialogImage extends Dialog {

    public ImageView picture;
    private Bitmap btm;
    private Window window = null;

    public DialogImage(Context context, int x, int y, Bitmap bitmap){
        super(context);
        windowDeploy(x,y);
        btm = bitmap;
    }

    public DialogImage(Context context){
        super(context);
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_window, null);
        picture = view.findViewById(R.id.dialog_window_image);
        picture.setImageBitmap(btm);

        setContentView(view);
    }

    //窗口显示
    public void windowDeploy(int x, int y){
        window = getWindow();
        window.getDecorView().setPadding(0,0,0,0);
        window.setWindowAnimations(R.style.windowAnimation); //窗口弹出动画

        WindowManager.LayoutParams wml = window.getAttributes();
        //x,y为窗口显示位置
        wml.x = x;
        wml.y = y;

        wml.width = WindowManager.LayoutParams.MATCH_PARENT;
        wml.height = WindowManager.LayoutParams.MATCH_PARENT;

        window.setAttributes(wml);
        window.getDecorView().setBackgroundColor(Color.BLACK);

    }

    @Override
    public void show(){
        super.show();

    }

    public void dismiss(){
        super.dismiss();
    }
}
