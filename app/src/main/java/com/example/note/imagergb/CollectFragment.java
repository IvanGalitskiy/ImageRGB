package com.example.note.imagergb;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CollectFragment extends Fragment {
    private ImageView imageView;
    private FloatingActionButton fab;
    private Bitmap bitmap;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_collect, container, false);
        imageView = (ImageView) v.findViewById(R.id.fragment_collect_image);
        fab = (FloatingActionButton) v.findViewById(R.id.fragment_collect_save);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Title");
                final EditText input = new EditText(getContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String filename = input.getText().toString();
                        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                                "/Notes";
                        try {
                            File dir = new File(file_path);
                            if (!dir.exists())
                                dir.mkdirs();
                            File file = new File(dir, filename + ".png");
                            FileOutputStream fOut = new FileOutputStream(file);

                            bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
                            fOut.flush();

                            fOut.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void setNewImage(int R[], int[] G, int[] B, int width, int height) {
        int[] colors = new int[R.length];
        for (int i = 0; i < R.length; i++) {
            colors[i] = Color.rgb(R[i], G[i], B[i]);
        }
        bitmap = Bitmap.createBitmap(colors, 0, width, width, height, Bitmap.Config.RGB_565);
    }

    public void display() {
        imageView.setImageBitmap(bitmap);
    }
}
