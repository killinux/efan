package com.efan.common;

import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractCronJob {
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	private AtomicBoolean isRunning = new AtomicBoolean(false);
	
	public void execute() {
		if(isRunning.compareAndSet(false, true)) {
			try {
				logger.info(getClass().getSimpleName() + " start running.");
				doExecute();
				logger.info(getClass().getSimpleName() + " end running.");
			} catch(Exception e) {
				logger.info(getClass().getSimpleName() + " running exception.", e);
			} finally {
				isRunning.set(false);
			}
		} else {
			logger.info(getClass().getSimpleName() + " is running.");
		}
	}
	
	abstract protected void doExecute();
}
