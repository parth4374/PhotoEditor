package photoeditor.parth.com.csc340;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import photoeditor.parth.com.photocollage.BuildConfig;
import photoeditor.parth.com.photocollage.R;

/**
 * Share class to share edited images to different platforms.
 *
 * @author parthpatel
 */

public class ShareActivity extends AppCompatActivity {

    //Textview for displaying the path of the image.
    TextView path;

    //URI for storing the image.
    URI myURI;
    Uri image;

    //ImageView for Facebook, WhatsApp and More.
    ImageView imageView, share_view, imagefb, imagews;
    private File file;

    /**
     * Main method for saving and sharing the edited images.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        //Linking the imageView and shareView to xml.
        imageView = findViewById(R.id.image_View2);
        share_view = findViewById(R.id.share_view1);

        //Getting saved image path from intent.
        final String image_path = getIntent().getStringExtra("imageresult");

        //Storing the image in Bitmap.
        final Bitmap bmp = BitmapFactory.decodeFile(image_path);

        //Getting file from saved image path.
        File f = new File(image_path);

        //Getting Uri for the saved file for sharing.
        image = FileProvider.getUriForFile(ShareActivity.this, BuildConfig.APPLICATION_ID + ".provider",f);

        //Setting Bitmap to imageview.
        imageView.setImageBitmap(bmp);

        //Try and Catch for URI exception throws.
        try {
            myURI = new URI(image_path);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        //Setting path to text path1.
        path = findViewById(R.id.textpath1);

        //Setting text of the image path.
        path.setText(image_path);

        //Linking the Facebook and WhatsApp buttons
        imagefb = findViewById(R.id.facebook1);
        imagews = findViewById(R.id.whatsaap1);

        //Listener for touch purposes and Sharing the image via more sources.
        share_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ////Creating a new intent and passing on the holding image through Extra Stream.
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.setType("image/*");
                sharingIntent.putExtra(Intent.EXTRA_STREAM, image);

                //Creating a chooser for sharing options.
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });


        //Listener for touch purposes and Sharing the image via Facebook.
        imagefb.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            public void onClick(View view) {

                //Creating a new intent and passing on the holding image through Extra Stream.
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.setType("image/*");
                share.putExtra(Intent.EXTRA_STREAM, image);

                //Setting the facebook application package for display on share screen.
                share.setPackage("com.facebook.katana");

                //Try and Catch for if Facebook application is not installed.
                try {
                    startActivity(Intent.createChooser(share, "Share Image"));
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(ShareActivity.this, "Facebook have not been installed.", 0).show();
                }
            }
        });


        //Listener for touch purposes and Sharing the image via Facebook.
        imagews.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            public void onClick(View view) {

                //Creating a new intent and passing on the holding image through Extra Stream.
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.setType("image/*");
                share.putExtra(Intent.EXTRA_STREAM, image);

                //Setting the WhatsApp application package for display on share screen.
                share.setPackage("com.whatsapp");

                //Try and Catch for if WhatsApp is not installed.
                try {
                    startActivity(Intent.createChooser(share, "Share Image"));
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(ShareActivity.this, "WhatsApp have not been installed.", 0).show();
                }
            }
        });
    }
}
