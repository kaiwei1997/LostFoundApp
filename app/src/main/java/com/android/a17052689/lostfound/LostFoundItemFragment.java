package com.android.a17052689.lostfound;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.android.a17052689.lostfound.utils.PhotoUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class LostFoundItemFragment extends Fragment {

    private static final String ARG_ITEM_ID = "item_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_TIME = "DialogTime";

    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;
    private static final int SELECT_IMAGE_REQUEST = 2;
    private static final int TAKE_IMAGE_REQUEST = 3;

    private static final String TAKE_PHOTO = "Take Photo";
    private static final String CHOOSE_PHOTO = "Choose Photo";
    private static final String CANCEL = "Cancel";

    private LostFoundItem mItem;
    private EditText mTitleField;
    private Button mDateButton;
    private Button mTimeButton;
    private EditText mLocationField;
    private CheckBox mFoundCheckbox;
    private EditText mCommentField;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;

    public static LostFoundItemFragment newInstance(UUID itemId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_ITEM_ID, itemId);

        LostFoundItemFragment fragment = new LostFoundItemFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID itemId = (UUID) getArguments().getSerializable(ARG_ITEM_ID);
        mItem = LostFoundItemLab.get(getActivity()).getItem(itemId);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_item, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_item:
                LostFoundItemLab.get(getActivity()).removeItem(mItem);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lostfound_item, container, false);

        mTitleField = (EditText)v.findViewById(R.id.item_title);
        mTitleField.setText(mItem.getmTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mItem.setmTitle(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mDateButton = (Button) v.findViewById(R.id.lost_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment
                        .newInstance(mItem.getmDate());
                dialog.setTargetFragment(LostFoundItemFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        mTimeButton = (Button)v.findViewById(R.id.lost_time);
        updateTime();
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();
                TimePickerFragment dialog = TimePickerFragment
                        .newInstance(mItem.getmTime());
                dialog.setTargetFragment(LostFoundItemFragment.this, REQUEST_TIME);
                dialog.show(manager, DIALOG_TIME);
            }
        });

        mLocationField = (EditText)v.findViewById(R.id.lostLocation);
        mLocationField.setText(mItem.getmLocation());
        mLocationField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mItem.setmLocation(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mFoundCheckbox = (CheckBox) v.findViewById(R.id.item_found);
        mFoundCheckbox.setChecked(mItem.ismFound());
        mFoundCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                mItem.setmFound(isChecked);
            }
        });

        mCommentField = (EditText) v.findViewById(R.id.comment);
        mCommentField.setText(mItem.getmComment());
        mCommentField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mItem.setmComment(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mPhotoView = (ImageView) v.findViewById(R.id.item_photo);
        byte[] data = mItem.getmItemPhoto();
        if(data != null){
            Bitmap bitmap = PhotoUtils.getImage(data);
            mPhotoView.setImageBitmap(bitmap);
        }

        mPhotoButton = (ImageButton) v.findViewById(R.id.btn_photo);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] items = {TAKE_PHOTO, CHOOSE_PHOTO, CANCEL};
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Item Photo");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(items[i].equals(TAKE_PHOTO)){
                            takePhoto();
                        }else if(items[i].equals(CHOOSE_PHOTO)){
                            selectImage();
                        }else if(items[i].equals(CANCEL)){
                            dialogInterface.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });


        return v;
    }


    private void updateDate(){
        DateFormat df = new SimpleDateFormat("E, MMMM dd, YYYY");
        mDateButton.setText(df.format(mItem.getmDate()));
    }

    private void updateTime(){
        DateFormat tf = new SimpleDateFormat("hh:mm a");
        mTimeButton.setText(tf.format(mItem.getmTime()));
    }

    private void selectImage(){
        try{
            if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},SELECT_IMAGE_REQUEST);
            }else{
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select item Photo"),SELECT_IMAGE_REQUEST);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void takePhoto(){
        try{
            if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA},TAKE_IMAGE_REQUEST);
            }else{
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(Intent.createChooser(intent, "Take item Photo"),TAKE_IMAGE_REQUEST);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        LostFoundItemLab.get(getActivity())
                .updateItem(mItem);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mItem.setmDate(date);
            updateDate();
        }else if(requestCode == REQUEST_TIME){
            Date time = (Date) data
                    .getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            mItem.setmTime(time);
            updateTime();
        }else if(requestCode == TAKE_IMAGE_REQUEST){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            mPhotoView.setImageBitmap(bitmap);
            mItem.setmItemPhoto(PhotoUtils.getBytes(bitmap));
        }else if(requestCode == SELECT_IMAGE_REQUEST){
            Uri selectedImage = data.getData();
            mPhotoView.setImageURI(selectedImage);
            Bitmap bitmap = ((BitmapDrawable) mPhotoView.getDrawable()).getBitmap();
            mItem.setmItemPhoto(PhotoUtils.getBytes(bitmap));
        }
    }
}
