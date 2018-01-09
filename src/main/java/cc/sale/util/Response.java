package cc.sale.util;

import java.util.List;

public class Response {

    public Response(Object result) {
        this(200, "OK", result, 1);
    }

    public Response(Object result, int size) {
        this(200, "OK", result, size);
    }

    public Response(int code, String message, Object result, int size) {
        this.code = code;
        this.message = message;
        this.result = result;
        this.size = size;
    }

    public int code;
    public String message;
    public int size;
    public Object result;
}
