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
    FrameLayout root;
    PopupWindow startPopup;
    float dX, dY;

    @Override
    protected void onCreate(Bundle s){
        super.onCreate(s);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY|View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        root = new FrameLayout(this);
        // Bliss wallpaper color
        GradientDrawable bg = new GradientDrawable(GradientDrawable.Orientation.TL_BR, new int[]{Color.parseColor("#1E3C72"), Color.parseColor("#2A5298"), Color.parseColor("#3A7BD5")});
        root.setBackground(bg);

        // Desktop
        LinearLayout desk = new LinearLayout(this);
        desk.setOrientation(LinearLayout.VERTICAL);
        desk.setPadding(25,100,0,0);
        String[][] icons = {{"\uD83D\uDCBB","Computer"},{"\uD83D\uDCC1","Documents"},{"\uD83D\uDDD1\uFE0F","Recycle Bin"},{"\uD83C\uDF10","Browser"}};
        for(String[] ic: icons){
            LinearLayout ll = new LinearLayout(this); ll.setOrientation(LinearLayout.VERTICAL); ll.setGravity(Gravity.CENTER); ll.setPadding(20,25,20,25);
            TextView e = new TextView(this); e.setText(ic[0]); e.setTextSize(42);
            TextView t = new TextView(this); t.setText(ic[1]); t.setTextColor(Color.WHITE); t.setTextSize(11); t.setShadowLayer(4,1,1,Color.BLACK);
            ll.addView(e); ll.addView(t);
            ll.setOnClickListener(v->openWindow(ic[1]));
            desk.addView(ll);
        }
        root.addView(desk);

        // Taskbar
        LinearLayout taskbar = new LinearLayout(this);
        GradientDrawable tb = new GradientDrawable(); tb.setColor(Color.parseColor("#CC000000")); tb.setStroke(1, Color.parseColor("#44FFFFFF"));
        taskbar.setBackground(tb);
        FrameLayout.LayoutParams tp = new FrameLayout.LayoutParams(-1,115); tp.gravity=Gravity.BOTTOM;
        taskbar.setLayoutParams(tp); taskbar.setGravity(Gravity.CENTER_VERTICAL); taskbar.setPadding(10,5,10,5);

        Button orb = new Button(this); orb.setText("◉"); orb.setTextSize(30); orb.setTextColor(Color.WHITE);
        GradientDrawable ob = new GradientDrawable(); ob.setShape(GradientDrawable.OVAL); ob.setColors(new int[]{Color.parseColor("#6EC6FF"), Color.parseColor("#2196F3"), Color.parseColor("#0D47A1")}); ob.setStroke(2,Color.WHITE);
        orb.setBackground(ob); orb.setLayoutParams(new LinearLayout.LayoutParams(105,105));
        taskbar.addView(orb);

        TextView clock = new TextView(this); clock.setText(" "+java.text.DateFormat.getTimeInstance(java.text.DateFormat.SHORT).format(new Date())+" "); clock.setTextColor(Color.WHITE); clock.setPadding(20,0,20,0);
        LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(-2,-2); cp.leftMargin=450;
        clock.setLayoutParams(cp);
        taskbar.addView(clock);
        root.addView(taskbar);
        setContentView(root);

        orb.setOnClickListener(v->showStartMenu());
    }

    void showStartMenu(){
        if(startPopup!=null && startPopup.isShowing()){startPopup.dismiss(); return;}
        LinearLayout menu = new LinearLayout(this); menu.setOrientation(LinearLayout.HORIZONTAL);
        menu.setBackgroundColor(Color.parseColor("#F2F2F2"));

        LinearLayout left = new LinearLayout(this); left.setOrientation(LinearLayout.VERTICAL); left.setBackgroundColor(Color.WHITE); left.setPadding(10,10,10,10);
        left.setLayoutParams(new LinearLayout.LayoutParams(450,-1));
        PackageManager pm = getPackageManager();
        List<ApplicationInfo> apps = pm.getInstalledApplications(0);
        int count=0;
        for(ApplicationInfo a: apps){
            if(count>12) break;
            if(pm.getLaunchIntentForPackage(a.packageName)!=null){
                TextView tv = new TextView(this); tv.setText(" "+pm.getApplicationLabel(a)); tv.setPadding(20,20,20,20); tv.setTextColor(Color.BLACK);
                tv.setOnClickListener(vv->{try{startActivity(pm.getLaunchIntentForPackage(a.packageName));}catch(Exception e){}});
                left.addView(tv); count++;
            }
        }

        LinearLayout right = new LinearLayout(this); right.setOrientation(LinearLayout.VERTICAL); right.setBackgroundColor(Color.parseColor("#0F2D5A")); right.setPadding(20,20,20,20);
        right.setLayoutParams(new LinearLayout.LayoutParams(300,-1));
        String[] r = {"\uD83D\uDCBB Computer","\uD83D\uDCC4 Documents","\u2699 Control Panel","\uD83D\uDD34 Shutdown"};
        for(String it: r){ TextView tv = new TextView(this); tv.setText(it); tv.setTextColor(Color.WHITE); tv.setPadding(20,25,20,25); right.addView(tv); }

        menu.addView(left); menu.addView(right);
        startPopup = new PopupWindow(menu, 800, 900, true);
        startPopup.setBackgroundDrawable(new GradientDrawable());
        startPopup.showAtLocation(root, Gravity.BOTTOM|Gravity.LEFT, 10, 120);
    }

    void openWindow(String title){
        LinearLayout win = new LinearLayout(this);
        win.setOrientation(LinearLayout.VERTICAL);
        GradientDrawable bd = new GradientDrawable(); bd.setColor(Color.WHITE); bd.setStroke(4, Color.parseColor("#245EDC")); bd.setCornerRadius(10);
        win.setBackground(bd);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(850, 950); lp.leftMargin=120; lp.topMargin=120;
        win.setLayoutParams(lp); win.setElevation(25);

        LinearLayout bar = new LinearLayout(this); bar.setBackgroundColor(Color.parseColor("#245EDC")); bar.setPadding(15,15,15,15); bar.setGravity(Gravity.CENTER_VERTICAL);
        TextView tt = new TextView(this); tt.setText(title); tt.setTextColor(Color.WHITE); tt.setLayoutParams(new LinearLayout.LayoutParams(0,-2,1));
        Button cl = new Button(this); cl.setText("X"); cl.setTextColor(Color.WHITE); cl.setBackgroundColor(Color.RED);
        bar.addView(tt); bar.addView(cl);
        win.addView(bar);

        TextView cont = new TextView(this); cont.setText("\n 📁 Local Disk (C:)\n\n 📁 Local Disk (D:)\n\n 📁 Pictures\n\n 📁 Downloads\n\n\n 👉 Drag me from blue bar!");
        cont.setPadding(30,30,30,30); cont.setTextColor(Color.BLACK); cont.setTextSize(16);
        win.addView(cont);

        bar.setOnTouchListener((v,e)->{
            if(e.getAction()==MotionEvent.ACTION_DOWN){ dX=win.getX()-e.getRawX(); dY=win.getY()-e.getRawY(); }
            if(e.getAction()==MotionEvent.ACTION_MOVE){ win.animate().x(e.getRawX()+dX).y(e.getRawY()+dY).setDuration(0).start(); }
            return true;
        });
        cl.setOnClickListener(v->root.removeView(win));
        root.addView(win);
    }
}
