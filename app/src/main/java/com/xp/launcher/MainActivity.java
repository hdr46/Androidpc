package com.xp.launcher;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Main Layout
        FrameLayout root = new FrameLayout(this);
        root.setBackgroundColor(Color.parseColor("#3A6EA5")); // XP Blue
        
        // Desktop Grid
        GridView grid = new GridView(this);
        grid.setNumColumns(4);
        grid.setVerticalSpacing(20);
        grid.setHorizontalSpacing(10);
        grid.setPadding(20,20,20,100);
        grid.setGravity(Gravity.CENTER);

        PackageManager pm = getPackageManager();
        List<ApplicationInfo> apps = pm.getInstalledApplications(0);
        ArrayList<ApplicationInfo> userApps = new ArrayList<>();
        for(ApplicationInfo a: apps) if(pm.getLaunchIntentForPackage(a.packageName)!=null) userApps.add(a);

        grid.setAdapter(new BaseAdapter() {
            public int getCount(){return userApps.size();}
            public Object getItem(int p){return userApps.get(p);}
            public long getItemId(int p){return p;}
            public View getView(int pos, View v, ViewGroup parent){
                LinearLayout ll = new LinearLayout(MainActivity.this);
                ll.setOrientation(LinearLayout.VERTICAL);
                ll.setGravity(Gravity.CENTER);
                ll.setPadding(10,10,10,10);
                try{
                    ImageView iv = new ImageView(MainActivity.this);
                    iv.setImageDrawable(pm.getApplicationIcon(userApps.get(pos)));
                    iv.setLayoutParams(new LinearLayout.LayoutParams(96,96));
                    TextView tv = new TextView(MainActivity.this);
                    tv.setText(pm.getApplicationLabel(userApps.get(pos)));
                    tv.setTextColor(Color.WHITE);
                    tv.setTextSize(11);
                    tv.setGravity(Gravity.CENTER);
                    tv.setShadowLayer(2,1,1,Color.BLACK);
                    ll.addView(iv);
                    ll.addView(tv);
                }catch(Exception e){}
                return ll;
            }
        });
        grid.setOnItemClickListener((p,v,pos,id)->{
            try{startActivity(pm.getLaunchIntentForPackage(userApps.get(pos).packageName));}catch(Exception e){}
        });
        root.addView(grid);

        // XP Taskbar
        LinearLayout taskbar = new LinearLayout(this);
        taskbar.setOrientation(LinearLayout.HORIZONTAL);
        taskbar.setBackgroundColor(Color.parseColor("#245EDC"));
        taskbar.setPadding(5,5,5,5);
        taskbar.setGravity(Gravity.CENTER_VERTICAL);
        FrameLayout.LayoutParams tp = new FrameLayout.LayoutParams(-1,110);
        tp.gravity = Gravity.BOTTOM;
        taskbar.setLayoutParams(tp);

        // Start Button
        Button start = new Button(this);
        start.setText("start");
        start.setTextColor(Color.WHITE);
        start.setTextSize(14);
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(Color.parseColor("#3BA33B"));
        gd.setCornerRadius(15);
        gd.setStroke(2, Color.parseColor("#1F5A1F"));
        start.setBackground(gd);
        LinearLayout.LayoutParams sp = new LinearLayout.LayoutParams(180,90);
        sp.setMargins(5,0,10,0);
        start.setLayoutParams(sp);
        start.setOnClickListener(v->Toast.makeText(this,"Welcome to Windows XP!",Toast.LENGTH_SHORT).show());
        taskbar.addView(start);

        // Clock
        TextView clock = new TextView(this);
        clock.setText(" 2:41 PM ");
        clock.setTextColor(Color.WHITE);
        clock.setBackgroundColor(Color.parseColor("#0F4ECF"));
        clock.setPadding(20,10,20,10);
        LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(-2,-2);
        cp.gravity = Gravity.END;
        taskbar.addView(clock);

        root.addView(taskbar);
        setContentView(root);
    }
}
