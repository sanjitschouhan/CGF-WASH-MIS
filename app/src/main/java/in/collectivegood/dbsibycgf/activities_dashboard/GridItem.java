package in.collectivegood.dbsibycgf.activities_dashboard;

public class GridItem {
    private int image;
    private String text;
    private Class className;

    public Class getClassName() {
        return className;
    }

    public GridItem(int image, String text, Class className) {
        this.image = image;
        this.text = text;
        this.className = className;
    }

    public GridItem(int image, String text) {
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
