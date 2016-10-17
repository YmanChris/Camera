package com.example.yman.camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private Button camera;
    private Button gallary;
    private ImageView show;

    public static final int TAKE_PHOTO = 1;
    public static final int CROP_PHOTO = 2;

    private Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }
    public void initView(){
        camera = (Button) findViewById(R.id.camera);
        gallary = (Button) findViewById(R.id.gallary);
        show = (ImageView) findViewById(R.id.show);
        camera.setOnClickListener(vn);
        gallary.setOnClickListener(vn);
    }
    private View.OnClickListener vn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.camera:
                    File outputImage = new File(Environment.getExternalStorageDirectory(),"tempImage.jpg");
                    try {
                        if(outputImage.exists())
                            outputImage.delete();
                        outputImage.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    imageUri = Uri.fromFile(outputImage);
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                    startActivityForResult(intent,TAKE_PHOTO);
                    Log.e("!!!!!!!!","@@@@@@@@@@@@");
                    break;
                case R.id.gallary:
                    outputImage = new File(Environment.getExternalStorageDirectory(),"output_image.jpg");
                    try {
                        if(outputImage.exists())
                            outputImage.delete();
                        outputImage.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    imageUri = Uri.fromFile(outputImage);
                    intent = new Intent("android.intent.action.GET_CONTENT");
                    intent.setType("image/*");
                    intent.putExtra("crop",true);
                    intent.putExtra("scale",true);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                    startActivityForResult(intent,CROP_PHOTO);
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode)
        {
            case TAKE_PHOTO:
                if(resultCode == RESULT_OK){
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(imageUri,"image/*");
                    intent.putExtra("scale",true);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                    startActivityForResult(intent,CROP_PHOTO);
                }
                break;
            case CROP_PHOTO:
                Log.e("!!!!!!!!!!!","!!!!!!!!!!");
                if(resultCode == RESULT_OK){
                    if(data.getData() != null)
                        imageUri = data.getData();
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        show.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }
}
