package com.newera.oneplus5tnavigationbartweaks.provider;

import android.content.res.Resources;
import android.content.res.TypedArray;

import com.newera.oneplus5tnavigationbartweaks.R;

public class StyleProvider {
    private final TypedArray home, back, search, down, recent, title;

    public StyleProvider(Resources resources) {
        home = resources.obtainTypedArray(R.array.a_tn_home);
        back = resources.obtainTypedArray(R.array.a_tn_back);
        search = resources.obtainTypedArray(R.array.a_tn_search);
        down = resources.obtainTypedArray(R.array.a_tn_down);
        recent = resources.obtainTypedArray(R.array.a_tn_recent);
        title = resources.obtainTypedArray(R.array.themes_nav_array);
    }

    public void release(){
        if(home != null)
            home.recycle();
        if(back != null)
            back.recycle();
        if(search != null)
            search.recycle();
        if(down != null)
            down.recycle();
        if(recent != null)
            recent.recycle();
        if(title != null)
            title.recycle();

    }

    public Icons getIconByIndex(int index) {
        return new Icons(
                home.getResourceId(index, -1),
                back.getResourceId(index, -1),
                search.getResourceId(index, -1),
                down.getResourceId(index, -1),
                recent.getResourceId(index, -1),
                title.getResourceId(index, -1)
        );
    }

    public int getMax(){
        return home.length();
    }

    public class Icons {
        public final int home, back, search, down, recent, title;

        public Icons(int home, int back, int search, int down, int recent, int title) {
            this.home = home;
            this.back = back;
            this.search = search;
            this.down = down;
            this.recent = recent;
            this.title = title;
        }


    }
}