package com.android.a17052689.lostfound;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class LostFoundItemListFragment extends Fragment {

    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

    private RecyclerView mItemRecyclerView;
    private ItemAdapter mAdapter;
    private boolean mSubtitleVisible;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lostfounditem_list, container, false);

        mItemRecyclerView = (RecyclerView) view
                .findViewById(R.id.item_recycler_view);
        mItemRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_item_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if (mSubtitleVisible) {
            subtitleItem.setTitle("Hide Subtitle");
        } else {
            subtitleItem.setTitle("Show Subtitle");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_item:
                LostFoundItem lostfounfitem = new LostFoundItem();
                LostFoundItemLab.get(getActivity()).addItem(lostfounfitem);
                Intent intent = LostFoundItemPagerActivity
                        .newIntent(getActivity(), lostfounfitem.getmId());
                startActivity(intent);
                return true;
            case R.id.show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateSubtitle() {
        LostFoundItemLab lostFoundItemLab = LostFoundItemLab.get(getActivity());
        int itemCount = lostFoundItemLab.getItems().size();
        String subtitle = getResources().getQuantityString(R.plurals.item_subtitle_plural, itemCount, itemCount);

        if (!mSubtitleVisible) {
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    private void updateUI() {
        LostFoundItemLab itemLab = LostFoundItemLab.get(getActivity());
        List<LostFoundItem> items = itemLab.getItems();

        if (mAdapter == null) {
            mAdapter = new ItemAdapter(items);
            mItemRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setItems(items);
            mAdapter.notifyDataSetChanged();
        }

        updateSubtitle();
    }

    private class LostFoundItemHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private LostFoundItem mItem;

        private TextView mTitleTextView;
        private TextView mDateTextView;
        private TextView mTimeTextView;
        private ImageView mSolvedImageView;

        public LostFoundItemHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_lostfound, parent, false));
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.item_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.lost_date);
            mTimeTextView = (TextView) itemView.findViewById(R.id.lost_time);
            mSolvedImageView = (ImageView) itemView.findViewById(R.id.item_found);
        }

        public void bind(LostFoundItem item) {
            mItem = item;
            mTitleTextView.setText(mItem.getmTitle());
            DateFormat df = new SimpleDateFormat("E, MMMM dd, YYYY");
            mDateTextView.setText(df.format(mItem.getmDate()));
            DateFormat tf = new SimpleDateFormat("hh:mm a");
            mTimeTextView.setText(tf.format(mItem.getmTime()));
            mSolvedImageView.setVisibility(item.ismFound() ? View.VISIBLE : View.GONE);
        }

        @Override
        public void onClick(View view) {
            Intent intent = LostFoundItemPagerActivity.newIntent(getActivity(), mItem.getmId());
            startActivity(intent);
        }
    }

    private class ItemAdapter extends RecyclerView.Adapter<LostFoundItemHolder> {

        private List<LostFoundItem> mItems;

        public ItemAdapter(List<LostFoundItem> items) {
            mItems = items;
        }

        @Override
        public LostFoundItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new LostFoundItemHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(LostFoundItemHolder holder, int position) {
            LostFoundItem item = mItems.get(position);
            holder.bind(item);
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        public void setItems(List<LostFoundItem> items) {
            mItems = items;
        }
    }
}
