package info.magnolia.k8s.staticHtml;

public class WebPageSpec {
    private String html;

    public String getHtml() { return html;}

    public WebPageSpec setHtml(String html){
        this.html = html;
        return this;
    }
}
