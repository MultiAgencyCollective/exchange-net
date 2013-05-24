package models;

public class CloudNode {

    public String text;
    
    public int weight;
    
    public String url;
    
    public CloudNode(
        final String text,
        final int weight,
        final String url
    ) {
        this.text = text;
        this.weight = weight;
        this.url = url;
    }
}
