/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package folder;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RecursiveTask;
import java.util.function.IntFunction;

public class Main {

    static long fibonacci(long n) {
        if (n <= 1) {
            return n;
        } else {
            return fibonacci(n - 1) + fibonacci(n - 2);
        }
    }

    class Fibonacci extends RecursiveTask<Long> {

        final long n;

        Fibonacci(long n) {
            this.n = n;
        }

        @Override
        protected Long compute() {
            if (n <= 10) {
                return fibonacci(n);
            }
            Fibonacci f1 = new Fibonacci(n - 1);
            f1.fork();
            Fibonacci f2 = new Fibonacci(n - 2);
            return f2.compute() + f1.join();
        }
    }

    Map<String, List<Long>> results = new HashMap<>();

    void execute(IntFunction<Long> code, int num, String name) {
        long start = System.currentTimeMillis();
        long r = code.apply(num);
        long time = System.currentTimeMillis() - start;
        results.computeIfAbsent(name, k -> new LinkedList<>()).add(time);
        System.out.println(r + " in " + time + " ms");
    }

    void run(int num, int repeats) {
        for (int i = 0; i < repeats; i++) {
            execute(n -> fibonacci(n), num, "one");
            execute(n -> new Fibonacci(n).compute(), num, "multi");
        }
        results.forEach((k, v) -> System.out.println(k + "\t" + v.stream().mapToLong(l -> l).average().getAsDouble()));
    }

    public static void main(String... args) {
        new Main().run(Integer.valueOf(args[0]), Integer.valueOf(args[1]));
    }
}
