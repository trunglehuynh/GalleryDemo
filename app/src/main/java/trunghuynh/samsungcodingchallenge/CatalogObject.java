package trunghuynh.samsungcodingchallenge;

/**
 * Created by trunghuynh on 7/31/17.
 */

public class CatalogObject {
    private String catalogName;
    private String[] images;

    public CatalogObject(String catalogName, String[] images) {
        this.catalogName = catalogName;
        this.images = images;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }
}