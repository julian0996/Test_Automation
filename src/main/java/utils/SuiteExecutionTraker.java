package utils;

public class SuiteExecutionTraker {
    private static int executedSuites = 0;
    private static int totalSuites = 0;
    private static boolean isCleanUpExecuted = false;

    public static synchronized void setTotalSuites(int count) {totalSuites = count;}

    public static synchronized boolean shouldExecuteCleanUp() {
        executedSuites++;
        if(executedSuites == totalSuites && !isCleanUpExecuted) {
            isCleanUpExecuted = true;
            return true;
        }
        return false;
    }
}
