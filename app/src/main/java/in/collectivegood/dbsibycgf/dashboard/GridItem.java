package in.collectivegood.dbsibycgf.dashboard;

class GridItem {
    private int image;
    private String text;
    private Class className;

    GridItem(int image, String text, Class className) {
        this.image = image;
        this.text = text;
        this.className = className;
    }

    GridItem(int image, String text) {
        this.image = image;
        this.text = text;
    }

    Class getClassName() {
        return className;
    }

    int getImage() {
        return image;
    }

    String getText() {
        return text;
    }
}
