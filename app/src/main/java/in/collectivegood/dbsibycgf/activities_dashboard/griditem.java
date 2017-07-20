package in.collectivegood.dbsibycgf.activities_dashboard;

/**
 * Created by hp on 7/20/2017.
 */

public class griditem {
    int image;
    String text;

    public griditem(int image, String text) {
        this.image = image;
        this.text = text;
    }

    public int getImage() {
        return image;
    }

    public String getText() {
        return text;
    }
}
