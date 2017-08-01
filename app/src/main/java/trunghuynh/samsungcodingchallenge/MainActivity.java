package trunghuynh.samsungcodingchallenge;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;



import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends Activity {

    final String TAG = "MainActivity";
    final String GALLERYFOLDER = "gallery";

    final int STARTX = 0, WIDTH = 1280, ROUNDPIXEL = 20;
    final int STARTY = 320, HEIGHT = 640;
    CatalogObject[] mainCatalog;
    RowObject[] items;
    CatalogObject catalog;

//    ViewPager viewPager;
    int catalogIndex = -1;
    String[] imageURLs = null;
    Drawable[] drawables = null;
    ViewPageAdapter viewPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final ListView catalogsListView = (ListView) findViewById(R.id.MainListView);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.MainViewPage);

//        Log.d(TAG, "test...........");

        //loanding all folder
        mainCatalog = loadFolder(GALLERYFOLDER);

        // loading all rows infromation
        items = loadItemAdapter(mainCatalog);
        if(items!= null) {
            MyListViewAdapter adapter = new MyListViewAdapter(this, R.layout.activity_main_row, R.id.catalogImage, R.id.catalogText, items);
            catalogsListView.setAdapter(adapter);
        }
        // open Image Detail class
        catalogsListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        catalog = mainCatalog[i];
                        Intent intent = new Intent(getBaseContext(), ImageDetail.class);
                        intent.putExtra(getResources().getString(R.string.ImageDetailArray), catalog.getImages());
                        intent.putExtra(getResources().getString(R.string.ImageDetailName), GALLERYFOLDER + "/" + catalog.getCatalogName());
                        startActivity(intent);

                    }
                }
        );

        // open preview view (long click)
        catalogsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (catalogIndex != i) {

                    catalogIndex = i;
                    imageURLs = mainCatalog[catalogIndex].getImages();
                    drawables = new Drawable[imageURLs.length];
                    try {

                        InputStream inputS = null;
                        Drawable d;
                        for (int y = 0; y < imageURLs.length; y++) {
                            inputS = getAssets().open(GALLERYFOLDER + "/" + mainCatalog[catalogIndex].getCatalogName() + "/" + imageURLs[y]);
                            drawables[y] = Drawable.createFromStream(inputS, null);
                        }

                        inputS.close();

                        viewPageAdapter = new ViewPageAdapter(drawables, MainActivity.this, R.layout.view_page_row, R.id.viewPageImageId);

                    } catch (Exception ex) {

                        Log.e(TAG, ex.toString());
                    }

                }

                viewPager.setVisibility(View.VISIBLE);
                viewPager.setAdapter(viewPageAdapter);
                Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.open_preview);
                viewPager.startAnimation(animation);

                return true;
            }


        });

//      swipe left and right detection
        catalogsListView.setOnTouchListener(new View.OnTouchListener() {

            float positionX = -1;
            float distance = 0;
            float rightThreshold = 100;
            float leftThreshold = rightThreshold * (-1);
            int index = 0;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (viewPager.getVisibility() == View.INVISIBLE) {
                    return false;
                }

                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    viewPager.setVisibility(View.INVISIBLE);
                    index = 0;
                    return true;
                } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    if (positionX == -1) {
                        positionX = motionEvent.getX();
                        return true;

                    }
                    distance = motionEvent.getX() - positionX;

//                    testing
//                    Log.d(TAG, "distance " + distance);
                    if (distance >= rightThreshold) {

//                        testing
//                        Log.d(TAG, "rightThreshold ");
                        positionX = motionEvent.getX();
                        --index;
                        if (index < 0) {
                            index = 0;
                            return false;
                        }
                        viewPager.setCurrentItem(index, true);

                    } else if (distance <= leftThreshold) {
//                    testing
//                        Log.d(TAG, "leftThreshold ");
                        positionX = motionEvent.getX();
                        ++index;
                        if (index >= drawables.length) {
                            index = drawables.length - 1;
                            return false;
                        }

                        viewPager.setCurrentItem(index, true);

                    }
//                    testing
//                    Log.d(TAG, "motionEvent.getX() "+ motionEvent.getX());
                }


                return false;
            }

        });



    }

    private RowObject[] loadItemAdapter(CatalogObject[] catalogs) {

        if (catalogs == null || catalogs.length == 0) return null;
        String imageURL;
        RowObject[] items = new RowObject[catalogs.length];

        try {
            // get input stream
            InputStream inputS = null;
            AssetManager assetManager = getAssets();
//            Drawable d ;
            Bitmap bitmap;
            for (int i = 0; i < catalogs.length; i++) {
                // get first Image
                imageURL = GALLERYFOLDER + "/" + catalogs[i].getCatalogName() + "/" + catalogs[i].getImages()[0];
                inputS = assetManager.open(imageURL);
                bitmap = BitmapFactory.decodeStream(inputS);
                items[i] = new RowObject(catalogs[i].getCatalogName(), getRoundedCornerBitmap(bitmap, STARTX, STARTY, WIDTH, HEIGHT, ROUNDPIXEL));

            }

            if (inputS != null)
                inputS.close();
        } catch (IOException ex) {
            Log.d(TAG, "error " + ex.getMessage());
            return null;
        }
        return items;
    }

    // load folder
    private CatalogObject[] loadFolder(String parentFolder) {

        try {

            String path;
            // get all folders in gallery
            String[] folders = getAssets().list(parentFolder);
            if (folders == null || folders.length == 0) return null;

            CatalogObject[] catalogs = new CatalogObject[folders.length];
            for (int i = 0; i < folders.length; i++) {
                String[] images = getAssets().list(parentFolder + "/" + folders[i]);

                // assume the app does not display empty folder
                if (images.length == 0) continue;
                catalogs[i] = new CatalogObject(folders[i], images);
                Log.d(TAG, folders[i]);
            }
            return catalogs;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // load all images in folder
    private String[] loadImages(String urlFolder) {
        try {
            String[] images = getAssets().list(urlFolder);
            return images;
        } catch (IOException e) {
            e.printStackTrace();
            return null;

        }

    }

    // resize image and round cornner
    // https://stackoverflow.com/questions/2459916/how-to-make-an-imageview-with-rounded-corners
    private Bitmap getRoundedCornerBitmap(Bitmap bitmap, int startX, int startY, int width, int height, int roundpixel) {
        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap = Bitmap.createBitmap(bitmap, startX, startY, width, height + startY);
        Canvas canvas = new Canvas(output);

        final int color = 0xffffffff;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, width, height);
        final RectF rectF = new RectF(rect);
        final float roundPx = roundpixel;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}
