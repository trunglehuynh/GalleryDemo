package trunghuynh.samsungcodingchallenge;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by trunghuynh on 7/31/17.
 */

public class ImageDetail extends Activity {

    final String TAG= "ImageDetail";

    RowObject[] items = null;
    ListView myListView;
    MyListViewAdapter myAdapter;
    //    int heightDp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_detail);

//        heightDp = imageHeightdp();
        items = getData();
        myListView = (ListView)findViewById(R.id.detailListView);
        myAdapter = new MyListViewAdapter(this,R.layout.image_detail_row,R.id.detailRowImageView,R.id.detailRowTextView,items);
        myListView.setAdapter(myAdapter);
        View header = getLayoutInflater().inflate(R.layout.image_detail_header, null);
        myListView.addHeaderView(header);

        ImageButton imageButton = (ImageButton)findViewById(R.id.imageDetailButtonBack);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


    private RowObject[] getData(){

        Intent intent = getIntent();
        String[] images = intent.getStringArrayExtra(getResources().getString(R.string.ImageDetailArray));
        String catalogName = intent.getStringExtra(getResources().getString(R.string.ImageDetailName));
        for (String str: images)
            Log.d(TAG, catalogName + "  "+str);

        RowObject[] items = new RowObject[images.length]; // have to check condition

        try {

            InputStream inputS = null;
            Drawable d ;
            Bitmap bitmap;
            String imageURL;
            AssetManager assetManager = getAssets();

            for (int i = 0 ; i <images.length ; i++){
                imageURL = catalogName+"/"+images[i];

                inputS = assetManager.open(imageURL);
                bitmap = BitmapFactory.decodeStream(inputS);
                items[i] = new RowObject(images[i],bitmap);
            }
            inputS.close();
        }
        catch(IOException ex) {
            return null;
        }
        return items;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}
