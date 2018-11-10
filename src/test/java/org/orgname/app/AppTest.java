package org.orgname.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */

    private static JedisPool pool = null;
    @BeforeClass
    public static void setUp()throws Exception {
        pool = new JedisPool("localhost", 6379);
        try (Jedis jedis = pool.getResource()) {
            jedis.del("echotime");
            java.util.Date date = new java.util.Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            Map<String, Double> mem = new HashMap<>();
            for (int i = 0; i < 3; i++) {
                cal.add(Calendar.SECOND, i);
               double d =(double) cal.getTime().getTime();
                mem.put("test " + i, d);
            }
            jedis.zadd("echotime", mem);
        }  if (pool == null) {
            pool.close();
        }

    }
    @Test
    public void runFirstTime()  {

       Service service = new Service();
       Date time = new Date();
       double d = (double) time.getTime();
       service.run(0,d);

        try(  Jedis jedis  = pool.getResource()){
            long elem = jedis.zcount("echotime","-inf", "+inf");
            assertEquals("expeted number of elements",2 ,elem);
        }finally {
            if (pool == null) {
                pool.close();
            }

        }

    }

    @Test
    public void runFutureTime()  {

        Service service = new Service();
        Date time = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        cal.add(Calendar.MINUTE, 5);
        double d =(double) cal.getTime().getTime();

        service.run(0,d);

        try(  Jedis jedis  = pool.getResource()){
            long elem = jedis.zcount("echotime","-inf", "+inf");
            assertEquals("expeted number of elements",0 ,elem);
        }finally {
            if (pool == null) {
                pool.close();
            }
        }

    }


}

