import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Request {
    private final String path;
    private String queryString;

    public Request(String path) {
        this.path = path;
        this.queryString = null;
    }

    public Request(String path, String queryString) {
        this.path = path;
        this.queryString = queryString;
    }

    public String getPath() {
        return path;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getQueryParam(String name) {
        List<NameValuePair> params = getQueryParams();
        for (NameValuePair param : params) {
            if (param.getName().equals(name)) {
                return param.getValue();
            }
        }
        return null;
    }

    public List<NameValuePair> getQueryParams() {
        List<NameValuePair> params = new ArrayList<>();
        if (this.queryString != null) {
            params = URLEncodedUtils.parse(this.queryString, StandardCharsets.UTF_8);
        }
        return params;
    }
}
