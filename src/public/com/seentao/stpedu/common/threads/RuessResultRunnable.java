package com.seentao.stpedu.common.threads;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.seentao.stpedu.common.components.factory.ClassLoaderBeanFactory;
import com.seentao.stpedu.common.entity.ArenaGuess;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.guess.service.QuizResultsLogic;
import com.seentao.stpedu.utils.LogUtil;

/**
 * 竞猜结果多线程处理
 * @author 	lw
 * @date	2016年7月4日  上午9:24:28
 *
 */
@Component
public class RuessResultRunnable implements Runnable {
	
	
	/**
	 * 竞猜结果处理容器：
	 * 需要传入竞猜处理对象
	 */
	private volatile List<ArenaGuess> guessList = new ArrayList<ArenaGuess>();
	
	//参数传递
	public void setGuess(final ArenaGuess guess) {
		this.guessList.add(guess);
	}

	//获取公布竞猜结果对象
	private synchronized ArenaGuess getGuess(){
		ArenaGuess arenaGuess = guessList.get(0);
		guessList.remove(0);
		return arenaGuess;
	}

	/**
	 * 等待时间执行结果
	 * 
	 * @author 			lw
	 * @date			2016年7月4日  上午9:26:53
	 */
	@Override
	public void run() {
		
		try {
			
			//	1. 线程睡眠一个小时
			Thread.sleep(GameConstants.GUESS_RESULT_TIME);
			
			//	2. 执行竞猜结果方法
			if(ClassLoaderBeanFactory.getBean(QuizResultsLogic.class).comeExecute(this.getGuess()).isSuccess()){
				LogUtil.info(this.getClass(), "RuessResultRunnable", GameConstants.SUCCESS_THREAD_RUN);
			}else{
				LogUtil.error(this.getClass(), "RuessResultRunnable", AppErrorCode.ERROR_QUIZRESULT_GUESS_AFTER_EXE);
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
			LogUtil.error(this.getClass(), "RuessResultRunnable", AppErrorCode.ERROR_QUIZRESULT_GUESS_AFTER_EXE, e);
		}
	}

}
