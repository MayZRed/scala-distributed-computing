# Parallelism in Scala
See how Fork-Join-Pools (work-stealing Thread-Pools) are used with different Tasks. Also get a small glance at the performance for different configurations of a Thread-Pool. For detailed explanations look at the comments in code.

## Further Information
**Differences of Task execution:**
 - *pool.execute(task)*: Is used for "fire-and-forget" tasks, without a return value or the possibility to check the state of the task.
 - *pool.invoke(task)*: Is used for asynchronous tasks, which need to provide access to their current state and/or a return value later on.
 - *pool.submit(task)*: Is used when you need a blocking call and require the result immediately in the calling thread.

**What is a common Fork-Join-Pool?**

The common Fork-Join-Pool is a globally available Pool, which is managed by the JVM. He can be used for general non-blocking tasks. But processes from different programs among the whole machine can access and use it. If this pool is fully occupied, there's no further capacity for your processes. An explicitly created Fork-Join-Pool is reserved for tasks / processes of your program, but must also be managed by your program.