# DAT250 Experiment Assignment 5 Report

## Installation and Setup
I installed Redis locally on Ubuntu 22.04 following the official instructions. I verified the installation using the CLI command `` redis-cli ping ``
which returned `PONG`, confirming the server is running.

## Technical Problems and Solutions
During the experiment, I encountered the following issues:

1. **Case sensitivity in CLI commands**
    * Initially, commands like `set user bob` returned errors because I typed lowercase or with typos.
    * Solution: Always use `SET` and `GET` in uppercase and double-check key names.

2. **Redis CLI vs Java Jedis API**
    * Some commands behave slightly differently between the CLI and Java client (`UnifiedJedis`).
    * Solution: Follow the Jedis API exactly as documented.

3. **Connection management**
    * Remembering to close the Jedis connection is important to avoid leaks.
    * Solution: Used `jedis.close()` at the end of each experiment.

## Experiments 
I use a ``switch()`` at the ``main`` to select which experiment is runned.
### 1. CLI 
###### (Done at terminal)
* **Case 1: Logged-in users**
    * Used a Redis Set to store connected users (`logged_in_users`).
    * Operations: `SADD`, `SREM`, `SMEMBERS`.
    * Verified sequence: Alice logs in, Bob logs in, Alice logs out, Eve logs in.

* **Case 2: Poll**
    * Used a Redis Hash to store a poll (`poll:01`) with title and votes for each option.
    * Operations: `HSET`, `HGETALL`, `HINCRBY`.
    * Verified votes increment for "yes" option.

### 2. Java (Jedis) 
###### (Case 1 at main)
* Repeated all CLI experiments in Java using `UnifiedJedis`.
* Same data structures: Set for users, Hash for polls.
* Verified that data inserted/retrieved from Redis matches expected values.

### 3. Cache Implementation 
###### (Case 2 at main)
I was uncertain as to whether it would be preferable to create a class or implement it at the main. The reason I chose to do it at the main is because we're not planning to expand this project any further.
* Implemented a simple cache for polls:
    * **Logic:**
        1. Check if poll exists in Redis (`jedis.exists(key)`).
        2. If yes: return cache (cache hit).
        3. If no: simulate database query, store in Redis with TTL 60s (cache miss).
    * **Votes update:** Increment option in Redis (`HINCRBY`) after a vote.
    * **TTL:** Each poll cached for 60 seconds.
    * **Cache invalidation:** Handled by TTL and by incrementing votes directly in Redis.
* Verified workflow:
    * First access: cache miss, poll stored in Redis.
    * Subsequent access: cache hit, data returned instantly.
    * After a vote: cache updated automatically.

## Pending Issues
No major pending issues. The actual database is simulated; in a real system, vote updates would also update the relational database. TTL expiration and automatic invalidation were not observed in real time, but the code implements it correctly.

