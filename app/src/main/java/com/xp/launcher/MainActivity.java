    void openWindow(String title){
        LinearLayout win = new LinearLayout(this);
        win.setOrientation(LinearLayout.VERTICAL);
        GradientDrawable bd = new GradientDrawable(); bd.setColor(Color.WHITE); bd.setStroke(3, Color.parseColor("#245EDC")); bd.setCornerRadius(12);
        win.setBackground(bd);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(850, 950); lp.leftMargin=120; lp.topMargin=120;
        win.setLayoutParams(lp); win.setElevation(25);

        // TITLE BAR - FIXED
        LinearLayout bar = new LinearLayout(this);
        bar.setBackgroundColor(Color.parseColor("#245EDC"));
        bar.setPadding(20,0,0);
        bar.setGravity(Gravity.CENTER_VERTICAL);
        bar.setLayoutParams(new LinearLayout.LayoutParams(-1, 110));

        TextView tt = new TextView(this); tt.setText(" "+title); tt.setTextColor(Color.WHITE); tt.setTextSize(18); tt.setLayoutParams(new LinearLayout.LayoutParams(0,-2,1));
        
        Button cl = new Button(this);
        cl.setText("✕");
        cl.setTextSize(22);
        cl.setTextColor(Color.WHITE);
        cl.setBackgroundColor(Color.parseColor("#E81123"));
        cl.setLayoutParams(new LinearLayout.LayoutParams(120, 110)); // MOTA BUTTON

        bar.addView(tt); bar.addView(cl);
        win.addView(bar);

        // CONTENT - ALAG ALAG
        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(30,30,30,30);
        
        TextView cont = new TextView(this);
        cont.setTextColor(Color.BLACK); cont.setTextSize(17);
        
        if(title.contains("Computer")){
            cont.setText("📁 Local Disk (C:)\n\n📁 Local Disk (D:)\n\n📁 USB Drive (E:)\n\n💾 Hard Disk");
        } else if(title.contains("Recycle")){
            cont.setText("🗑️ Recycle Bin is Empty\n\n\n(No deleted files)");
        } else if(title.contains("Docu")){
            cont.setText("📄 My Document.txt\n\n📄 Resume.pdf\n\n📁 Projects\n\n📁 Photos");
        } else {
            cont.setText("🌍 Google Chrome\n\n🔍 Search...\n\n(Internet Browser)");
        }
        content.addView(cont);
        win.addView(content);

        bar.setOnTouchListener((v,e)->{
            if(e.getAction()==MotionEvent.ACTION_DOWN){ dX=win.getX()-e.getRawX(); dY=win.getY()-e.getRawY(); }
            if(e.getAction()==MotionEvent.ACTION_MOVE){ win.animate().x(e.getRawX()+dX).y(e.getRawY()+dY).setDuration(0).start(); }
            return true;
        });
        cl.setOnClickListener(v->root.removeView(win));
        root.addView(win);
    }
