package com.example.kafkaworkshop.components;

import java.util.concurrent.Callable;

public class ExecutionTimer {

    public static class TimedResult<ReturnType> {
        public ReturnType result;
        public long elapsed;

        public TimedResult(ReturnType result, long elapsed) {
            this.result = result;
            this.elapsed = elapsed;
        }
    }

    public <ReturnType> TimedResult<ReturnType> time(Callable<ReturnType> func) throws Exception {
        long startTime = System.currentTimeMillis();
        ReturnType result = func.call();
        long endTime = System.currentTimeMillis();
        return new TimedResult<>(result, endTime - startTime);
    }
}
