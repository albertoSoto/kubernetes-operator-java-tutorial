package org.acme.customResource;

public class WebPageSpec {
    private String html;

    public String getHtml() { return html;}

    public WebPageSpec setHtml(String html){
        this.html = html;
        return this;
    }
}
