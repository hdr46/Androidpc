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
import java.util.Arrays;

public class MainActivity extends Activity {
    FrameLayout root;
    float dX, dY;
    LinearLayout rightList;
    LinearLayout currentWin;

    @Override
    protected void onCreate(Bundle s){
        super.onCreate(s);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        // Permission mangi le
        if(checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
        }

        root = new FrameLayout(this);
        root.setBackgroundColor(Color.parseColor("#0A82FF")); // Ekdam tara screenshot jevo blue

        // ===== DESKTOP ICONS =====
        LinearLayout desk = new LinearLayout(this);
        desk.setOrientation(LinearLayout.VERTICAL);
        desk.setPadding(20,30,0,0);
        
        desk.addView(makeIcon("🗑️","Recycle Bin", v->openWindow("Recycle Bin")));
        desk.addView(makeIcon("💻","Computer", v->openWindow("Computer")));
        
        root.addView(desk);

        // ===== TASKBAR - Niche hamesha dekhashe - FIX =====
        LinearLayout taskbar = new LinearLayout(this);
        taskbar.setOrientation(LinearLayout.HORIZONTAL);
        taskbar.setGravity(Gravity.CENTER_VERTICAL);
        taskbar.setBackgroundColor(Color.parseColor("#C0C0C0")); // Classic taskbar
        GradientDrawable tb = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{Color.parseColor("#245EDC"), Color.parseColor("#1941A5")});
        taskbar.setBackground(tb);
        FrameLayout.LayoutParams tp = new FrameLayout.LayoutParams(-1, 95);
        tp.gravity = Gravity.BOTTOM;
        taskbar.setLayoutParams(tp);
        
        Button start = new Button(this);
        start.setText("  ⊞ Start");
        start.setTextColor(Color.WHITE);
        start.setTextSize(14);
        start.setBackgroundColor(Color.parseColor("#0A8A0A"));
        start.setLayoutParams(new LinearLayout.LayoutParams(250, 85));
        taskbar.addView(start);
        
