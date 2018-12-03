package photoeditor.parth.com.csc340.Extra;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import photoeditor.parth.com.photocollage.R;

/**
 * GridViewAdapter class for handling grids.
 *
 * @author parthpatel
 */

public class GridViewAdapter extends BaseAdapter {

    // Declare variables
    private Activity activity;
    private String[] filepath;
    ImageView image;
    private static LayoutInflater inflater = null;

    /**
     * Grid View Adaptor method for setting paths and layout.
     * @param a
     * @param fpath
     */
    public GridViewAdapter(Activity a, String[] fpath ) {
        activity = a;
        filepath = fpath;

        //
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * Getter for Count.
     * @return
     */
    public int getCount() {
        return filepath.length;
    }

    /**
     * Getter for Item.
     * @param position
     * @return
     */
    public Object getItem(int position) {
        return position;
    }

    /**
     * Getter for Item ID.
     * @param position
     * @return
     */
    public long getItemId(int position) {
        return position;
    }

    /**
     * Getter for View method that uses inflater to view gallery items.
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.gallery_item, null);

        //Locating the ImageView in gallery_item.xml
        image = (ImageView) vi.findViewById(R.id.thumbImage);

        //Decode the filepath with BitmapFactory followed by the position
        Bitmap bitmap1 = BitmapFactory.decodeFile(filepath[position]);

        //Set the decoded bitmap into ImageView
        image.setImageBitmap(bitmap1);

        //Returns the View vi.
        return vi;
    }
}