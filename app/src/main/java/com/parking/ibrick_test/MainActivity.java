package com.parking.ibrick_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    ImageButton imgbtn;
    EditText etname,etcity,etmail,etisAdmin;
    Button btnsubmit;
    Uri imguri;
    TextView tvpathimg;
    String imagepath ="";
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        tvpathimg = findViewById(R.id.tvpath);
        imgbtn = findViewById(R.id.imageButton);
        etname = findViewById(R.id.editTextTextPersonName);
        etmail = findViewById(R.id.editTextTextPersonName2);
        etcity = findViewById(R.id.editTextTextPersonName3);
        etisAdmin = findViewById(R.id.editTextTextPersonName4);
        btnsubmit = findViewById(R.id.button);


        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Intent cameraIntent = new Intent(Intent.ACTION_GET_CONTENT);
//                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,)
                cameraIntent.setType("image/*");
                startActivityForResult(cameraIntent,101);
            }//
        });

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                JsonObject json = new JsonObject();
//                json.addProperty("email","abdulsalamshaikh101@gmail.com");
//                Toast.makeText(context, "click", Toast.LENGTH_SHORT).show();
//                Ion.with(context)
//                        .load("use this link here")
//                        .asJsonArray()
//                        .setCallback(new FutureCallback<JsonArray>() {
//                            @Override
//                            public void onCompleted(Exception e, JsonArray result) {
////                                tvpathimg.setText( result.getAsString()+"h");
//                                Log.i("cback", "onCompleted: "+result);
//                                Toast.makeText(context, "ye : " +  result, Toast.LENGTH_LONG).show();
//                            }
//                        });

                try {
//                    ParcelFileDescriptor prd =
//                            context.getContentResolver().openFileDescriptor(imguri,"r",null);
//                    InputStream inputStream = new  FileInputStream(prd.getFileDescriptor());
//                    File file = new File(context.getCacheDir(),imagepath);
//                    OutputStream ops = new FileOutputStream(file);
//
//                    IOUtils.copy(inputStream,ops);

//                    File path  = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//                    File file = new File(path,imagepath);
//                    path.mkdirs();
////                    File img =File.createTempFile(imagepath,".jpg",path);



                    Ion.with(context)
                            .load("POST","http://vps3.inspeero.com:3007/api/setprofile")
//                            .setHeader("Content-Type","multipart/form-data")
                            .setMultipartParameter("email", "abdulsalamshaikh101@gmail.com")
                            .setMultipartParameter("isAdmin", "false")
                            .setMultipartParameter("fullname", etname.getText().toString())
                            .setMultipartParameter("city", "azamgarh")
                            .setMultipartFile("imageuri", "image/jpeg", new File(imagepath))
                            .asString()
                            .setCallback(new FutureCallback<String>() {
                                @Override
                                public void onCompleted(Exception e, String result) {

                                    try {

                                        tvpathimg.setText( result);
                                        Toast.makeText(context, "" +  result, Toast.LENGTH_LONG).show();
                                        //do stuff with result

                                    }catch (Exception e1)
                                    {
                                        tvpathimg.setText(e.getMessage()+" =="+e1.getMessage());
                                        Toast.makeText(context, "" +  e1.getMessage()+" and "+e.getMessage(), Toast.LENGTH_LONG).show();
                                    }

                                }
                            });


                } catch (Exception e) {
                    e.printStackTrace();
                    tvpathimg.setText(e.getMessage());
                    Toast.makeText(context, "file cache wala error " +  e.getMessage(), Toast.LENGTH_LONG).show();

                }//

            }//
        });

    }//






    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 101 && resultCode == RESULT_OK) {
//            Bitmap photo = (Bitmap) data.getExtras().get("data");
//            imgbtn.setImageBitmap(photo);
//            knop.setVisibility(Button.VISIBLE);


            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
//            Uri tempUri = getImageUri(getApplicationContext(), photo);
            Uri tempUri = data.getData();
            imguri = tempUri;
            // CALL THIS METHOD TO GET THE ACTUAL PATH
            String filenamepath = getRealPathFromURI(tempUri);
            imagepath = filenamepath;
            tvpathimg.setText(imagepath);
            Toast.makeText(this.getApplicationContext(), "" + filenamepath, Toast.LENGTH_LONG).show();
//            File finalFile = new File(filenamepath);

            Log.i("fname", "onActivityResult: imgefilepath" + filenamepath);
//            System.out.println(filename);

        }
    }



    public  Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri)
        {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }





}//
