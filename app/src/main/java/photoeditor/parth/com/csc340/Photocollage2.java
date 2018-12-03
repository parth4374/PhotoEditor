package photoeditor.parth.com.csc340;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.net.Uri;
import android.os.Environment;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Random;

import photoeditor.parth.com.csc340.Extra.Containers;
import photoeditor.parth.com.csc340.Extra.HorizontalAdapter;
import photoeditor.parth.com.csc340.OnTouch.MultiTouchListener;
import photoeditor.parth.com.photocollage.R;

import static android.view.View.GONE;

/**
 *Photo Collage class that handles the editing functions for the application.
 *
 * @author parthpatel
 */

public class Photocollage2 extends AppCompatActivity {

    //Defining ImageView variables.
    ImageView firstimage, back_img, savebtn;

    //Defining RecyclerViews for Blur and Brightness adjustments.
    RecyclerView recyclerView, back_recyclerview;

    //Horizontal adapter to store Frames and Backgrounds.
    HorizontalAdapter FrameHorizontalAdapter, BackgroundHorizontalAdapter;

    //Loading Frames in the array.
    int android_image_urls[] = {R.drawable.frame1, R.drawable.frame2, R.drawable.frame3, R.drawable.frame4, R.drawable.frame5,
            R.drawable.frame6, R.drawable.frame7};

    //TextView for editing purposes.
    TextView Blurtext, brightnesstext;

    //Loading Backgrounds in the array.
    int Backgrounds[] = {R.drawable.bg_1, R.drawable.bg_2,
            R.drawable.bg_3, R.drawable.bg_4,R.drawable.bg_5,
            R.drawable.bg_6,R.drawable.bg_7,
            R.drawable.bg_9, R.drawable.bg_10,
            R.drawable.bg_11, R.drawable.bg_12,
            R.drawable.bg_13, R.drawable.bg_14,
            R.drawable.bg_15, R.drawable.bg_16,
            R.drawable.bg_17, R.drawable.bg_18,
            R.drawable.bg_19, R.drawable.bg_20};

    //An ImageView for pattern.
    ImageView pattern;

    //Static final int for load image result.
    private static final int RESULT_LOAD_IMAGE = 1;

    //Defining a layout ofr savepg.
    RelativeLayout savepg;

    //File variables.
    File fname, file;

    //Integer for brightness count.
    private int bright = 100;

    //Slider for Brightness and Blur.
    SeekBar brightness, Blurseek;

    //More ImageViews for buttons.
    ImageView framebtn, brightnessbtn, backgroundbtn, gallerybtn;

    //Click verification boolean.
    Boolean click = true;

    //Bitmap to store the image.
    Bitmap bmp;

    //Loading Indicator variable.
    private AVLoadingIndicatorView avi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Linking the activity_photocollage2.xml to the program.
        setContentView(R.layout.activity_photocollage2);

        //Mapping buttons.
        firstimage = findViewById(R.id.first_img);
        Blurtext = findViewById(R.id.blurtext);
        brightnesstext = findViewById(R.id.brightnesstext);
        back_img = findViewById(R.id.back_img);
        recyclerView = findViewById(R.id.recycler_view);
        brightness = findViewById(R.id.brightness);
        framebtn = findViewById(R.id.btnframe);
        brightnessbtn = findViewById(R.id.btnbrightness);
        backgroundbtn = findViewById(R.id.btnbackground);
        savebtn = findViewById(R.id.savebtn);
        savepg = findViewById(R.id.savepg);
        gallerybtn = findViewById(R.id.galleryimage);
        Blurseek = findViewById(R.id.blur_seek);
        avi = (AVLoadingIndicatorView) findViewById(R.id.avi);
        pattern = findViewById(R.id.pattern);
        back_recyclerview = findViewById(R.id.back_recycler_view);

