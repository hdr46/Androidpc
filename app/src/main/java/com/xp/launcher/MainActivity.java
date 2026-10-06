package com.xp.launcher;

import android.app.Activity;
import android.content.pm.*;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import java.util.*;

public class MainActivity extends Activity {
    PopupWindow startPopup;
    FrameLayout root;
    float dX, dY;

    @Override
    protected void onCreate(Bundle s){
        super.onCreate(s);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY|View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        root = new FrameLayout(this);
        GradientDrawable bg = new GradientDrawable(GradientDrawable.Orientation.TL_BR, new int[]{Color.parseColor("#1E3C72"), Color.parseColor("#2A5298"), Color.parseColor("#3A7BD5")});
        root.setBackground(bg);

        // Desktop Icons
        LinearLayout desk = new LinearLayout(this);
        desk.setOrientation(LinearLayout.VERTICAL);
        desk.setPadding(25,100,0,0);
        String[][] icons = {{"\uD83D\uDCBB","Computer"},{"\uD83D\uDDD1\uFE0F","Recycle Bin"},{"\uD83D\uDCC1","My Docs"}};
        for(String[] ic: icons){
            LinearLayout ll = new LinearLayout(this); ll.setOrientation(LinearLayout.VERTICAL); ll.setGravity(Gravity.CENTER); ll.setPadding(20,25,20,25);
            TextView e = new TextView(this); e.setText(ic[0]); e.setTextSize(40);
            TextView t = new TextView(this); t.setText(ic[1]); t.setTextColor(Color.WHITE); t.setShadowLayer(4,1,1,Color.BLACK);
            ll.addView(e); ll.addView(t);
            String title = ic[1];
            ll.setOnClickListener(v->openWindow(title));
            desk.addView(ll);
        }
        root.addView(desk);

        // Taskbar
        LinearLayout taskbar = new LinearLayout(this);
        taskbar.setBackgroundColor(Color.parseColor("#E6000000"));
        FrameLayout.LayoutParams tp = new FrameLayout.LayoutParams(-1,110); tp.gravity=Gravity.BOTTOM;
        taskbar.setLayoutParams(tp); taskbar.setGravity(Gravity.CENTER_VERTICAL); taskbar.setPadding(15,5,15,5);
        Button orb = new Button(this); orb.setText("◉"); orb.setTextSize(30); orb.setTextColor(Color.WHITE);
        GradientDrawable ob = new GradientDrawable(); ob.setShape(GradientDrawable.OVAL); ob.setColors(new int[]{Color.parseColor("#4FC3F7"), Color.parseColor("#01579B")}); ob.setStroke(3,Color.WHITE);
        orb.setBackground(ob); orb.setLayoutParams(new LinearLayout.LayoutParams(100,100));
        taskbar.addView(orb);
        root.addView(taskbar);
        setContentView(root);

        orb.setOnClickListener(v->Toast.makeText(this,"Start Menu - Next Update ma!",Toast.LENGTH_SHORT).show());
    }

    void openWindow(String title){
        // Window Container
        LinearLayout win = new LinearLayout(this);
        win.setOrientation(LinearLayout.VERTICAL);
        win.setBackgroundColor(Color.WHITE);
        GradientDrawable border = new GradientDrawable(); border.setColor(Color.WHITE); border.setStroke(4, Color.parseColor("#245EDC")); border.setCornerRadius(8);
        win.setBackground(border);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(800, 900);
        lp.leftMargin = 150; lp.topMargin = 150;
        win.setLayoutParams(lp);
        win.setElevation(20);

        // Title Bar - Draggable
        LinearLayout titleBar = new LinearLayout(this);
        titleBar.setOrientation(LinearLayout.HORIZONTAL);
        titleBar.setBackgroundColor(Color.parseColor("#245EDC"));
        titleBar.setPadding(15,15,15,15);
        titleBar.setGravity(Gravity.CENTER_VERTICAL);
        TextView titleT = new TextView(this); titleT.setText(title); titleT.setTextColor(Color.WHITE); titleT.setTextSize(14); titleT.setLayoutParams(new LinearLayout.LayoutParams(0,-2,1));
        Button close = new Button(this); close.setText("X"); close.setTextColor(Color.WHITE); close.setBackgroundColor(Color.RED);
        titleBar.addView(titleT); titleBar.addView(close);
        win.addView(titleBar);

        // Content
        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL); content.setPadding(20,20,20,20);
        TextView c1 = new TextView(this); c1.setText("📁 Local Disk (C:)\n📁 Local Disk (D:)\n📁 USB Drive (E:)\n\nDrag this window by title bar!");
        c1.setTextColor(Color.BLACK); c1.setTextSize(16);
        content.addView(c1);
        win.addView(content);

        // Drag Logic
        titleBar.setOnTouchListener((v, event)->{
            switch(event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    dX = win.getX() - event.getRawX();
                    dY = win.getY() - event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    win.animate().x(event.getRawX()+dX).y(event.getRawY()+dY).setDuration(0).start();
                    break;
            }
            return true;
        });

        close.setOnClickListener(v->root.removeView(win));
        root.addView(win);
    }
}