        root.addView(taskbar);
        setContentView(root);
    }

    LinearLayout makeIcon(String emoji, String name, View.OnClickListener clk){
        LinearLayout ll = new LinearLayout(this); ll.setOrientation(LinearLayout.VERTICAL); ll.setGravity(Gravity.CENTER); ll.setPadding(15,20,15,20);
        TextView e = new TextView(this); e.setText(emoji); e.setTextSize(40);
        TextView t = new TextView(this); t.setText(name); t.setTextColor(Color.WHITE); t.setTextSize(11); t.setShadowLayer(3,1,1,Color.BLACK); t.setGravity(Gravity.CENTER);
        ll.addView(e); ll.addView(t);
        ll.setOnClickListener(clk);
        return ll;
    }

    void openWindow(String title){
        if(currentWin!=null) root.removeView(currentWin);
        
        LinearLayout win = new LinearLayout(this);
        win.setOrientation(LinearLayout.VERTICAL);
        win.setBackgroundColor(Color.WHITE);
        GradientDrawable bd = new GradientDrawable(); bd.setColor(Color.WHITE); bd.setStroke(3, Color.parseColor("#0831D9")); bd.setCornerRadius(6);
        win.setBackground(bd);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(1000, 1200); lp.leftMargin=60; lp.topMargin=20;
        win.setLayoutParams(lp); win.setElevation(30);
        currentWin = win;

        // Title bar - MOTO X BUTTON
        LinearLayout bar = new LinearLayout(this); bar.setBackgroundColor(Color.parseColor("#0831D9")); bar.setGravity(Gravity.CENTER_VERTICAL); bar.setLayoutParams(new LinearLayout.LayoutParams(-1, 90)); bar.setPadding(10,0,0,0);
        TextView tt = new TextView(this); tt.setText(" "+title); tt.setTextColor(Color.WHITE); tt.setTextSize(15); tt.setLayoutParams(new LinearLayout.LayoutParams(0,-2,1));
        Button close = new Button(this); close.setText("X"); close.setTextSize(22); close.setTextColor(Color.WHITE); close.setBackgroundColor(Color.RED); close.setLayoutParams(new LinearLayout.LayoutParams(120, 85));
        bar.addView(tt); bar.addView(close);
        win.addView(bar);

        // Content
        if(title.equals("Recycle Bin")){
            TextView tv = new TextView(this); tv.setText("\n\n  Recycle Bin is Empty"); tv.setTextSize(16); tv.setTextColor(Color.BLACK); win.addView(tv);
        } else {
            LinearLayout split = new LinearLayout(this); split.setOrientation(LinearLayout.HORIZONTAL); split.setLayoutParams(new LinearLayout.LayoutParams(-1,-1));
            
            // Left - ACTIVE BADDHA
            LinearLayout left = new LinearLayout(this); left.setOrientation(LinearLayout.VERTICAL); left.setLayoutParams(new LinearLayout.LayoutParams(300,-1)); left.setBackgroundColor(Color.parseColor("#F5F5F5"));
            String[] leftItems = {"⭐ Quick Access","⬇️ Downloads","🖼️ Pictures","🎬 Videos","🎵 Music","📄 Documents"};
            for(String it: leftItems){
                TextView row = new TextView(this); row.setText(" "+it); row.setPadding(15,18,15,18); row.setTextColor(Color.BLACK); row.setTextSize(13);
                row.setOnClickListener(v->{
                    for(int i=0;i<left.getChildCount();i++) left.getChildAt(i).setBackgroundColor(Color.parseColor("#F5F5F5"));
                    row.setBackgroundColor(Color.parseColor("#CCE8FF"));
                    File f = Environment.getExternalStorageDirectory();
                    if(it.contains("Down")) f = new File(f+"/Download");
                    else if(it.contains("Pic")) f = new File(f+"/DCIM");
                    else if(it.contains("Vid")) f = new File(f+"/Movies");
                    else if(it.contains("Music")) f = new File(f+"/Music");
                    else if(it.contains("Doc")) f = new File(f+"/Documents");
                    loadFiles(f);
                });
                left.addView(row);
            }
            
            rightList = new LinearLayout(this); rightList.setOrientation(LinearLayout.VERTICAL); rightList.setBackgroundColor(Color.WHITE);
            ScrollView sv = new ScrollView(this); sv.addView(rightList); sv.setLayoutParams(new LinearLayout.LayoutParams(-1,-1));
            
            split.addView(left); split.addView(sv);
            win.addView(split);
            loadFiles(Environment.getExternalStorageDirectory());
        }

        bar.setOnTouchListener((v,e)->{
            if(e.getAction()==MotionEvent.ACTION_DOWN){ dX=win.getX()-e.getRawX(); dY=win.getY()-e.getRawY(); }
            if(e.getAction()==MotionEvent.ACTION_MOVE){ win.setX(e.getRawX()+dX); win.setY(e.getRawY()+dY); }
            return true;
        });
        close.setOnClickListener(v->root.removeView(win));
        root.addView(win);
    }

    void loadFiles(File folder){
        if(rightList==null) return;
        rightList.removeAllViews();
        File[] files = folder.listFiles();
        if(files==null){ rightList.addView(new TextView(this){{setText("  Empty"); setPadding(20,20,20,20);}}); return; }
        Arrays.sort(files,(a,b)->Boolean.compare(!a.isDirectory(),!b.isDirectory()));
        for(File f: files){
            if(f.getName().startsWith(".")) continue;
            LinearLayout r = new LinearLayout(this); r.setOrientation(LinearLayout.HORIZONTAL); r.setPadding(12,12,12,12);
            TextView ic = new TextView(this); ic.setText(f.isDirectory()?"📁 ":"📄 "); ic.setTextSize(16);
            TextView nm = new TextView(this); nm.setText(f.getName()); nm.setTextColor(Color.BLACK); nm.setTextSize(12);
            r.addView(ic); r.addView(nm);
            if(f.isDirectory()) r.setOnClickListener(v->loadFiles(f));
            rightList.addView(r);
        }
    }
}
