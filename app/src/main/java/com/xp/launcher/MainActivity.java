package com.xp.launcher;
import android.app.Activity; import android.content.*; import android.content.pm.*;
import android.os.*; import android.view.*; import android.widget.*;
import java.util.*; import java.text.SimpleDateFormat;
public class MainActivity extends Activity {
    boolean isStartOpen=false; List<ResolveInfo> allApps; List<String> allNames; Handler h=new Handler();
    protected void onCreate(Bundle b){
        super.onCreate(b);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.activity_main);
        PackageManager pm=getPackageManager();
        Intent main=new Intent(Intent.ACTION_MAIN,null); main.addCategory(Intent.CATEGORY_LAUNCHER);
        allApps=pm.queryIntentActivities(main,0); allNames=new ArrayList<>();
        for(ResolveInfo r:allApps) allNames.add(r.loadLabel(pm).toString());
        GridView grid=findViewById(R.id.grid);
        grid.setAdapter(new BaseAdapter(){
            public int getCount(){return Math.min(6,allApps.size());}
            public Object getItem(int i){return null;} public long getItemId(int i){return 0;}
            public View getView(int i,View v,ViewGroup p){
                if(v==null) v=getLayoutInflater().inflate(R.layout.item_app,p,false);
                ((TextView)v.findViewById(R.id.name)).setText(allNames.get(i));
                ((ImageView)v.findViewById(R.id.icon)).setImageDrawable(allApps.get(i).loadIcon(pm)); return v;
            }
        });
        grid.setOnItemClickListener((a,v,i,l)->{try{startActivity(pm.getLaunchIntentForPackage(allApps.get(i).activityInfo.packageName));}catch(Exception e){}});
        ListView startList=findViewById(R.id.start_list); View startMenu=findViewById(R.id.start_menu);
        startList.setAdapter(new BaseAdapter(){
            public int getCount(){return allApps.size();} public Object getItem(int i){return null;} public long getItemId(int i){return 0;}
            public View getView(int i,View v,ViewGroup p){if(v==null)v=getLayoutInflater().inflate(android.R.layout.simple_list_item_1,p,false); TextView tv=(TextView)v; tv.setText(allNames.get(i)); tv.setPadding(20,25,20,25); return v;}
        });
        startList.setOnItemClickListener((a,v,i,l)->{try{startActivity(pm.getLaunchIntentForPackage(allApps.get(i).activityInfo.packageName));}catch(Exception e){} startMenu.setVisibility(View.GONE); isStartOpen=false;});
        TextView btnStart=findViewById(R.id.btn_start);
        btnStart.setOnClickListener(v->{if(isStartOpen){startMenu.setVisibility(View.GONE); isStartOpen=false;}else{startMenu.setVisibility(View.VISIBLE); isStartOpen=true;}});
        TextView clock=findViewById(R.id.clock); Runnable r2=new Runnable(){public void run(){clock.setText(new SimpleDateFormat("hh:mm a").format(new Date())); h.postDelayed(this,1000);}}; h.post(r2);
        TextView battery=findViewById(R.id.battery); registerReceiver(new BroadcastReceiver(){public void onReceive(Context c,Intent i){int lvl=i.getIntExtra(BatteryManager.EXTRA_LEVEL,0); battery.setText(lvl+"% ");}},new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }
    public void onBackPressed(){View m=findViewById(R.id.start_menu); if(isStartOpen){m.setVisibility(View.GONE); isStartOpen=false;}}
}
