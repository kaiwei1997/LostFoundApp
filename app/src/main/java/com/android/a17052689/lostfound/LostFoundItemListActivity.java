package com.android.a17052689.lostfound;

import android.support.v4.app.Fragment;

public class LostFoundItemListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new LostFoundItemListFragment();
    }
}
