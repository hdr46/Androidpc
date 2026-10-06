package com.xp.launcher;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.*;
import android.widget.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainActivity extends Activity {
    FrameLayout root;
    float dX, dY;
    File currentPath = Environment.getExternalStorageDirectory();

    @Override
    protected void onCreate(Bundle s){
        super.onCreate(s);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Permission
        if(checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.READ_MEDIA_IMAGES}, 1);
        }

        root = new FrameLayout(this);
        root.setBackgroundColor(Color.parseColor("#1A8CFF")); // Win7 blue

        // Desktop Icons
        LinearLayout desk = new LinearLayout(this);
        desk.setOrientation(LinearLayout.VERTICAL);
        desk.setPadding(15,20,0,0);
        String[][] icons = {{"🗑️","Recycle Bin"},{"💻","Computer"}};
        for(String[] ic: icons){
            LinearLayout ll = new LinearLayout(this); ll.setOrientation(LinearLayout.VERTICAL); ll.setGravity(Gravity.CENTER); ll.setPadding(10,15,10,15);
            TextView e = new TextView(this); e.setText(ic[0]); e.setTextSize(40);
            TextView t = new TextView(this); t.setText(ic[1]); t.setTextColor(Color.WHITE); t.setShadowLayer(3,1,1,Color.BLACK); t.setTextSize(11);
            ll.addView(e); ll.addView(t);
            ll.setOnClickListener(v->openComputer());
            desk.addView(ll);
        }
        root.addView(desk);
        setContentView(root);
    }

    void openComputer(){
        // Main Window like your screenshot
        LinearLayout win = new LinearLayout(this);
        win.setOrientation(LinearLayout.VERTICAL);
        win.setBackgroundColor(Color.parseColor("#FFFFFF"));
        GradientDrawable bd = new GradientDrawable(); bd.setColor(Color.WHITE); bd.setStroke(2, Color.parseColor("#6D8DBE")); bd.setCornerRadius(8);
        win.setBackground(bd);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(1100, 1200); lp.leftMargin=60; lp.topMargin=40;
        win.setLayoutParams(lp); win.setElevation(20);

        // Title Bar - like screenshot
        LinearLayout titleBar = new LinearLayout(this);
        titleBar.setBackgroundColor(Color.parseColor("#D6E6F8"));
        titleBar.setPadding(10,5,5,5);
        titleBar.setGravity(Gravity.CENTER_VERTICAL);
        titleBar.setLayoutParams(new LinearLayout.LayoutParams(-1, 80));
        TextView title = new TextView(this); title.setText(" Computer"); title.setTextColor(Color.BLACK); title.setLayoutParams(new LinearLayout.LayoutParams(0,-2,1));
        Button close = new Button(this); close.setText("X"); close.setTextColor(Color.WHITE); close.setBackgroundColor(Color.parseColor("#E81123")); close.setLayoutParams(new LinearLayout.LayoutParams(110,70));
        titleBar.addView(title); titleBar.addView(close);
        win.addView(titleBar);

        // Address Bar
        LinearLayout addr = new LinearLayout(this); addr.setBackgroundColor(Color.parseColor("#F0F0F0")); addr.setPadding(10,10,10,10);
        TextView addrT = new TextView(this); addrT.setText("Computer > System (C:) > "); addrT.setTextColor(Color.BLACK); addrT.setTextSize(12);
        addr.addView(addrT);
        win.addView(addr);

        // Content Split - Left + Right like your photo
        LinearLayout split = new LinearLayout(this); split.setOrientation(LinearLayout.HORIZONTAL); split.setLayoutParams(new LinearLayout.LayoutParams(-1,-1));

        // LEFT SIDEBAR - Quick Access
        LinearLayout left = new LinearLayout(this); left.setOrientation(LinearLayout.VERTICAL); left.setBackgroundColor(Color.parseColor("#FFFFFF")); left.setPadding(10,10,10,10); left.setLayoutParams(new LinearLayout.LayoutParams(350,-1));
        String[] leftItems = {"⭐ Quick Access","📁 Desktop","⬇️ Downloads","📄 Documents","🎵 Music","🖼️ Pictures","🎬 Videos"};
        for(String it: leftItems){
            TextView tv = new TextView(this); tv.setText(it); tv.setPadding(20,18,20,18); tv.setTextColor(it.startsWith("⭐")?Color.parseColor("#003399"):Color.BLACK); tv.setTextSize(13);
            if(it.equals("🖼️ Pictures")){
                tv.setBackgroundColor(Color.parseColor("#CCE8FF"));
                tv.setOnClickListener(v->loadRealFiles(new File(Environment.getExternalStorageDirectory()+"/DCIM"), rightList));
            }
            if(it.equals("⬇️ Downloads")){
                tv.setOnClickListener(v->loadRealFiles(new File(Environment.getExternalStorageDirectory()+"/Download"), rightList));
            }
            left.addView(tv);
        }

        // RIGHT FILE LIST
        rightList = new LinearLayout(this); rightList.setOrientation(LinearLayout.VERTICAL); rightList.setBackgroundColor(Color.WHITE); rightList.setLayoutParams(new LinearLayout.LayoutParams(-1,-1)); rightList.setPadding(10,10,10,10);
        ScrollView sv = new ScrollView(this); sv.addView(rightList);

        split.addView(left); split.addView(sv);
        win.addView(split);

        // Initial Load - C: Drive folders
        loadRealFiles(new File("/storage/emulated/0"), rightList);

        // Drag
        titleBar.setOnTouchListener((v,e)->{
            if(e.getAction()==MotionEvent.ACTION_DOWN){ dX=win.getX()-e.getRawX(); dY=win.getY()-e.getRawY(); }
            if(e.getAction()==MotionEvent.ACTION_MOVE){ win.setX(e.getRawX()+dX); win.setY(e.getRawY()+dY); }
            return true;
        });
        close.setOnClickListener(v->root.removeView(win));
        root.addView(win);
    }

    LinearLayout rightList;
    void loadRealFiles(File folder, LinearLayout list){
        list.removeAllViews();
        if(!folder.exists()){ TextView tv=new TextView(this); tv.setText("This folder is empty"); tv.setPadding(30,30,30,30); list.addView(tv); return; }
        File[] files = folder.listFiles();
        if(files==null || files.length==0){ TextView tv=new TextView(this); tv.setText("This folder is empty"); tv.setPadding(30,30,30,30); list.addView(tv); return; }

        Arrays.sort(files, (a,b)->Boolean.compare(b.isDirectory(), a.isDirectory())); // folders first

        for(File f: files){
            if(f.getName().startsWith(".")) continue;
            LinearLayout row = new LinearLayout(this); row.setOrientation(LinearLayout.HORIZONTAL); row.setPadding(15,12,15,12); row.setGravity(Gravity.CENTER_VERTICAL);
            TextView icon = new TextView(this); icon.setText(f.isDirectory()?"📁":"📄"); icon.setTextSize(20); icon.setPadding(0,0,15,0);
            TextView name = new TextView(this); name.setText(f.getName()); name.setTextColor(Color.BLACK); name.setTextSize(13); name.setLayoutParams(new LinearLayout.LayoutParams(0,-2,1));
            row.addView(icon); row.addView(name);
            if(f.isDirectory()){
                row.setOnClickListener(v->loadRealFiles(f, list));
            } else {
                row.setOnClickListener(v->Toast.makeText(this,"Opening: "+f.getName(),Toast.LENGTH_SHORT).show());
            }
            list.addView(row);
            // divider
            View div = new View(this); div.setBackgroundColor(Color.parseColor("#EEEEEE")); div.setLayoutParams(new LinearLayout.LayoutParams(-1,2));
            list.addView(div);
        }
    }
}
