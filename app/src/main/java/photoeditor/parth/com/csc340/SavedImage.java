package photoeditor.parth.com.csc340;


import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;

import java.io.File;

import photoeditor.parth.com.csc340.Extra.GridViewAdapter;
import photoeditor.parth.com.photocollage.R;

/**
 * Saved Image class that displays the images created using the application.
 *
 * @author parthpatel
 */

public class SavedImage extends AppCompatActivity {

    //An array to link saved images to the Grid View.
    private String[] FilePathStrings;

    //An array for Listing files in the application save directory.
    private File[] listFile;

    //A grid to store saved image objects for display.
    GridView grid;

    //A Grid View Adapter for storing the Grid.
    GridViewAdapter adapter;

    //A file for loading purposes.
    File file;

    /**
     * A method for storing files. (Saved Images)
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_image);

        //Creating a new file object to get files from the save folder.
        file = new File(Environment.getExternalStorageDirectory()
                + File.separator + "/PhotoEditor");

        //Creating a directory if PhotoEditor folder does not exist.
        file.mkdirs();

        //Checking if the previous set path is a directory.
        if (file.isDirectory()) {

            //List file command to store files in listFile if the directory is present.
            listFile = file.listFiles();

            //Storing the loaded files to FilePathStrings.
            FilePathStrings = new String[listFile.length];
            for (int i = 0; i < listFile.length; i++) {
                FilePathStrings[i] = listFile[i].getAbsolutePath();
            }
        }

        //Setting the grid on the screen.
        grid = (GridView) findViewById(R.id.PhoneImageGrid);

        //Creating an adapter for scroll purposes.
        adapter = new GridViewAdapter(this, FilePathStrings);

        //Setting adapter to grid so display files.
        grid.setAdapter(adapter);
    }
}