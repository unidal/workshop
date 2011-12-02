package com.site.lab.performance;

/**
 * Execution flow:
 * 
 * <pre>
 * if (isEligible(ctx)) {
 *    onBeforeWarmups(ctx);
 *    
 *    loop for <i>warm-ups</i> times {
 *       // do real execute
 *    }
 *    
 *    onBeforeExecutions(ctx);
 *    
 *    loop for <i>executions</i> times {
 *       // do real execute
 *       onExecution(ctx);
 *    }
 *    
 *    onAfterExecutions(ctx);
 * }
 * </pre>
 */
public interface ExecutionListener {
   public boolean shouldForkForGclog(ExecutionContext ctx);

   public boolean isEligible(ExecutionContext ctx);
   
   public void onBeforeWarmups(ExecutionContext ctx);
   
   public void onBeforeExecutions(ExecutionContext ctx);

   public void onAfterExecutions(ExecutionContext ctx);

   public void onExecution(ExecutionContext ctx);
}
