package com.statsd.example.statsclient;

import com.codahale.metrics.*;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;

import java.net.InetSocketAddress;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class GetStarted {
    static final MetricRegistry metrics = new MetricRegistry();
    public static void main(String args[]) throws InterruptedException {
      startReport();
      Meter requests = metrics.meter("requests");
      
      while(true){
    	  int randomInt = new Random().nextInt(20);
    	  for(int i =0 ; i < randomInt; i++){
    		  requests.mark(); 
    	  }
    	  
    	  Thread.sleep(2000);
      }
     //wait5Seconds();
    }

  static void startReport() {
      ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics)
          .convertRatesTo(TimeUnit.MINUTES)
          .convertDurationsTo(TimeUnit.MINUTES)
          .build();
      reporter.start(1, TimeUnit.MINUTES);
      Graphite graphite = new Graphite(new InetSocketAddress("35.200.159.151", 2003));
      final GraphiteReporter greporter = GraphiteReporter.forRegistry(metrics)
              .prefixedWith("testwork")
              .convertRatesTo(TimeUnit.MINUTES)
              .convertDurationsTo(TimeUnit.MINUTES)
              .filter(MetricFilter.ALL)
              .build(graphite);
      greporter.start(1, TimeUnit.MINUTES);
  }

  static void wait5Seconds() {
      try {
          Thread.sleep(50*1000);
      }
      catch(InterruptedException e) {}
  }
}
