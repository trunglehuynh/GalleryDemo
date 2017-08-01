package trunghuynh.samsungcodingchallenge;

import android.graphics.Bitmap;

/**
 * Created by trunghuynh on 7/31/17.
 */

public class RowObject {

    private String imageName;
    private Bitmap imageBitmap;

    public RowObject(String imageName, Bitmap imageBitmap) {
        this.imageName = imageName;
        this.imageBitmap = imageBitmap;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageId(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }
}
