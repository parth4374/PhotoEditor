package photoeditor.parth.com.csc340.Extra;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import photoeditor.parth.com.photocollage.R;

/**
 * Horizontal Adapter class for handling horizontal grids.
 *
 * @author parthpatel
 */

public class HorizontalAdapter extends android.support.v7.widget.RecyclerView.Adapter<HorizontalAdapter.MyViewHolder> {

    //An array for horizontalList to store data.
    private int[] horizontalList;

    public class MyViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {

        //An ImageView that stores frames.
        public ImageView image;

        //Displaying frames.
        public MyViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.frame);
        }
    }


    /**
     * Constructor method.
     * @param horizontalList
     */
    public HorizontalAdapter(int[] horizontalList) {
        this.horizontalList = horizontalList;
    }

    /**
     * Method that links ItemView and The inflater and Displays them.
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //Displaying the frames using inflater and view.
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.framelayout, parent, false);
        return new MyViewHolder(itemView);
    }

    /**
     * A method that loads the images.
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        //Using picasso to load images.
        Picasso.get()
                .load(horizontalList[position])
                .resize(600, 200)
                .centerInside()
                .into(holder.image);
    }

    /**
     * Getter for Item Count.
     * @return
     */
    @Override
    public int getItemCount() {
        return horizontalList.length;
    }
}