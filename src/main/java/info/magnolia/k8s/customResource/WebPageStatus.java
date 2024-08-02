package info.magnolia.k8s.customResource;

public class WebPageStatus {
    private Boolean ready;

    public Boolean getReady(){ return ready;}

    public WebPageStatus setReady(Boolean ready){
        this.ready = ready;
        return this;
    }
}
