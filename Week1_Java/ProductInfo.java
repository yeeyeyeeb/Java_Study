public class ProductInfo {
    private String name;
    private String type;
    private String shelfLifeText;

    public ProductInfo(String name, String type, String shelfLifeText) {
        this.name = name;
        this.type = type;
        this.shelfLifeText = shelfLifeText;
    }

    public String getName() { return name; }
    public String getType() { return type; }
    public String getShelfLifeText() { return shelfLifeText; }

    @Override
    public String toString() {
        return "[제품] " + name + " (식품유형: " + type + ", 유통기한: " + shelfLifeText + ")";
    }
}
