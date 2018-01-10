package cc.sale.controller;

import cc.sale.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@RestController
public class OrderService {
    @Autowired
    JdbcTemplate jdbcTemplate;

    private Object resultCache;
    private Integer sizeCache;

    @RequestMapping(value = "/order", method = RequestMethod.GET)
    public Object get(String productId, String customerId) {
        if (productId == null && customerId == null) {
            return new Response(412, "Missing Param", null, 0);
        }
        Object result;
        if (productId == null) {
            result = jdbcTemplate.queryForList("SELECT * FROM orders WHERE  customer_id='" + customerId + "'");
        } else if (customerId == null) {
            result = jdbcTemplate.queryForList("SELECT * FROM orders WHERE product_id='" + productId + "'");
        } else {
            result = jdbcTemplate.queryForList("SELECT * FROM orders WHERE product_id='" + productId + "'and customer_id='" + customerId + "'");
        }
        return new Response(result, ((List) result).size());
    }

    @RequestMapping(value = "/order/list", method = RequestMethod.GET)
    public Object get(Boolean useCache) {
        if (!useCache) {
            resultCache = jdbcTemplate.queryForList("SELECT * FROM orders LIMIT 200");
            sizeCache = jdbcTemplate.queryForObject("SELECT count(*) FROM orders", Integer.class);
        }
        return new Response(resultCache, sizeCache);
    }

    @RequestMapping(value = "/order", method = RequestMethod.POST)
    public Object create(@RequestBody Map<String, String> body) {
        long time = System.currentTimeMillis() / 1000L;
        String sql = String.format("INSERT INTO orders VALUES (NULL, '%s', '%s', %d, %s, %s, %s, %s)", body.get("customerId"), body.get("productId"), time, body.get("basePrice"), body.get("discountRate"), body.get("productCnt"), body.get("rating"));
        jdbcTemplate.execute(sql);
        return jdbcTemplate.queryForMap("SELECT * FROM orders WHERE time='" + time + "'");
    }

    @RequestMapping(value = "/order", method = RequestMethod.DELETE)
    public Object delete(@RequestBody Map<String, String> body) {
        jdbcTemplate.execute("DELETE FROM orders WHERE id='" + body.get("id") + "'");
        return new Response("ok");
    }

    @RequestMapping(value = "/order", method = RequestMethod.PUT)
    public Object update(@RequestBody Map<String, String> body) {
        delete(body);
        String sql = String.format("INSERT INTO orders VALUES ('%s', '%s', '%s', %d, %s, %s, %s, %s)", body.get("id"), body.get("customerId"), body.get("productId"), Integer.valueOf(body.get("time")), body.get("basePrice"), body.get("discountRate"), body.get("productCnt"), body.get("rating"));
        jdbcTemplate.execute(sql);
        return jdbcTemplate.queryForMap("SELECT * FROM orders WHERE id='" + body.get("id") + "'");
    }
}
