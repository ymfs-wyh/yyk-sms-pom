package com.yyk333.upacp.pay.utils;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 唯一主键生成器，生成指定规则的字符串：
 * 		前缀+间隔符+时间字符串+间隔符+流水号
 * @author weiyanhao
 *
 */
public class IdGenerator implements Runnable {

	/**
	 * 流水号
	 */
	private AtomicInteger value;
	
	/**
	 * 时间字符串
	 */
	private String dateTime;
	
	/**
	 * 间隔符
	 */
	private static final String SPLIT_STRING = "";
	
	/**
	 * 前缀
	 */
	private static final String PREFIX_STRING = "";
	
	/**
	 * 流水号初始值
	 */
	private static final int ROLLING_INTERVAL = 1000;
	
	
	private static final int INITIAL_NUM = 1000;

	private Thread thread;

	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	public IdGenerator() {
		dateTime = DateUtil.getDate();
		value = new AtomicInteger(this.getInitial());

		thread = new Thread(this);
		thread.setDaemon(true);
		thread.start();
	}
	
	private static final IdGenerator ID_GENERATOR = new IdGenerator();  
    
    public static IdGenerator getInstance() {  
        return ID_GENERATOR;  
    }

	public void run() {
		while (true) {
			try {
				Thread.sleep(getRollingInterval());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			String now = DateUtil.getDate();
			if (!now.equals(dateTime)) {
				lock.writeLock().lock();
				dateTime = now;
				value.set(this.getInitial());
				lock.writeLock().unlock();
			}
		}
	}

	public String nextId() {
		lock.readLock().lock();
		StringBuffer sb = new StringBuffer(this.getPrefix()).append(this.getSplitString()).append(dateTime)
				.append(this.getSplitString()).append(value.getAndIncrement());
		lock.readLock().unlock();
		return sb.toString();
	}

	public String getSplitString() {
		return SPLIT_STRING;
	}

	public int getInitial() {
		return INITIAL_NUM;
	}

	public String getPrefix() {
		return PREFIX_STRING;
	}

	public int getRollingInterval() {
		return ROLLING_INTERVAL;
	}
	
	public static String getId() {
		return ID_GENERATOR.nextId();
	}
	
}
