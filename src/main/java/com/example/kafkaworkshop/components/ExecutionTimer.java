package com.example.kafkaworkshop.components;

public class ExecutionTimer {

    static class TimedResult<ReturnType> {
        public ReturnType result;
        public long elapsed;

        public TimedResult(ReturnType result, long elapsed) {
            this.result = result;
            this.elapsed = elapsed;
        }
    }

    @FunctionalInterface
    public interface CheckedFunction<T, R> {
        R apply(T t) throws Exception;
    }

    public <InputType, ReturnType> TimedResult<ReturnType> timeCheckedFunction(InputType input,
           CheckedFunction<InputType, ReturnType> func) throws Exception {
        long startTime = System.currentTimeMillis();
        ReturnType result = func.apply(input);
        long endTime = System.currentTimeMillis();
        return new TimedResult<>(result, endTime - startTime);
    }
}
