package cc.sale.controller;

import cc.sale.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@RestController
public class InfoController {
    @Autowired
    JdbcTemplate jdbcTemplate;

    private Integer productsCnt;
    private Integer ordersCnt;
    private Integer customersCnt;

    //@PostConstruct
    private void cache() {
        productsCnt = jdbcTemplate.queryForObject("SELECT count(*) FROM products", Integer.class);
        ordersCnt = jdbcTemplate.queryForObject("SELECT count(*) FROM orders", Integer.class);
        customersCnt = jdbcTemplate.queryForObject("SELECT count(*) FROM customers", Integer.class);
    }

//    @PostConstruct
//    private void config() {
//        jdbcTemplate.execute("SET hive.support.concurrency=true");
//        jdbcTemplate.execute("SET hive.enforce.bucketing=true");
//        jdbcTemplate.execute("SET hive.exec.dynamic.partition.mode=nonstrict");
//        jdbcTemplate.execute("SET hive.txn.manager=org.apache.hadoop.hive.ql.lockmgr.DbTxnManager");
//        jdbcTemplate.execute("SET hive.compactor.initiator.on=true");
//        jdbcTemplate.execute("SET hive.compactor.worker.threads=1");
//        jdbcTemplate.execute("set hive.compactor.worker.threads = 1");
//    }


    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public Object get(Boolean useCache) {
        if (!useCache) {
            cache();
        }
        Map<String, Integer> result = new HashMap<>();
        result.put("productsCnt", productsCnt);
        result.put("ordersCnt", ordersCnt);
        result.put("customersCnt", customersCnt);

        return new Response(result);
    }

}
