package com.android.a17052689.lostfound;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

public class LostFoundItemPagerActivity extends AppCompatActivity {
    private static final String EXTRA_ITEM_ID =
            "com.android.a17052689.lostfound.item_id";

    private ViewPager mViewPager;
    private List<LostFoundItem> mItems;

    public static Intent newIntent(Context packageContext, UUID itemId) {
        Intent intent = new Intent(packageContext, LostFoundItemPagerActivity.class);
        intent.putExtra(EXTRA_ITEM_ID, itemId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_viewpager);

        UUID itemId = (UUID) getIntent()
                .getSerializableExtra(EXTRA_ITEM_ID);

        mViewPager = (ViewPager) findViewById(R.id.item_view_pager);

        mItems =LostFoundItemLab.get(this).getItems();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {

            @Override
            public Fragment getItem(int position) {
                LostFoundItem item = mItems.get(position);
                return LostFoundItemFragment.newInstance(item.getmId());
            }

            @Override
            public int getCount() {
                return mItems.size();
            }
        });

        for (int i = 0; i < mItems.size(); i++) {
            if (mItems.get(i).getmId().equals(itemId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
