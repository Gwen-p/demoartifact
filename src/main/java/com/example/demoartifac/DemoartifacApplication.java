package com.example.demoartifac;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.UnifiedJedis;

@SpringBootApplication
@RestController
public class DemoartifacApplication {

	public static void main(String[] args) {
        int mode = 2;
        switch (mode) {
            case 0:
        //Experiment 1:
                SpringApplication.run(DemoartifacApplication.class, args);
                break;
        //Experiment 5: ------------------------------------------------------------------
            case 1:
                UnifiedJedis jedis = new UnifiedJedis("redis://localhost:6379");

                //      Case 1:
                // Delete set idf it exist
                jedis.del("logged_in_users");

                // Alice log in
                jedis.sadd("logged_in_users", "alice");

                // Bob log in
                jedis.sadd("logged_in_users", "bob");

                // Alice log out
                jedis.srem("logged_in_users", "alice");

                // Eve log in
                jedis.sadd("logged_in_users", "eve");

                // Show users connected
                System.out.println("Logged in users: " + jedis.smembers("logged_in_users"));
                System.out.println("Expected output: Logged in users: [bob, eve]");

                //      Case 2:
                // Create poll
                jedis.hset("poll:01", "title", "Pineapple on Pizza?");
                jedis.hset("poll:01", "option:yes", "269");
                jedis.hset("poll:01", "option:no", "268");
                jedis.hset("poll:01", "option:meh", "42");

                // Show poll
                System.out.println("Poll data: " + jedis.hgetAll("poll:01"));
                System.out.println("Expected output: Poll data: {title=Pineapple on Pizza?, option:yes=269, option:no=268, option:meh=42}");


                // Add yes votes
                jedis.hincrBy("poll:01", "option:yes", 1);

                // Show actual yes votes
                System.out.println("Yes votes: " + jedis.hget("poll:01", "option:yes"));
                System.out.println("Expected output: Yes votes: 270");

                jedis.close();

                break;

            case 2:

                UnifiedJedis jedis2 = new UnifiedJedis("redis://localhost:6379");
                String pollKey = "poll:01";

                // If it is at cache
                if (jedis2.exists(pollKey)) {
                    System.out.println(" Cache hit");
                    System.out.println(jedis2.hgetAll(pollKey));
                } else {
                    // Simulate consuly at DB
                    System.out.println(" Cache miss, consulting DB...");
                    jedis2.hset(pollKey, "title", "Pineapple on Pizza?");
                    jedis2.hset(pollKey, "option:yes", "269");
                    jedis2.hset(pollKey, "option:no", "268");
                    jedis2.hset(pollKey, "option:meh", "42");
                    jedis2.expire(pollKey, 60); // TTL
                    System.out.println(jedis2.hgetAll(pollKey));
                }

                // Simular voto
                jedis2.hincrBy(pollKey, "option:yes", 1);
                System.out.println(" After vote:");
                System.out.println(jedis2.hgetAll(pollKey));

                jedis2.close();

                break;
        }



	}

    @GetMapping("/")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }
}
