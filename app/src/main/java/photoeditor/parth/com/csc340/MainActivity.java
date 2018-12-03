package photoeditor.parth.com.csc340;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

import photoeditor.parth.com.csc340.Extra.Containers;
import photoeditor.parth.com.photocollage.R;

/**
 * This is the initial class that maps the buttons on the Starting Screen (Main Activity Page).
 *
 * @author parthpatel
 */

public class MainActivity extends AppCompatActivity {

    //Creating 3 ImageViews for the 3 buttons.
    ImageView gallery, camera, saved;

    //A bitmap to store the image.
    private Bitmap bmp;

    //Differential static integers.
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int CAMERA_REQUEST = 1888;

    /**
     * A permission method to get access for Read/Write Storage, Internet and Camera on the phone.
     * @param savedInstanceState
     */
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Linking the activity_main.xml to the class.
        setContentView(R.layout.activity_main);

        //Asking permission for any Android higher than lollipop.
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.INTERNET, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

        if (!hasPermissions(MainActivity.this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS, PERMISSION_ALL);
        }

        //Mapping buttons through findViewById.
        camera = findViewById(R.id.camerabtn);
        gallery = findViewById(R.id.gallerybtn);
        saved = findViewById(R.id.saved);

        //A listener that logs the camera button touch on the phone.
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);

            }
        });

        //A listener that logs the gallery button touch on the phone.
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i, "Select Picture"), RESULT_LOAD_IMAGE);
            }
        });

        //A listener that logs the saved images button touch on the phone.
        saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SavedImage.class);
                startActivity(intent);
            }
        });

    }

    /**
     * Permission checking method.
     * @param context
     * @param permissions
     * @return true, false
     */
    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Listener operation method that performs the tasks when appropriate buttons are pressed.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @SuppressLint("WrongConstant")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //Creating a new  Bitmap Factory.
        BitmapFactory.Options options = new BitmapFactory.Options();

        //RGB 565 for usability with opaque bitmap.
        options.inPreferredConfig = Bitmap.Config.RGB_565;

        //Creating a new Intent.
        Intent i;

        //If, to see if the image has been loaded.
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK) {
            try {
                bmp = BitmapFactory.decodeStream(getContentResolver().openInputStream(data.getData()), null, options);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if (bmp == null) {
                Toast.makeText(getApplicationContext(), "Image could not be loaded..", 1).show();
                return;
            }

            //Passing the intent to the next screen.
            i = new Intent(this, Photocollage2.class);

            //Creating a new ByteArrayOutputStream for decoding and storing the image in the Bitmap.
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            //Compressing and reconstructing the bitmap as a .PNG image.
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
            Bitmap decoded2 = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

            //Storing the decoded bitmap in startBmp Bitmap.
            Containers.startBmp = decoded2;
            startActivity(i);
        }

        //If, for when camera is requested.
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

            //Creating a new bitmap for camera clicked picture.
            Bitmap camerabitmap = (Bitmap) data.getExtras().get("data");

            //Creating a new ByteArrayOutputStream for decoding and storing the image in the Bitmap.
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            //Compressing and reconstructing the bitmap as a .PNG image.
            camerabitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

            //Creating a new intent for camera clicked picture and storing it to the startBmp bitmap.
            Intent intent = new Intent(MainActivity.this, Photocollage2.class);
            Containers.startBmp = decoded;
            startActivity(intent);
        }
    }
}