        //Button function for the Frames button.
        framebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Setting blur and brightness values.
                Blurtext.setVisibility(GONE);
                brightnesstext.setVisibility(GONE);
                brightness.setVisibility(View.GONE);
                Blurseek.setVisibility(View.GONE);
                gallerybtn.setVisibility(View.GONE);
                back_recyclerview.setVisibility(View.GONE);

                //If else for visibility of the picture.
                if (click) {
                    click = false;
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    click = true;
                }
            }
        });

        //Button function for the brightness button.
        brightnessbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Setting blur and brightness values.
                Blurtext.setVisibility(GONE);
                Blurseek.setVisibility(View.GONE);
                gallerybtn.setVisibility(View.GONE);
                back_recyclerview.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);

                //If else for visibility of the picture.
                if (click) {
                    click = false;
                    brightnesstext.setVisibility(View.VISIBLE);
                    brightness.setVisibility(View.VISIBLE);
                } else {
                    click = true;
                    brightness.setVisibility(View.GONE);
                    brightnesstext.setVisibility(View.GONE);
                }
            }
        });

        //Button function for the backgrounds button.
        backgroundbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Setting blur and brightness values.
                recyclerView.setVisibility(View.GONE);
                brightness.setVisibility(View.GONE);
                brightnesstext.setVisibility(View.GONE);

                ////If else for visibility of the picture.
                if (click) {
                    click = false;
                    Blurtext.setVisibility(View.VISIBLE);
                    Blurseek.setVisibility(View.VISIBLE);
                    gallerybtn.setVisibility(View.VISIBLE);
                    back_recyclerview.setVisibility(View.VISIBLE);
                } else {
                    Blurtext.setVisibility(GONE);
                    Blurseek.setVisibility(View.GONE);
                    gallerybtn.setVisibility(View.GONE);
                    back_recyclerview.setVisibility(View.GONE);
                    click = true;
                }
            }
        });

        //Setting the Bitmap to first image.
        firstimage.setImageBitmap(Containers.startBmp);

        //Enabling Multi Touch for first picture.
        firstimage.setOnTouchListener(new MultiTouchListener());

        //Setting the Bitmap to back image.
        back_img.setImageBitmap(createBlurBitmap( Containers.startBmp, 10));

        //Multi Touch listener for background picture.
        back_img.setOnTouchListener(new MultiTouchListener());

        //Creating a new Horizontal Adapter for Frames.
        FrameHorizontalAdapter = new HorizontalAdapter(android_image_urls);

        //Setting the layout for the frames using recyclerView.
        recyclerView.setLayoutManager(new LinearLayoutManager(Photocollage2.this, LinearLayoutManager.HORIZONTAL, false));

        //Setting Adapter, Size and Touch on the recycler.
        recyclerView.setAdapter(FrameHorizontalAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnItemTouchListener(new FrameRecyclerTouchListener(Photocollage2.this, recyclerView, new ClickListener() {

            /**
             * Method that sets background image to the first image.
             * @param view
             * @param position
             */
            @Override
            public void onClick(View view, int position) {
                firstimage.setBackgroundResource(android_image_urls[position]);
            }

            /**
             * Method for Long touch function for actual picture.
             * @param view
             * @param position
             */
            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        //Creating a new Horizontal Adapter for Backgrounds.
        BackgroundHorizontalAdapter = new HorizontalAdapter(Backgrounds);

        //Setting the layout for the frames using recyclerView.
        back_recyclerview.setLayoutManager(new LinearLayoutManager(Photocollage2.this, LinearLayoutManager.HORIZONTAL, false));
        back_recyclerview.setAdapter(BackgroundHorizontalAdapter);
        back_recyclerview.setHasFixedSize(true);
        back_recyclerview.addOnItemTouchListener(new FrameRecyclerTouchListener(Photocollage2.this, back_recyclerview, new ClickListener() {

            /**
             * Method that loads the backgrounds using Bitmap.
             * @param view
             * @param position
             */
            @Override
            public void onClick(View view, int position) {
                Blurseek.setProgress(0);
                Bitmap icon = BitmapFactory.decodeResource(getResources(), Backgrounds[position]);
                Containers.startBmp = icon;
                back_img.setImageResource(Backgrounds[position]);
                back_img.setImageBitmap(Containers.startBmp);
            }

            /**
             * Method for Long Touch function for backgrounds.
             * @param view
             * @param position
             */
            @Override
            public void onLongClick(View view, int position) {
            }
        }));

        //Setting Max Brightness to 100%
        brightness.setMax(100);

        //Brightness controls using seek bar/slider.
        brightness.setProgress(bright);
        brightness.setOnSeekBarChangeListener(new OnbrightSeekbarChangeListner());

        //The transparent background on the editing pane.
        savepg = findViewById(R.id.savepg);

        //Setting DrawingCache to enable to draw the view in bitmap.
        savepg.setDrawingCacheEnabled(true);

        //Measurements for the height and width.
        savepg.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        //Setting layout of the measured height and width.
        savepg.layout(0, 0, savepg.getMeasuredWidth(), savepg.getMeasuredHeight());
        savepg.buildDrawingCache(true);

        //Linking savebtn to the button.
        savebtn = findViewById(R.id.savebtn);

        //Functionality of the save button.
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Setting the visibility of the button.
                avi.setVisibility(GONE);
                pattern.setVisibility(GONE);

                //File saving path.
                String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/PhotoEditor";

                //Directory for the file path.
                File myDir = new File(path);
                Log.i("myDir", "" + myDir);
                myDir.mkdirs();

                //Random for filenames.
                Random generator = new Random();
                int n = 10000;
                n = generator.nextInt(n);

                //Creating filenames.
                fname = new File("Image-" + n + ".png");
                file = new File(path + File.separator + fname);
                if (file.exists()) file.delete();
                Bitmap b = null;
                try {
                    b = Bitmap.createBitmap(savepg.getDrawingCache());
                    Bitmap converetdImage = getResizedBitmap(b, 720);

                    //Creating a snapshot of ImageView for Savebtn
                    savepg.setDrawingCacheEnabled(true);
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();

                    //Compressing and saving the image in .png format.
                    converetdImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
                    FileOutputStream out = new FileOutputStream(file);
                    converetdImage.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    Log.i("log e", "" + e);
                    e.printStackTrace();
                }

                //Scanning new images.
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                intent.setData(Uri.fromFile(file));
                sendBroadcast(intent);
                Intent sharepage = new Intent(Photocollage2.this, ShareActivity.class);
                sharepage.putExtra("lname", file);
                sharepage.putExtra("imageresult", file.getAbsolutePath().toString());

                //Moving on to the share page.
                startActivity(sharepage);

                //Text popup.
                Toast.makeText(getApplicationContext(), "Your Image Saved At PhotoEditor", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        //Listener for gallery button and function that loads pictures from gallery for background.
        gallerybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Blurseek.setProgress(0);// set brightness 0
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i, "Select Picture"), RESULT_LOAD_IMAGE);
            }
        });

        //Maxing the Blurseek at 25.
        Blurseek.setMax(25);

        //Incrementation on the slider.
        Blurseek.setKeyProgressIncrement(1);
        Blurseek.setProgress(100);// set brightess progress 100

        //Listener for the slider/seekbar.
        Blurseek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            /**
             * The change of pace of progress for the background blur.
             * @param seekBar
             * @param i
             * @param fromUser
             */
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {

                Photocollage2.this.Blurtext.setText(String.format("%02d", new Object[]{Integer.valueOf(i) * 4}));
            }

            /**
             * Method that starts tracking the seekbar.
             * @param seekBar
             */
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            /**
             * Method that stops tracking the seekbar.
             * @param seekBar
             */
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                final int i = Photocollage2.this.Blurseek.getProgress();

                //Loading the background image from Bitmap if the slider progress is 0.
                if (i == 0) {
                    back_img.setImageBitmap(Containers.startBmp);

                } else {
                    back_img.setImageBitmap(createBlurBitmap(Containers.startBmp, i));
                }
            }
        });
    }


    /**
     * Method for the Click listener.
     */
    public interface ClickListener {
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }

    /**
     * Touch listener class for different types of touch listeners.
     */
    public static class FrameRecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        //GestureDetector variable.
        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        /**
         * Method that detects gestures.
         * @param context
         * @param recyclerView
         * @param clickListener
         */
        public FrameRecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    //Finding the topmost view for the listener.
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());

                    //Checking if there is child has any recyclers.
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        /**
         * Method that checks if there are gestures.
         * @param rv
         * @param e
         * @return
         */
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());

            //If gestures are found it the Listener will get recyclers from child.
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        /**
         * Touch event method.
         * @param rv
         * @param e
         */
        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        /**
         * A method that is used when the finger is lifted from the seekbar.
         * @param disallowIntercept
         */
        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    /**
     * Method that listens to the changes made to the seek bar.
     */
    private class OnbrightSeekbarChangeListner implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            //Brightness text above the slider.
            Photocollage2.this.brightnesstext.setText(String.format("%02d", new Object[]{Integer.valueOf(progress)}));

            //Brightness progress.
            firstimage.setColorFilter(setBrightness(progress));
        }

        /**
         * Method that starts tracking touch.
         * @param seekBar
         */
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        /**
         * Method that stops tracking touch.
         * @param seekBar
         */
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    //Algorithm that adjusts the brightness.
    private static PorterDuffColorFilter setBrightness(int progress) {
        if (progress >= 100) {
            int value = (progress - 100) * 255 / 100;
            return new PorterDuffColorFilter(Color.argb(value, 255, 255, 255), PorterDuff.Mode.SRC_OVER);
        } else {
            int value = (100 - progress) * 255 / 100;
            return new PorterDuffColorFilter(Color.argb(value, 0, 0, 0), PorterDuff.Mode.SRC_ATOP);
        }
    }

    /**
     * Method that loads the image and sets it on the background.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @SuppressLint("WrongConstant")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //Bitmap factory that will be used in storing the color pixels.
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Intent i;

        //If the image can be loaded it will decode the stream and store the image in bitmap.
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK) {
            try {
                bmp = BitmapFactory.decodeStream(getContentResolver().openInputStream(data.getData()), null, options);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            //If the image cannot be loaded.
            if (bmp == null) {
                Toast.makeText(getApplicationContext(), "Image could not be loaded..", 1).show();
                return;
            }

            //Compressing the quality of the background image.
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
            Bitmap decoded2 = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
            Containers.startBmp = decoded2;
            back_img.setImageBitmap(Containers.startBmp);
        }
    }

    /**
     * A method for resize function on the activity.
     * @param image
     * @param maxSize
     * @return
     */
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {

        //Variables for height and width.
        int width = image.getWidth();
        int height = image.getHeight();
        float bitmapRatio = (float) width / (float) height;

        //Algorithm for the resizing factor.
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, 1000, 1000, true);
    }

    /**
     * A method that creates a blurred bitmap for the background.
     * @param src
     * @param r
     * @return
     */
    private Bitmap createBlurBitmap(Bitmap src, float r) {
        if (r <= 0) {
            r = 0.1f;
        } else if (r > 25) {
            r = 25.0f;
        }

        //New bitmap for the blurred image.
        Bitmap bitmap = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);
        RenderScript renderScript = RenderScript.create(this);

        //Creating the Blur input and output from the bitmap.
        Allocation blurInput = Allocation.createFromBitmap(renderScript, src);
        Allocation blurOutput = Allocation.createFromBitmap(renderScript, bitmap);
        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));

        //Setting blur values.
        blur.setInput(blurInput);
        blur.setRadius(r);
        blur.forEach(blurOutput);
        blurOutput.copyTo(bitmap);
        renderScript.destroy();

        return bitmap;
    }
}
