package com.example.note.imagergb;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.appyvet.rangebar.RangeBar;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by NOTE on 17.09.2016.
 */
public class RGBFragment extends Fragment implements RangeBar.OnRangeBarChangeListener {
    ImageView imageR;
    ImageView imageG;
    ImageView imageB;
    RangeBar rangeBarR;
    RangeBar rangeBarG;
    RangeBar rangeBarB;
    int minR, minG, minB, maxR, maxG, maxB;
    int[] rValues, gValues, bValues;
    int[] newR, newG, newB;
    int width, height;
    private static SectionsPagerAdapter mAdapter;

    public static RGBFragment createFragment(SectionsPagerAdapter adapter) {
        mAdapter = adapter;
        return new RGBFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.rgb_layout, container, false);
        imageR = (ImageView) v.findViewById(R.id.image_r);
        imageG = (ImageView) v.findViewById(R.id.image_g);
        imageB = (ImageView) v.findViewById(R.id.image_b);
        rangeBarR = (RangeBar) v.findViewById(R.id.rangebarR);
        rangeBarR.setOnRangeBarChangeListener(this);
        rangeBarG = (RangeBar) v.findViewById(R.id.rangebarG);
        rangeBarG.setOnRangeBarChangeListener(this);
        rangeBarB = (RangeBar) v.findViewById(R.id.rangebarB);
        rangeBarB.setOnRangeBarChangeListener(this);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void splitToChannel(Bitmap bitmap) {
        int[] pixR, pixG, pixB;
        width = bitmap.getWidth();
        height = bitmap.getHeight();
        int[] pix = new int[width * height];
        bitmap.getPixels(pix, 0, width, 0, 0, width, height);
        pixR = new int[width * height];
        pixG = new int[width * height];
        pixB = new int[width * height];
        rValues = new int[width * height];
        gValues = new int[width * height];
        bValues = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int index = y * width + x;
                int R = (pix[index] & 0xff0000) >> 16;
                int G = (pix[index] & 0xff00) >> 8;
                int B = pix[index] & 0xff;
                rValues[index] = R;
                gValues[index] = G;
                bValues[index] = B;
                pixR[index] = Color.rgb(R, 0, 0);
                pixG[index] = Color.rgb(0, G, 0);
                pixB[index] = Color.rgb(0, 0, B);
            }
        }
        getDiapason(rValues, gValues, bValues);
        displayChannel(bitmap, pixR, pixG, pixB);
        displayNewImage();
    }

    public void getDiapason(int[] r, int[] g, int b[]) {
        List list = Arrays.asList(ArrayUtils.toObject(r));
        minR = (int) Collections.min(list);
        maxR = (int) Collections.max(list);
        rangeBarR.setRangePinsByIndices(minR, maxR);
        list = Arrays.asList(ArrayUtils.toObject(g));
        minG = (int) Collections.min(list);
        maxG = (int) Collections.max(list);
        rangeBarG.setRangePinsByIndices(minG, maxG);
        list = Arrays.asList(ArrayUtils.toObject(b));
        minB = (int) Collections.min(list);
        maxB = (int) Collections.max(list);
        rangeBarB.setRangePinsByIndices(minB, maxB);
    }

    public void displayChannel(Bitmap source, int[] rBmp, int[] gBmp, int[] bBmp) {
        if (rBmp != null && gBmp != null && bBmp != null) {
            Bitmap mutableBmp = Bitmap.createBitmap(rBmp, width, height, Bitmap.Config.RGB_565);
            imageR.setImageBitmap(mutableBmp);
            mutableBmp = Bitmap.createBitmap(gBmp, width, height, Bitmap.Config.RGB_565);
            imageG.setImageBitmap(mutableBmp);
            mutableBmp = Bitmap.createBitmap(bBmp, width, height, Bitmap.Config.RGB_565);
            imageB.setImageBitmap(mutableBmp);
        }
    }

    public void displayChannel(int[] p, ImageView view) {
        if (width != 0 && height != 0) {
            int[] pix = new int[width * height];
            switch (view.getId()) {
                case R.id.image_r:
                    for (int i = 0; i < p.length; i++) {
                        pix[i] = Color.rgb(p[i], 0, 0);
                    }
                    break;
                case R.id.image_g:
                    for (int i = 0; i < p.length; i++) {
                        pix[i] = Color.rgb(0, p[i], 0);
                    }
                    break;
                case R.id.image_b:
                    for (int i = 0; i < p.length; i++) {
                        pix[i] = Color.rgb(0, 0, p[i]);
                    }
                    break;
            }
            Bitmap mutableBmp = Bitmap.createBitmap(pix, width, height, Bitmap.Config.RGB_565);
            view.setImageBitmap(mutableBmp);
        }
    }

    public int[] changeDiapason(int[] p, int left, int right, int min, int max) {
        int incL = left - min;
        int incR = right - max;
        min = left;
        max = right;
        int inc = incL + incR;
        int[] tR = p.clone();
        for (int i = 0; i < p.length; i++) {
            tR[i] = p[i] + inc;
            if (tR[i] > max) {
                tR[i] = max;
            } else if (tR[i] < min) {
                tR[i] = min;
            }
        }
        return tR;
    }

    @Override
    public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
        switch (rangeBar.getId()) {
            case R.id.rangebarR:
                newR = changeDiapason(rValues, leftPinIndex, rightPinIndex, minR, maxR);
                displayChannel(newR, imageR);
                break;
            case R.id.rangebarG:
                newG = changeDiapason(gValues, leftPinIndex, rightPinIndex, minG, maxG);
                displayChannel(newG, imageG);
                break;
            case R.id.rangebarB:
                newB = changeDiapason(bValues, leftPinIndex, rightPinIndex, minB, maxB);
                displayChannel(newB, imageB);
                break;
        }
        displayNewImage();
    }
    public void displayNewImage(){
        CollectFragment fragment =  (CollectFragment) mAdapter.getFragment(2);
        if (width!=0 && height!=0) {
            if (newR==null) {
                newR = rValues;
            }
            if (newG==null){
                newG = gValues;
            }
            if(newB==null) {
                newB = bValues;
            }
            fragment.setNewImage(newR, newG, newB, width, height);
        }
    }
}
