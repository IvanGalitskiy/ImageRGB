package com.example.note.imagergb;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.Date;

/**
 * Created by NOTE on 17.09.2016.
 */
public class PlaceholderFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private Uri outputFileUri = null;
    ImageView image;
    static SectionsPagerAdapter mAdapter = null;
    public PlaceholderFragment() {
    }


    public static PlaceholderFragment createFragment(SectionsPagerAdapter adapter) {
        mAdapter = adapter;
        return new PlaceholderFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        image = (ImageView) rootView.findViewById(R.id.image_chooice);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choiceFile();
            }
        });
        return rootView;
    }

    @Override
    public void onActivityResult(final int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                try{
                    outputFileUri = resultData.getData();
                    Glide.with(PlaceholderFragment.this).load(outputFileUri).asBitmap()
                            .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            RGBFragment rgbFragment;
                            CollectFragment collectFragment;
                            if (mAdapter.getList().size()<=1){
                                collectFragment = new CollectFragment();
                                rgbFragment =RGBFragment.createFragment(mAdapter);
                                mAdapter.addFragmnet(rgbFragment);
                                mAdapter.addFragmnet(collectFragment);
                            } else {
                                rgbFragment = (RGBFragment) mAdapter.getFragment(1);
                            }
                            rgbFragment.splitToChannel(resource);
                            image.setImageBitmap(resource);
                        }
                    });
                } catch (OutOfMemoryError ex){
                    ex.printStackTrace();
                }

            }
            if (requestCode == 2) {
                outputFileUri = null;
            }
        }
    }

    public void choiceFile() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "StartTodo image" + new Date().getTime());
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image from Camera");
        outputFileUri = getActivity().getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        Intent chooserIntent = Intent.createChooser(i, "Image Chooser");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[]{captureIntent});
        startActivityForResult(chooserIntent, 1);
    }


}
