package com.xp.launcher;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.*;
import android.webkit.MimeTypeMap;
import android.widget.*;
import java.io.File;
import java.util.Arrays;

public class MainActivity extends Activity {
    FrameLayout root;
    LinearLayout taskbar;
    LinearLayout startMenu;
    float dX, dY;
    LinearLayout currentWin;

    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.READ_MEDIA_IMAGES, android.Manifest.permission.READ_MEDIA_VIDEO, android.Manifest.permission.READ_MEDIA_AUDIO}, 1);
        }

        root = new FrameLayout(this);
        root.setBackgroundColor(Color.parseColor("#0A82FF"));
        root.setBackgroundResource(android.R.drawable.screen_background_dark); // wallpaper jevu

        // ===== DESKTOP ICONS - MINI COMPUTER =====
        LinearLayout desk = new LinearLayout(this);
        desk.setOrientation(LinearLayout.VERTICAL);
        desk.setPadding(15, 25, 0, 0);

        desk.addView(makeDeskIcon("💻", "Computer", () -> openFileExplorer("Computer", Environment.getExternalStorageDirectory())));
        desk.addView(makeDeskIcon("🗑️", "Recycle Bin", () -> openFileExplorer("Recycle Bin", null)));
        desk.addView(makeDeskIcon("📁", "My Documents", () -> openFileExplorer("Documents", new File(Environment.getExternalStorageDirectory()+"/Documents"))));
        desk.addView(makeDeskIcon("🖼️", "My Pictures", () -> openFileExplorer("Pictures", new File(Environment.getExternalStorageDirectory()+"/DCIM"))));
        desk.addView(makeDeskIcon("🎵", "My Music", () -> openFileExplorer("Music", new File(Environment.getExternalStorageDirectory()+"/Music"))));

        root.addView(desk);

        // ===== TASKBAR =====
        taskbar = new LinearLayout(this);
        taskbar.setOrientation(LinearLayout.HORIZONTAL);
        taskbar.setGravity(Gravity.CENTER_VERTICAL);
        GradientDrawable tb = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{Color.parseColor("#3C81F5"), Color.parseColor("#1A4FC3")});
        taskbar.setBackground(tb);
        FrameLayout.LayoutParams tp = new FrameLayout.LayoutParams(-1, 100);
        tp.gravity = Gravity.BOTTOM;
        taskbar.setLayoutParams(tp);

        Button start = new Button(this);
        start.setText("⊞ Start");
        start.setTextColor(Color.WHITE);
        start.setTextSize(15);
        GradientDrawable sb = new GradientDrawable(); sb.setShape(GradientDrawable.OVAL); sb.setColor(Color.parseColor("#1B5E20")); sb.setStroke(2, Color.WHITE);
        start.setBackground(sb);
        start.setLayoutParams(new LinearLayout.LayoutParams(240, 88));
        start.setOnClickListener(v -> toggleStartMenu());
        taskbar.addView(start);

        TextView clock = new TextView(this);
        clock.setText(" 3:47 PM "); clock.setTextColor(Color.WHITE);
        LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(-2, -2); cp.leftMargin = 500;
        clock.setLayoutParams(cp);
        taskbar.addView(clock);

        // ===== START MENU =====
        startMenu = new LinearLayout(this);
        startMenu.setOrientation(LinearLayout.VERTICAL);
        startMenu.setBackgroundColor(Color.WHITE);
        GradientDrawable smb = new GradientDrawable(); smb.setColor(Color.WHITE); smb.setStroke(2, Color.parseColor("#1A4FC3")); smb.setCornerRadius(8);
        startMenu.setBackground(smb);
        FrameLayout.LayoutParams smp = new FrameLayout.LayoutParams(550, 800);
        smp.gravity = Gravity.BOTTOM | Gravity.LEFT; smp.bottomMargin = 100; smp.leftMargin = 5;
        startMenu.setLayoutParams(smp);
        startMenu.setVisibility(View.GONE);
        startMenu.setElevation(50);

        String[] programs = {"💻 Computer", "📁 Documents", "🖼️ Pictures", "🎬 Videos", "🎵 Music", "⬇️ Downloads", "📄 All PDF Files", "⚙️ Settings"};
        for (String p : programs) {
            TextView tv = new TextView(this); tv.setText(" " + p); tv.setPadding(20, 22, 20, 22); tv.setTextColor(Color.BLACK); tv.setTextSize(14);
            tv.setOnClickListener(v -> {
                startMenu.setVisibility(View.GONE);
                if (p.contains("Computer")) openFileExplorer("Computer", Environment.getExternalStorageDirectory());
                else if (p.contains("Doc")) openFileExplorer("Documents", new File(Environment.getExternalStorageDirectory()+"/Documents"));
                else if (p.contains("Pic")) openFileExplorer("Pictures", new File(Environment.getExternalStorageDirectory()+"/DCIM"));
                else if (p.contains("Videos")) openFileExplorer("Videos", new File(Environment.getExternalStorageDirectory()+"/Movies"));
                else if (p.contains("Music")) openFileExplorer("Music", new File(Environment.getExternalStorageDirectory()+"/Music"));
                else if (p.contains("Downloads")) openFileExplorer("Downloads", new File(Environment.getExternalStorageDirectory()+"/Download"));
                else if (p.contains("PDF")) openFileExplorer("All PDF", Environment.getExternalStorageDirectory());
            });
            startMenu.addView(tv);
        }

        root.addView(startMenu);
        root.addView(taskbar);
        setContentView(root);
    }

    LinearLayout makeDeskIcon(String emoji, String name, Runnable action) {
        LinearLayout ll = new LinearLayout(this); ll.setOrientation(LinearLayout.VERTICAL); ll.setGravity(Gravity.CENTER); ll.setPadding(10, 15, 10, 15);
        ll.setLayoutParams(new LinearLayout.LayoutParams(220, 220));
        TextView e = new TextView(this); e.setText(emoji); e.setTextSize(42); e.setGravity(Gravity.CENTER);
        TextView t = new TextView(this); t.setText(name); t.setTextColor(Color.WHITE); t.setTextSize(11); t.setGravity(Gravity.CENTER); t.setShadowLayer(4, 1, 1, Color.BLACK);
        ll.addView(e); ll.addView(t);
        ll.setOnClickListener(v -> action.run());
        return ll;
    }

    void toggleStartMenu() {
        if (startMenu.getVisibility() == View.VISIBLE) startMenu.setVisibility(View.GONE);
        else { startMenu.setVisibility(View.VISIBLE); startMenu.bringToFront(); taskbar.bringToFront(); }
    }

    void openFileExplorer(String title, File startFolder) {
        if (currentWin!= null) root.removeView(currentWin);
        startMenu.setVisibility(View.GONE);

        LinearLayout win = new LinearLayout(this);
        win.setOrientation(LinearLayout.VERTICAL);
        win.setBackgroundColor(Color.WHITE);
        GradientDrawable bd = new GradientDrawable(); bd.setColor(Color.WHITE); bd.setStroke(3, Color.parseColor("#0831D9")); bd.setCornerRadius(8);
        win.setBackground(bd);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(1050, 1350); lp.leftMargin = 20; lp.topMargin = 15;
        win.setLayoutParams(lp); win.setElevation(40);
        currentWin = win;

        LinearLayout bar = new LinearLayout(this); bar.setBackgroundColor(Color.parseColor("#0831D9")); bar.setGravity(Gravity.CENTER_VERTICAL); bar.setLayoutParams(new LinearLayout.LayoutParams(-1, 85)); bar.setPadding(10, 0, 0, 0);
        TextView tt = new TextView(this); tt.setText(" " + title); tt.setTextColor(Color.WHITE); tt.setTextSize(14); tt.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1));
        Button close = new Button(this); close.setText("X"); close.setTextColor(Color.WHITE); close.setBackgroundColor(Color.RED); close.setLayoutParams(new LinearLayout.LayoutParams(110, 80));
        bar.addView(tt); bar.addView(close);
        win.addView(bar);

        LinearLayout split = new LinearLayout(this); split.setOrientation(LinearLayout.HORIZONTAL); split.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));

        LinearLayout left = new LinearLayout(this); left.setOrientation(LinearLayout.VERTICAL); left.setLayoutParams(new LinearLayout.LayoutParams(300, -1)); left.setBackgroundColor(Color.parseColor("#F0F8FF"));
        LinearLayout rightList = new LinearLayout(this); rightList.setOrientation(LinearLayout.VERTICAL);

        ScrollView svRight = new ScrollView(this); svRight.addView(rightList);
        split.addView(left); split.addView(svRight);
        win.addView(split);

        // Left Menu Click = Right ma real file dekhase
        String[][] leftMenu = {{"⭐ All Files", ""}, {"🖼️ My Pictures", "/DCIM"}, {"📄 My PDFs", "/Documents"}, {"🎵 My Music", "/Music"}, {"🎬 My Videos", "/Movies"}, {"⬇️ Downloads", "/Download"}};
        for (String[] item : leftMenu) {
            TextView tv = new TextView(this); tv.setText(item[0]); tv.setPadding(15, 18, 15, 18); tv.setTextColor(Color.BLACK); tv.setTextSize(13);
            tv.setOnClickListener(v -> {
                for (int i = 0; i < left.getChildCount(); i++) left.getChildAt(i).setBackgroundColor(Color.parseColor("#F0F8FF"));
                tv.setBackgroundColor(Color.parseColor("#CCE8FF"));
                File f = item[1].isEmpty()? Environment.getExternalStorageDirectory() : new File(Environment.getExternalStorageDirectory() + item[1]);
                if (item[0].contains("PDF")) loadFiles(f, rightList, true);
                else loadFiles(f, rightList, false);
            });
            left.addView(tv);
        }

        if (title.equals("Recycle Bin")) {
            TextView tv = new TextView(this); tv.setText("\n\n Recycle Bin is Empty"); tv.setTextSize(15); rightList.addView(tv);
        } else {
            loadFiles(startFolder!= null? startFolder : Environment.getExternalStorageDirectory(), rightList, title.contains("PDF"));
        }

        bar.setOnTouchListener((v, e) -> {
            if (e.getAction() == MotionEvent.ACTION_DOWN) { dX = win.getX() - e.getRawX(); dY = win.getY() - e.getRawY(); win.bringToFront(); taskbar.bringToFront(); }
            if (e.getAction() == MotionEvent.ACTION_MOVE) { win.setX(e.getRawX() + dX); win.setY(e.getRawY() + dY); }
            return true;
        });
        close.setOnClickListener(v -> root.removeView(win));
        root.addView(win);
        taskbar.bringToFront();
    }

    void loadFiles(File folder, LinearLayout list, boolean pdfOnly) {
        list.removeAllViews();
        if (folder == null ||!folder.exists()) { TextView tv = new TextView(this); tv.setText(" Folder not found"); list.addView(tv); return; }
        File[] files = folder.listFiles();
        if (files == null || files.length == 0) { TextView tv = new TextView(this); tv.setText(" This folder is empty"); tv.setPadding(20,20,20,20); list.addView(tv); return; }
        Arrays.sort(files, (a,b) -> Boolean.compare(!a.isDirectory(),!b.isDirectory()));

        for (File f : files) {
            if (f.getName().startsWith(".")) continue;
            if (pdfOnly &&!f.isDirectory() &&!f.getName().toLowerCase().endsWith(".pdf")) continue;

            LinearLayout row = new LinearLayout(this); row.setOrientation(LinearLayout.HORIZONTAL); row.setPadding(12, 14, 12, 14); row.setGravity(Gravity.CENTER_VERTICAL);
            String icon = "📄";
            if (f.isDirectory()) icon = "📁";
            else if (f.getName().endsWith(".jpg") || f.getName().endsWith(".png")) icon = "🖼️";
            else if (f.getName().endsWith(".mp4") || f.getName().endsWith(".mkv")) icon = "🎬";
            else if (f.getName().endsWith(".mp3")) icon = "🎵";
            else if (f.getName().endsWith(".pdf")) icon = "📕";

            TextView ic = new TextView(this); ic.setText(icon + " "); ic.setTextSize(16);
            TextView nm = new TextView(this); nm.setText(f.getName()); nm.setTextColor(Color.BLACK); nm.setTextSize(12); nm.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1));
            row.addView(ic); row.addView(nm);

            if (f.isDirectory()) row.setOnClickListener(v -> loadFiles(f, list, pdfOnly));
            else row.setOnClickListener(v -> openRealFile(f));

            list.addView(row);
            View div = new View(this); div.setBackgroundColor(Color.parseColor("#EEEEEE")); div.setLayoutParams(new LinearLayout.LayoutParams(-1, 1));
            list.addView(div);
        }
    }

    void openRealFile(File f) {
        try {
            String ext = MimeTypeMap.getFileExtensionFromUrl(f.getName());
            String mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setDataAndType(Uri.fromFile(f), mime);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        } catch (Exception e) {
            Toast.makeText(this, f.getName() + " - File: " + f.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        }
    }
}
