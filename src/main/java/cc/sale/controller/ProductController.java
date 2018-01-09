package cc.sale.controller;

import cc.sale.util.Response;
import org.apache.tomcat.jni.Time;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Random;
import java.util.Timer;

@RestController
public class ProductController {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public static String getRandomString(int length) { //length表示生成字符串的长度
        String base = "QWERTYUIOPASDFGHJKLZXCVBNM0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    @RequestMapping(value = "/product", method = RequestMethod.GET)
    public Object get(String productId) {
        Object result = jdbcTemplate.queryForMap("SELECT * FROM products WHERE product_id='" + productId + "'");
        return new Response(result);
    }


    @RequestMapping(value = "/product/list", method = RequestMethod.GET)
    public Object get() {
        Object result = jdbcTemplate.queryForList("SELECT * FROM products LIMIT 20");
        Integer size = jdbcTemplate.queryForObject("SELECT count(*) FROM products", Integer.class);
        return new Response(result, size);
    }

    @RequestMapping(value = "/product", method = RequestMethod.POST)
    public Object add(@RequestBody Map<String, String> body) {
        String productId = getRandomString(10);
        String sql = String.format("INSERT INTO products VALUES ('%s', '%s', %s, '%s', '%s',%s ,%s ,%s ,%s)", productId, body.get("title"), body.get("price"), body.get("imUrl"), body.get("category"), body.get("rank"), body.get("rating"), body.get("soldCnt"), body.get("inventoryCnt"));
        jdbcTemplate.execute(sql);
        Object result = jdbcTemplate.queryForMap("SELECT * FROM products WHERE product_id='" + productId + "'");
        return new Response(result);
    }


    @RequestMapping(value = "/product", method = RequestMethod.DELETE)
    public Object delete(@RequestBody Map<String, String> body) {
        jdbcTemplate.execute("DELETE FROM products WHERE product_id= '" + body.get("productId") + "'");
        return new Response("success");
    }

    @RequestMapping(value = "/product", method = RequestMethod.PUT)
    public Object update(@RequestBody Map<String, String> body) {
        delete(body);
        String sql = String.format("INSERT INTO products VALUES ('%s', '%s', %s, '%s', '%s',%s ,%s ,%s ,%s)", body.get("productId"), body.get("title"), body.get("price"), body.get("imUrl"), body.get("category"), body.get("rank"), body.get("rating"), body.get("soldCnt"), body.get("inventoryCnt"));
        jdbcTemplate.execute(sql);
        Object result = jdbcTemplate.queryForMap("SELECT * FROM products WHERE product_id='" + body.get("productId") + "'");
        return new Response(result);
    }
}
