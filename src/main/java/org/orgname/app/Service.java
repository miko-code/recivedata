package org.orgname.app;

import redis.clients.jedis.*;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Service {

    private final static Logger LOGGER = Logger.getLogger(Service.class.getName());


    public void run(double min, double max) {
      try( JedisPool  pool = new JedisPool(new JedisPoolConfig(), "localhost")) {
          //get a jedis connection jedis connection pool
          try (Jedis jedis = pool.getResource()) {
              try (Transaction t = jedis.multi()) {

                  Response<Set<String>> setResponse = t.zrangeByScore("echotime", min, max);
                  t.zremrangeByScore("echotime", min, max);
                  t.exec();
                  for (String s : setResponse.get()) {
                      LOGGER.info(s);
                  }


              } catch (IOException e) {
                  LOGGER.log(Level.SEVERE, e.toString());
              }

          }


      }
      }


    public void runFirstTime(){
        Service s = new Service();
        Date time = new Date();
        double d = (double) time.getTime();
        s.run(0,d);
    }

    public void runWithTimer(){

            TimerTask task = new TimerTask() {
            Service s = new Service();


            @Override
            public void run() {
                Date time = new Date();
                double d = (double) time.getTime();
                Calendar cal = Calendar.getInstance();
                cal.setTime(time);
                cal.add(Calendar.SECOND, -1);
                s.run(cal.getTime().getTime(),d);
            }
        };
        Timer timer = new Timer("Timer");

        long delay  = 1000L;
        long period = 1000L;
        timer.scheduleAtFixedRate(task, delay, period);
    }

}
