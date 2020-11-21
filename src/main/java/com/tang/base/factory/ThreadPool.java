package com.tang.base.factory;

import com.tang.base.consts.DatePatternConsts;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

/**
 * @description 线程池
 * 
 * 使用:
 * 1、在线程池中异步执行指定任务可以调用以submit打头的方法即可
 * 2、在线程池中定时执行指定任务可以调用以schedule打头的方法即可
 * 3、在当前线程中同步执行指定任务可以调用以execute打头的方法即可
 * 
 * 说明:
 * 作为任务的参数传递可以是一个Lambda表达式或实现了函数接口的可调用对象
 * 带可变参数的方法设计为方便应用层调用,但无法适用于带可变参数的目标方法
 * 带参数的异步调用适用于非Lambda表达式的的独立接口层设计,但也适用于使用Lambda的上下文调用方式
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ThreadPool {
    /**
     * 线程池执行器
     */
    private static ScheduledThreadPoolExecutor executorService;
    
    /**
     * 计划任务结果集队列尺寸
     */
    private static Integer SCHEDULED_QUEUE_CAPACITY = Integer.MAX_VALUE;
    
    /**
     * 全局线程执行结果字典
     */
    private static Map<Long,QueueFuture> THREAD_RESULT=new ConcurrentHashMap<Long,QueueFuture>();
    
    /**
     * 全局线程上下文队列
     */
    private static Map<String,QueueFuture<ThreadContext>> THREAD_QUEUE=new ConcurrentHashMap<String,QueueFuture<ThreadContext>>();
    
    static{
    	final Runtime JRE=Runtime.getRuntime();
    	Integer logicCores=JRE.availableProcessors();
    	executorService=new ScheduledThreadPoolExecutor(logicCores);
    	executorService.setMaximumPoolSize(logicCores);
    	JRE.addShutdownHook(new Thread(()->executorService.shutdown()));
    }
    
    /**
     * 查找类中的方法(不含超类和超接口中的方法)
     * @param targetType 查找的目标类型
     * @param methodName 查找的目标方法
     * @param typeArgs 查找的目标参数列表
     * @return 目标方法
     */
    private static final Method findMethod(Class<?> targetType,String methodName,Class[] typeArgs){
		Method targetMethod=null;
		Method[] methods=targetType.getDeclaredMethods();
		out:for(Method iteMethod:methods){
			if(!methodName.equals(iteMethod.getName())) continue out;
			Class[] targetArgTypes=iteMethod.getParameterTypes();
			if(targetArgTypes.length!=typeArgs.length) continue out;
			for(int i=0;i<targetArgTypes.length;i++) if(!targetArgTypes[i].isAssignableFrom(typeArgs[i])) continue out;
			targetMethod=iteMethod;
			break out;
		}
		return targetMethod; 
    }
    
    /**
     * 在当前线程中执行参数对象中的方法(可以是静态方法或私有方法)
     * @param target 需要执行的目标对象
     * @param method 需要调用的目标方法
     * @param args 传递给方法的参数列表
     * @return Object对象
     */
    public static Object execute(Object target,String method,Object... args){
		final Class[] typeArgs=new Class[null==args?0:args.length];
    	if(null!=args && 0!=args.length)for(int i=0;i<args.length;typeArgs[i]=args[i].getClass(),i++);
    	try {
    		Class<?> targetType=null;
    		if(target instanceof Class){
    			targetType=(Class)target;
    		}else{
    			targetType=target.getClass();
    		}
    		Method targetMethod=findMethod(targetType,method,typeArgs);
    		if(null==targetMethod) return null;
    		targetMethod.setAccessible(true);
    		return targetMethod.invoke(target, args);
    	} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
    }
    
    /**
     * 在当前线程中执行参数对象中的方法(可以是静态方法或私有方法)
     * @param target 需要执行的目标对象
     * @param method 需要调用的目标方法
     * @param returnType 目标方法返回类型
     * @param args 传递给方法的参数列表
     * @return 泛化对象
     */
    public static <R> R execute(Object target,String method,Class<R> returnType,Object... args){
		final Class[] typeArgs=new Class[null==args?0:args.length];
    	if(null!=args && 0!=args.length)for(int i=0;i<args.length;typeArgs[i]=args[i].getClass(),i++);
    	try {
    		Class<?> targetType=null;
    		if(target instanceof Class){
    			targetType=(Class)target;
    		}else{
    			targetType=target.getClass();
    		}
    		Method targetMethod=findMethod(targetType,method,typeArgs);
    		if(null==targetMethod) return null;
    		targetMethod.setAccessible(true);
    		return returnType.cast(targetMethod.invoke(target, args));
    	} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
    }
    
    /**
     * 异步执行一个指定的任务
     * @param task 需要执行的任务
     * @return Future对象
     */
    public static Future<?> submit(Runnable task){
    	return executorService.submit(task);
    }
    
    /**
     * 异步执行一个指定的任务
     * @param task 需要执行的任务
     * @return Future对象
     */
    public static <R> Future<R> submit(Callable<R> task){
    	return executorService.submit(task);
    }
    
    
    /**
     * 异步执行一个指定的任务
     * @param task 需要执行的任务
     * @param t 传递给任务的参数列表(可变参数,没有参数可以不传)
     * @return Future对象
     */
    public static <T,R> Future<R> submit(Executable<T,R> task,T... t){
    	return executorService.submit(()->task.execute(t));
    }
    
    /**
     * 异步执行对象中的方法(可以是静态方法或私有方法)
     * @param target 需要异步执行的目标对象
     * @param method 需要异步调用的目标方法
     * @param args 传递给目标方法的参数列表
     * @return Future对象
     */
    public static <R> Future<R> submit(Object target,String method,Object... args){
		final Class[] typeArgs=new Class[null==args?0:args.length];
    	if(null!=args && 0!=args.length)for(int i=0;i<args.length;typeArgs[i]=args[i].getClass(),i++);
    	try {
	    	return executorService.submit(()->{
        		Class<?> targetType=null;
        		if(target instanceof Class){
        			targetType=(Class)target;
        		}else{
        			targetType=target.getClass();
        		}
        		Method targetMethod=findMethod(targetType,method,typeArgs);
        		if(null==targetMethod) return null;
        		targetMethod.setAccessible(true);
        		return (R)targetMethod.invoke(target, args);
	    	});
    	} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
    }
    
    /**
     * 异步执行一堆任务
     * 超时后所有未完成的任务将被取消(Cancel),已经完成的任务可能是正常结束,也可能是以抛出异常的方式结束,
     * 任务无论以哪一种方式结束其返回的结果集列表中的每一个Future的isDone都将返回True,通过本方法可以并发
     * 执行任务集中的所有任务
     * @param tasks 任务集
     * @param timeout 超时时间(可选,单位:毫秒,如果未给定则将等待所有任务完成为止)
     * @return Future列表
     */
    public static <R> List<Future<R>> submitAll(Collection<? extends Callable<R>> tasks,Long... timeout){
    	try {
    		if(null==timeout || 0==timeout.length) return executorService.invokeAll(tasks);
			return executorService.invokeAll(tasks, timeout[0], TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	return null;
    }
    
    /**
     * 异步执行一堆任务
     * 超时前只要有任意一个任务结束(成功结束或异常结束),则剩余的其它任务被取消(Cancel);超时后所有的任务被取消(Cancel),
     * 如果第一个完成的任务是异常返回或没有任何一个任务在超时前返回则本方法最终返回NULL,通过本方法可以筛选出任务集中执行
     * 速度最快的一个任务
     * @param tasks 任务集
     * @param timeout 超时时间(可选,单位:毫秒,如果未给定则将等待至少有一个任务返回为止)
     * @return 第一个结束任务的返回值
     */
    public static <R> R submitAny(Collection<? extends Callable<R>> tasks,Long... timeout){
		try {
			if(null==timeout || 0==timeout.length) return executorService.invokeAny(tasks);
			return executorService.invokeAny(tasks, timeout[0], TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
    	return null;
    }
    
    /**
     * 计划从调用本方法开始延迟给定时间后执行指定任务
     * @param task 需要计划执行的任务
     * @param delay 延迟时间(单位:毫秒)
     * @return ScheduledFuture对象
     */
    public static ScheduledFuture<?> schedule(Runnable task,Long delay){
    	return executorService.schedule(task, delay, TimeUnit.MILLISECONDS);
    }
    
    /**
     * 计划从调用本方法开始延迟给定时间后执行指定任务
     * @param task 需要计划执行的任务
     * @param delay 延迟时间(单位:毫秒)
     * @return ScheduledFuture对象
     */
    public static <R> ScheduledFuture<R> schedule(Callable<R> task,Long delay){
    	return executorService.schedule(task, delay, TimeUnit.MILLISECONDS);
    }
    
    /**
     * 计划从调用本方法开始延迟给定时间后执行指定任务
     * @param task 需要计划执行的任务
     * @param delay 延迟时间(单位:毫秒)
     * @param t 传递给任务的参数列表(可变参数,没有参数可以不传)
     * @return ScheduledFuture对象
     */
    public static <T,R> ScheduledFuture<R> schedule(Executable<T,R> task,Long delay,T... t){
    	return executorService.schedule(()->task.execute(t), delay, TimeUnit.MILLISECONDS);
    }
    
    /**
     * 计划从调用本方法开始延迟给定时间后执行指定对象中的方法(可以是静态方法或私有方法)
     * 本方法可以异步执行给定对象中的方法,其异步行为涵盖了延迟时间
     * @param delay 延迟时间(单位:毫秒)
     * @param target 需要异步执行的目标对象
     * @param method 需要异步调用的目标方法
     * @param args 传递给目标方法的参数列表
     * @return ScheduledFuture对象
     */
    public static <R> ScheduledFuture<R> schedule(Long delay,Object target,String method,Object... args){
    	final Class[] typeArgs=new Class[null==args?0:args.length];
    	if(null!=args && 0!=args.length)for(int i=0;i<args.length;typeArgs[i]=args[i].getClass(),i++);
    	try {
	    	return executorService.schedule(()->{
	    		Class<?> targetType=null;
        		if(target instanceof Class){
        			targetType=(Class)target;
        		}else{
        			targetType=target.getClass();
        		}
        		Method targetMethod=findMethod(targetType,method,typeArgs);
        		if(null==targetMethod) return null;
        		targetMethod.setAccessible(true);
        		return (R)targetMethod.invoke(target, args);
	    	},delay, TimeUnit.MILLISECONDS);
    	} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
    }
    
    /**
     * 计划从调用本方法开始延迟给定时间后执行一次指定任务,后续以给定的周期循环执行指定任务
     * @param task 需要计划执行的任务
     * @param initialDelay 第一次执行任务之前的延迟时间(单位:毫秒)
     * @param period 上一次执行任务开始时间到下一次执行任务开始时间的时间间隔(单位:毫秒)
     * @return ScheduledFuture对象
     */
    public static ScheduledFuture<?> scheduleByPeriod(Runnable task,Long initialDelay,Long period){
    	try {
	    	return executorService.scheduleAtFixedRate(task,initialDelay,period,TimeUnit.MILLISECONDS);
    	} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
    }
    
    /**
     * 计划从调用本方法开始延迟给定时间后执行一次指定任务,后续以给定的周期循环执行指定任务
     * @param task 需要计划执行的任务
     * @param initialDelay 第一次执行任务之前的延迟时间(单位:毫秒)
     * @param period 上一次执行任务开始时间到下一次执行任务开始时间的时间间隔(单位:毫秒)
     * @return QueueFuture对象
     */
    public static <R> QueueFuture<R> scheduleByPeriod(Callable<R> task,Long initialDelay,Long period){
    	QueueFuture<R> queue=new QueueFuture<R>(SCHEDULED_QUEUE_CAPACITY);
    	executorService.scheduleAtFixedRate(()->{
    		try {
    			R r=task.call();
				if(null!=r)queue.put(r);
			} catch (Exception e) {
				e.printStackTrace();
			}
    	},initialDelay,period,TimeUnit.MILLISECONDS);
		return queue;
    }
    
    /**
     * 计划从调用本方法开始延迟给定时间后执行一次指定任务,后续以给定的周期循环执行指定任务
     * @param task 需要计划执行的任务
     * @param initialDelay 第一次执行任务之前的延迟时间(单位:毫秒)
     * @param period 上一次执行任务开始时间到下一次执行任务开始时间的时间间隔(单位:毫秒)
     * @param t 传递给任务的参数列表(可变参数,没有参数可以不传)
     * @return QueueFuture对象
     */
    public static <T,R> QueueFuture<R> scheduleByPeriod(Executable<T,R> task,Long initialDelay,Long period,T... t){
    	QueueFuture<R> queue=new QueueFuture<R>(SCHEDULED_QUEUE_CAPACITY);
    	executorService.scheduleAtFixedRate(()->{
    		try {
    			R r=task.execute(t);
				if(null!=r)queue.put(r);
			} catch (Exception e) {
				e.printStackTrace();
			}
    	},initialDelay,period,TimeUnit.MILLISECONDS);
		return queue;
    }
    
    /**
     * 计划从调用本方法开始延迟给定时间后执行一次指定任务,后续以给定的周期循环执行指定任务
     * @param task 需要计划执行的任务
     * @param initialDelay 第一次执行任务之前的延迟时间(单位:毫秒)
     * @param delay 上一次执行任务结束时间到下一次执行任务开始时间的时间间隔(单位:毫秒)
     * @return ScheduledFuture对象
     */
    public static ScheduledFuture<?> scheduleByDelay(Runnable task,Long initialDelay,Long delay){
    	try {
	    	return executorService.scheduleWithFixedDelay(task,initialDelay,delay,TimeUnit.MILLISECONDS);
    	} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
    }
    
    /**
     * 计划从调用本方法开始延迟给定时间后执行一次指定任务,后续以给定的周期循环执行指定任务
     * @param task 需要计划执行的任务
     * @param initialDelay 第一次执行任务之前的延迟时间(单位:毫秒)
     * @param delay 上一次执行任务结束时间到下一次执行任务开始时间的时间间隔(单位:毫秒)
     * @return QueueFuture对象
     */
    public static <R> QueueFuture<R> scheduleByDelay(Callable<R> task,Long initialDelay,Long delay){
    	QueueFuture<R> queue=new QueueFuture<R>(SCHEDULED_QUEUE_CAPACITY);
    	try {
    		executorService.scheduleWithFixedDelay(()->{
	    		try {
	    			R r=task.call();
					if(null!=r)queue.put(r);
				} catch (Exception e) {
					e.printStackTrace();
				}
	    	},initialDelay,delay,TimeUnit.MILLISECONDS);
    	} catch (Exception e) {
			e.printStackTrace();
		}
		return queue;
    }
    
    /**
     * 计划从调用本方法开始延迟给定时间后执行一次指定任务,后续以给定的周期循环执行指定任务
     * @param task 需要计划执行的任务
     * @param initialDelay 第一次执行任务之前的延迟时间(单位:毫秒)
     * @param delay 上一次执行任务结束时间到下一次执行任务开始时间的时间间隔(单位:毫秒)
     * @param t 传递给任务的参数列表(可变参数,没有参数可以不传)
     * @return QueueFuture对象
     */
    public static <T,R> QueueFuture<R> scheduleByDelay(Executable<T,R> task,Long initialDelay,Long delay,T... t){
    	QueueFuture<R> queue=new QueueFuture<R>(SCHEDULED_QUEUE_CAPACITY);
    	try {
    		executorService.scheduleWithFixedDelay(()->{
	    		try {
	    			R r=task.execute(t);
					if(null!=r)queue.put(r);
				} catch (Exception e) {
					e.printStackTrace();
				}
	    	},initialDelay,delay,TimeUnit.MILLISECONDS);
    	} catch (Exception e) {
			e.printStackTrace();
		}
		return queue;
    }
    
    /**
     * 计划从调用本方法开始延迟给定时间后执行一次指定任务,后续以给定的周期循环执行指定对象中的方法
     * @param startTime 第一次执行任务的开始时间点(格式:"yyyy-MM-dd hh:mi:ss S")
     * @param period 上一次执行任务开始时间到下一次执行任务开始时间的时间间隔(单位:毫秒)
     * @param target 需要异步执行的目标对象
     * @param method 需要异步调用的目标方法
     * @param args 传递给目标方法的参数列表
     * @return QueueFuture对象
     */
    public static ScheduledFuture<?> scheduleByPeriod(String startTime,Long period,Object target,String method,Object... args){
    	Calendar now=Calendar.getInstance();
		Calendar future=Calendar.getInstance();
		Long futureTime=null;
		try {
			futureTime = new SimpleDateFormat(DatePatternConsts.NORM_DATETIME_MS_PATTERN).parse(startTime).getTime();
		} catch (ParseException e1) {
			e1.printStackTrace();
			throw new RuntimeException("startTime format is error...");
		}
		
		future.setTimeInMillis(futureTime);
		if(future.before(now)) throw new RuntimeException("startTime must be great than current time...");
		
		Long initialDelay=futureTime-now.getTimeInMillis();
		return scheduleByPeriod(initialDelay,period,target,method,args);
    }
    
    /**
     * 计划从调用本方法开始延迟给定时间后执行一次指定任务,后续以给定的周期循环执行指定对象中的方法
     * @param startTime 第一次执行任务的开始时间点
     * @param period 上一次执行任务开始时间到下一次执行任务开始时间的时间间隔(单位:毫秒)
     * @param target 需要异步执行的目标对象
     * @param method 需要异步调用的目标方法
     * @param args 传递给目标方法的参数列表
     * @return QueueFuture对象
     */
    public static ScheduledFuture<?> scheduleByPeriod(Date startTime,Long period,Object target,String method,Object... args){
    	Calendar now=Calendar.getInstance();
		Calendar future=Calendar.getInstance();
		Long futureTime = startTime.getTime();
		
		future.setTimeInMillis(futureTime);
		if(future.before(now)) throw new RuntimeException("startTime must be great than current time...");
		
		Long initialDelay=futureTime-now.getTimeInMillis();
		return scheduleByPeriod(initialDelay,period,target,method,args);
    }
    
    /**
     * 计划从调用本方法开始延迟给定时间后执行一次指定任务,后续以给定的周期循环执行指定对象中的方法
     * @param initialDelay 第一次执行任务之前的延迟时间(单位:毫秒)
     * @param period 上一次执行任务开始时间到下一次执行任务开始时间的时间间隔(单位:毫秒)
     * @param target 需要异步执行的目标对象
     * @param method 需要异步调用的目标方法
     * @param args 传递给目标方法的参数列表
     * @return QueueFuture对象
     */
    public static ScheduledFuture<?> scheduleByPeriod(Long initialDelay,Long period,Object target,String method,Object... args){
    	final Class[] typeArgs=new Class[null==args?0:args.length];
    	if(null!=args && 0!=args.length)for(int i=0;i<args.length;typeArgs[i]=args[i].getClass(),i++);
    	
    	return executorService.scheduleAtFixedRate(()->{
    		try {
    			Class<?> targetType=null;
        		if(target instanceof Class){
        			targetType=(Class)target;
        		}else{
        			targetType=target.getClass();
        		}
        		Method targetMethod=findMethod(targetType,method,typeArgs);
        		if(null==targetMethod) return;
	    		targetMethod.setAccessible(true);
	    		targetMethod.invoke(target, args);
	    	} catch (Exception e) {
				e.printStackTrace();
			}
    	}, initialDelay, period, TimeUnit.MILLISECONDS);
    }
    
    /**
     * 计划从调用本方法开始延迟给定时间后执行一次指定任务,后续以给定的周期循环执行指定对象中的方法
     * @param startTime 第一次执行任务的开始时间点(格式:"yyyy-MM-dd hh:mi:ss S")
     * @param period 上一次执行任务开始时间到下一次执行任务开始时间的时间间隔(单位:毫秒)
     * @param target 需要异步执行的目标对象
     * @param method 需要异步调用的目标方法
     * @param returnType 异步调用目标方法的返回类型
     * @param args 传递给目标方法的参数列表
     * @return QueueFuture对象
     */
    public static <R> QueueFuture<R> scheduleByPeriod(String startTime,Long period,Object target,String method,Class<R> returnType,Object... args){
    	Calendar now=Calendar.getInstance();
		Calendar future=Calendar.getInstance();
		Long futureTime=null;
		try {
			futureTime = new SimpleDateFormat(DatePatternConsts.NORM_DATETIME_MS_PATTERN).parse(startTime).getTime();
		} catch (ParseException e1) {
			e1.printStackTrace();
			throw new RuntimeException("startTime format is error...");
		}
		
		future.setTimeInMillis(futureTime);
		if(future.before(now)) throw new RuntimeException("startTime must be great than current time...");
		
		Long initialDelay=futureTime-now.getTimeInMillis();
		return scheduleByPeriod(initialDelay,period,target,method,returnType,args);
    }
    
    /**
     * 计划从调用本方法开始延迟给定时间后执行一次指定任务,后续以给定的周期循环执行指定对象中的方法
     * @param startTime 第一次执行任务的开始时间点
     * @param period 上一次执行任务开始时间到下一次执行任务开始时间的时间间隔(单位:毫秒)
     * @param target 需要异步执行的目标对象
     * @param method 需要异步调用的目标方法
     * @param returnType 异步调用目标方法的返回类型
     * @param args 传递给目标方法的参数列表
     * @return QueueFuture对象
     */
    public static <R> QueueFuture<R> scheduleByPeriod(Date startTime,Long period,Object target,String method,Class<R> returnType,Object... args){
    	Calendar now=Calendar.getInstance();
		Calendar future=Calendar.getInstance();
		Long futureTime = startTime.getTime();
		
		future.setTimeInMillis(futureTime);
		if(future.before(now)) throw new RuntimeException("startTime must be great than current time...");
		
		Long initialDelay=futureTime-now.getTimeInMillis();
		return scheduleByPeriod(initialDelay,period,target,method,returnType,args);
    }
    
    /**
     * 计划从调用本方法开始延迟给定时间后执行一次指定任务,后续以给定的周期循环执行指定对象中的方法
     * @param initialDelay 第一次执行任务之前的延迟时间(单位:毫秒)
     * @param period 上一次执行任务开始时间到下一次执行任务开始时间的时间间隔(单位:毫秒)
     * @param target 需要异步执行的目标对象
     * @param method 需要异步调用的目标方法
     * @param returnType 异步调用目标方法的返回类型
     * @param args 传递给目标方法的参数列表
     * @return QueueFuture对象
     */
    public static <R> QueueFuture<R> scheduleByPeriod(Long initialDelay,Long period,Object target,String method,Class<R> returnType,Object... args){
    	final Class[] typeArgs=new Class[null==args?0:args.length];
    	if(null!=args && 0!=args.length)for(int i=0;i<args.length;typeArgs[i]=args[i].getClass(),i++);
    	
    	QueueFuture<R> queue=new QueueFuture<R>(SCHEDULED_QUEUE_CAPACITY);
    	executorService.scheduleAtFixedRate(()->{
    		try {
    			Class<?> targetType=null;
        		if(target instanceof Class){
        			targetType=(Class)target;
        		}else{
        			targetType=target.getClass();
        		}
        		Method targetMethod=findMethod(targetType,method,typeArgs);
        		if(null==targetMethod) return;
	    		targetMethod.setAccessible(true);
	    		Object object=targetMethod.invoke(target, args);
	    		if(null!=object)queue.put(returnType.cast(object));
	    	} catch (Exception e) {
				e.printStackTrace();
			}
    	}, initialDelay, period, TimeUnit.MILLISECONDS);
    	
    	return queue;
    }
    
    /**
     * 计划从调用本方法开始延迟给定时间后执行一次指定任务,后续以给定的周期循环执行指定任务
     * @param startTime  第一次执行任务的开始时间点(格式:"yyyy-MM-dd hh:mi:ss S")
     * @param delay 上一次执行任务结束时间到下一次执行任务开始时间的时间间隔(单位:毫秒)
     * @param target 需要异步执行的目标对象
     * @param method 需要异步调用的目标方法
     * @param args 传递给目标方法的参数列表
     * @return QueueFuture对象
     */
    public static ScheduledFuture<?> scheduleByDelay(String startTime,Long delay,Object target,String method,Object... args){
    	Calendar now=Calendar.getInstance();
		Calendar future=Calendar.getInstance();
		Long futureTime=null;
		try {
			futureTime = new SimpleDateFormat(DatePatternConsts.NORM_DATETIME_MS_PATTERN).parse(startTime).getTime();
		} catch (ParseException e1) {
			e1.printStackTrace();
			throw new RuntimeException("startTime format is error...");
		}
		
		future.setTimeInMillis(futureTime);
		if(future.before(now)) throw new RuntimeException("startTime must be great than current time...");
		
		Long initialDelay=futureTime-now.getTimeInMillis();
		return scheduleByDelay(initialDelay,delay,target,method,args);
    }
    
    /**
     * 计划从调用本方法开始延迟给定时间后执行一次指定任务,后续以给定的周期循环执行指定任务
     * @param startTime  第一次执行任务的开始时间点
     * @param delay 上一次执行任务结束时间到下一次执行任务开始时间的时间间隔(单位:毫秒)
     * @param target 需要异步执行的目标对象
     * @param method 需要异步调用的目标方法
     * @param args 传递给目标方法的参数列表
     * @return QueueFuture对象
     */
    public static ScheduledFuture<?> scheduleByDelay(Date startTime,Long delay,Object target,String method,Object... args){
    	Calendar now=Calendar.getInstance();
		Calendar future=Calendar.getInstance();
		Long futureTime = startTime.getTime();
		
		future.setTimeInMillis(futureTime);
		if(future.before(now)) throw new RuntimeException("startTime must be great than current time...");
		
		Long initialDelay=futureTime-now.getTimeInMillis();
		return scheduleByDelay(initialDelay,delay,target,method,args);
    }
    
    /**
     * 计划从调用本方法开始延迟给定时间后执行一次指定任务,后续以给定的周期循环执行指定任务
     * @param initialDelay  第一次执行任务之前的延迟时间(单位:毫秒)
     * @param delay 上一次执行任务结束时间到下一次执行任务开始时间的时间间隔(单位:毫秒)
     * @param target 需要异步执行的目标对象
     * @param method 需要异步调用的目标方法
     * @param args 传递给目标方法的参数列表
     * @return ScheduledFuture对象
     */
    public static ScheduledFuture<?> scheduleByDelay(Long initialDelay,Long delay,Object target,String method,Object... args){
    	final Class[] typeArgs=new Class[null==args?0:args.length];
    	if(null!=args && 0!=args.length)for(int i=0;i<args.length;typeArgs[i]=args[i].getClass(),i++);
    	
    	return executorService.scheduleWithFixedDelay(()->{
    		try {
    			Class<?> targetType=null;
        		if(target instanceof Class){
        			targetType=(Class)target;
        		}else{
        			targetType=target.getClass();
        		}
        		Method targetMethod=findMethod(targetType,method,typeArgs);
        		if(null==targetMethod) return;
	    		targetMethod.setAccessible(true);
	    		targetMethod.invoke(target, args);
	    	} catch (Exception e) {
				e.printStackTrace();
			}
    	}, initialDelay, delay, TimeUnit.MILLISECONDS);
    }
    
    /**
     * 计划从调用本方法开始延迟给定时间后执行一次指定任务,后续以给定的周期循环执行指定任务
     * @param startTime  第一次执行任务的开始时间点(格式:"yyyy-MM-dd hh:mi:ss S")
     * @param delay 上一次执行任务结束时间到下一次执行任务开始时间的时间间隔(单位:毫秒)
     * @param target 需要异步执行的目标对象
     * @param method 需要异步调用的目标方法
     * @param returnType 异步调用目标方法的返回类型
     * @param args 传递给目标方法的参数列表
     * @return QueueFuture对象
     */
    public static <R> QueueFuture<R> scheduleByDelay(String startTime,Long delay,Object target,String method,Class<R> returnType,Object... args){
    	Calendar now=Calendar.getInstance();
		Calendar future=Calendar.getInstance();
		Long futureTime=null;
		try {
			futureTime = new SimpleDateFormat(DatePatternConsts.NORM_DATETIME_MS_PATTERN).parse(startTime).getTime();
		} catch (ParseException e1) {
			e1.printStackTrace();
			throw new RuntimeException("startTime format is error...");
		}
		
		future.setTimeInMillis(futureTime);
		if(future.before(now)) throw new RuntimeException("startTime must be great than current time...");
		
		Long initialDelay=futureTime-now.getTimeInMillis();
		return scheduleByDelay(initialDelay,delay,target,method,returnType,args);
    }
    
    /**
     * 计划从调用本方法开始延迟给定时间后执行一次指定任务,后续以给定的周期循环执行指定任务
     * @param startTime  第一次执行任务的开始时间点
     * @param delay 上一次执行任务结束时间到下一次执行任务开始时间的时间间隔(单位:毫秒)
     * @param target 需要异步执行的目标对象
     * @param method 需要异步调用的目标方法
     * @param returnType 异步调用目标方法的返回类型
     * @param args 传递给目标方法的参数列表
     * @return QueueFuture对象
     */
    public static <R> QueueFuture<R> scheduleByDelay(Date startTime,Long delay,Object target,String method,Class<R> returnType,Object... args){
    	Calendar now=Calendar.getInstance();
		Calendar future=Calendar.getInstance();
		Long futureTime = startTime.getTime();
		
		future.setTimeInMillis(futureTime);
		if(future.before(now)) throw new RuntimeException("startTime must be great than current time...");
		
		Long initialDelay=futureTime-now.getTimeInMillis();
		return scheduleByDelay(initialDelay,delay,target,method,returnType,args);
    }
    
    /**
     * 计划从调用本方法开始延迟给定时间后执行一次指定任务,后续以给定的周期循环执行指定任务
     * @param initialDelay  第一次执行任务之前的延迟时间(单位:毫秒)
     * @param delay 上一次执行任务结束时间到下一次执行任务开始时间的时间间隔(单位:毫秒)
     * @param target 需要异步执行的目标对象
     * @param method 需要异步调用的目标方法
     * @param returnType 异步调用目标方法的返回类型
     * @param args 传递给目标方法的参数列表
     * @return QueueFuture对象
     */
    public static <R> QueueFuture<R> scheduleByDelay(Long initialDelay,Long delay,Object target,String method,Class<R> returnType,Object... args){
    	final Class[] typeArgs=new Class[null==args?0:args.length];
    	if(null!=args && 0!=args.length)for(int i=0;i<args.length;typeArgs[i]=args[i].getClass(),i++);
    	
    	QueueFuture<R> queue=new QueueFuture<R>(SCHEDULED_QUEUE_CAPACITY);
    	executorService.scheduleWithFixedDelay(()->{
    		try {
    			Class<?> targetType=null;
        		if(target instanceof Class){
        			targetType=(Class)target;
        		}else{
        			targetType=target.getClass();
        		}
        		Method targetMethod=findMethod(targetType,method,typeArgs);
        		if(null==targetMethod) return;
	    		targetMethod.setAccessible(true);
	    		Object object=targetMethod.invoke(target, args);
	    		if(null!=object) queue.put(returnType.cast(object));
	    	} catch (Exception e) {
				e.printStackTrace();
			}
    	}, initialDelay, delay, TimeUnit.MILLISECONDS);
    	
    	return queue;
    }
    
    /**
     * 将线程数据压入线程队列中去
     * @description 线程组与它绑定的队列之间的映射关系在应用进程生命周期中始终存在,但队列中的任务则会被消费者线程逐次消费减少
     * @param threadGroup 线程组名称(一个线程组对应一个生产者队列)
     * @param threadData 压入线程上下文的数据
     */
    public static void addToscheduleQueue(String threadGroup,Object threadData){
    	Long threadId=Thread.currentThread().getId();
    	try {
    		THREAD_RESULT.put(threadId, new QueueFuture());
    		QueueFuture<ThreadContext> queueFuture=THREAD_QUEUE.get(threadGroup);
    		if(null==queueFuture) THREAD_QUEUE.put(threadGroup, queueFuture=new QueueFuture<ThreadContext>(SCHEDULED_QUEUE_CAPACITY));
    		queueFuture.put(new ThreadContext(threadId,threadData));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * 从线程中获取调度执行后的结果
     * @param timeout 超时时间(单位:毫秒)
     * @return 调度执行后的结果
     */
    public static Object getScheduleQueueData(Long... timeout){
    	QueueFuture queueFuture=THREAD_RESULT.get(Thread.currentThread().getId());
    	if(null==timeout || 0==timeout.length) return queueFuture.get();
    	return queueFuture.get(timeout);
    }
    
    /**
     * 基于消费者队列线程的异步调用实现,本方法一般仅在初始化时对每一个线程组调用一次
     * @description 本方法是异步嵌套的,外层的异步线程用于防应用启动时发生主线程阻塞,内层异步线程是周期性的计划调度方式
     * @param threadGroup  线程组名称(一个线程组对应一个队列)
     * @param initialDelay  第一次执行任务之前的延迟时间(单位:毫秒)
     * @param delay 上一次执行任务结束时间到下一次执行任务开始时间的时间间隔(单位:毫秒)
     * @param asyncTarget 调用目标对象
     * @param asyncMethod 调用目标方法
     * @return 消费者出口队列
     */
    public static void scheduleAsyncConsumer(String threadGroup,Long initialDelay,Long delay,Object asyncTarget,String asyncMethod){
    	executorService.submit(()->{
    		QueueFuture<ThreadContext> tempQueue=null;
        	while(null==(tempQueue=THREAD_QUEUE.get(threadGroup))){
        		try {
    				Thread.sleep(2000L);
    			} catch (InterruptedException e) {
    				e.printStackTrace();
    			}
        	}
        	
        	QueueFuture<ThreadContext> queueFuture=tempQueue;
        	executorService.scheduleWithFixedDelay(()->{
        		ThreadContext threadContext=queueFuture.get(2000L);
        		Long threadId=threadContext.getThreadId();
        		QueueFuture resultFuture=THREAD_RESULT.get(threadId);
        		
        		try {
        			Class<?> targetType=null;
            		if(asyncTarget instanceof Class){
            			targetType=(Class)asyncTarget;
            		}else{
            			targetType=asyncTarget.getClass();
            		}
            		
            		Object argsObject=threadContext.getData();
            		final Class[] asyncTypeArgs=new Class[]{argsObject.getClass()};
            		Method targetMethod=findMethod(targetType,asyncMethod,asyncTypeArgs);
            		if(null==targetMethod) {
            			resultFuture.put(null);
            			return;
            		}
            		
            		targetMethod.setAccessible(true);
            		resultFuture.put(targetMethod.invoke(asyncTarget, argsObject));
    	    	} catch (Exception e) {
    				e.printStackTrace();
    			}
        	}, initialDelay, delay, TimeUnit.MILLISECONDS);
    	});
    }
    
    /**
     * 查找线程对象
     * @param threadId 线程ID标识
     * @return 线程对象
     */
    public static Thread findThread(Long threadId) {
    	double xs=1.5;
        ThreadGroup group = Thread.currentThread().getThreadGroup();
        while(null!=group) {
            Thread[] threads = new Thread[(int)(group.activeCount() * xs)];
            int count = group.enumerate(threads, true);
            if(count>=threads.length) {
            	xs+=0.5;
            	continue;
            }
            for(int i=0; i<count; i++) if(threadId == threads[i].getId()) return threads[i];
            group = group.getParent();
        }
        return null;
    }
    
    /**
     * 线程上下文
     * @author Louis
     */
    public static class ThreadContext{
    	/**
    	 * 线程数据
    	 */
    	private Object data;
    	
    	/**
    	 * 线程ID
    	 */
    	private Long threadId;
    	
    	/**
    	 * 线程名称
    	 */
    	private String threadName;
    	
    	public ThreadContext(){
    		this.threadId = Thread.currentThread().getId();
    	}
    	
    	public ThreadContext(Object data) {
			this.data = data;
			this.threadId = Thread.currentThread().getId();
		}
    	
    	public ThreadContext(String threadName,Object data) {
			this.data = data;
			this.threadName=threadName;
			this.threadId = Thread.currentThread().getId();
		}
    	
		public ThreadContext(Long threadId, Object data) {
			this.data = data;
			this.threadId = threadId;
		}
		
		public ThreadContext(Long threadId, String threadName,Object data) {
			this.data = data;
			this.threadId = threadId;
			this.threadName=threadName;
		}
		
		public Object getData() {
			return data;
		}

		public Long getThreadId() {
			return threadId;
		}

		public String getThreadName() {
			return threadName;
		}
    }
    
    /**
     * @author Louis
     * @param <R>
     * 定时任务队列
     */
    @SuppressWarnings("serial")
	public static class QueueFuture<R> extends LinkedBlockingQueue<R>{
    	/**
    	 * 构造方法
    	 */
    	public QueueFuture(){}
    	
    	/**
    	 * 构造方法
    	 * @param capacity 队列尺寸
    	 */
    	public QueueFuture(Integer capacity){
    		super(capacity);
    	}
    	
    	/**
    	 * 返回队列中元素数量
    	 * @return 元素数量
    	 */
    	public int length(){
    		return super.size();
    	}
    	
    	/**
    	 * 阻塞直到队列中有数据时返回并移除队首元素
    	 * @return 泛化类型
    	 */
    	public R get(){
    		try {
				return super.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    		return null;
    	}
    	
    	/**
    	 * 从队首开始返回并移除队列中给定数量的元素 
    	 * 如果没有指定数量则返回队列中所有元素,如果队列中没有元素则返回一个空列表
    	 * @param maxNumber 需要获取的元素数量(可选)
    	 * @return 元素集合
    	 */
    	public List<R> getAll(Integer... maxNumber){
    		List<R> resultList=new ArrayList<R>();
    		if(null==maxNumber || 0==maxNumber.length) {
				super.drainTo(resultList);
			}else{
				super.drainTo(resultList,maxNumber[0]);
			}
			return resultList;
    	}
    	
    	/**
    	 * 等待给定的时间内返回并移除队列中队首元素,如果超时则返回NULL
    	 * @param timeout 超时时间(单位:毫秒)
    	 * @return 泛化类型
    	 */
    	public R get(Long... timeout){
    		try {
    			if(null==timeout || 0==timeout.length) return super.poll();
				return super.poll(timeout[0], TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    		return null;
    	}
    }
    
    /**
     * @author Louis
     * @param <T> 入参类型
     * @param <R> 出值类型
     * 
     * 可执行接口
     * 该接口是对Runnable接口和Callable接口的参数扩展
     */
    public interface Executable<T,R>{
    	/**
    	 * 任务执行方法
    	 * @param t 参数对象
    	 * @return 出值对象
    	 */
    	public R execute(T... t);
    }
}
