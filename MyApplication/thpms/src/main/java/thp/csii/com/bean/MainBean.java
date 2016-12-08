package thp.csii.com.bean;

/**
 * Created by Administrator on 2016/8/30.
 */
public class MainBean {
    String url;
    String name;

    public MainBean(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public MainBean() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
