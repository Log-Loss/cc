package cc.sale.controller;

import cc.sale.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class CustomerController {
    @Autowired
    JdbcTemplate jdbcTemplate;

    private Object resultCache;
    private Integer sizeCache;

    @RequestMapping(value = "/customer", method = RequestMethod.GET)
    public Object get(String customerId) {
        Object result = jdbcTemplate.queryForMap("SELECT * FROM customers WHERE customer_id='" + customerId + "'");
        return new Response(result);
    }

    @RequestMapping(value = "/customer/list", method = RequestMethod.GET)
    public Object getList(Boolean useCache) {
        if (!useCache) {
            resultCache = jdbcTemplate.queryForList("SELECT * FROM customers LIMIT 200");
        }
        return new Response(resultCache);
    }

    @RequestMapping(value = "/customer", method = RequestMethod.DELETE)
    public Object delete(@RequestBody Map<String, String> body) {
        jdbcTemplate.execute("DELETE FROM customers WHERE customer_id='" + body.get("customerId") + "'");
        return new Response("ok");
    }

    @RequestMapping(value = "/customer", method = RequestMethod.POST)
    public Object post(@RequestBody Map<String, String> body) {
        jdbcTemplate.execute("INSERT INTO customers VALUES ('" + body.get("customerId") + "',0)");
        Object result = jdbcTemplate.queryForMap("SELECT * FROM customers WHERE customer_id='" + body.get("customerId") + "'");
        return new Response(result);
    }

}
