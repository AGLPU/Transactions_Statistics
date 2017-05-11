package com.assessment.controller;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.aop.aspectj.AspectJAdviceParameterNameDiscoverer.AmbiguousBindingException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.assessment.helper.ValueHolderOfEachMinute;
import com.assessment.model.Statistics;
import com.assessment.model.Transaction;

/**
 * 
 * @author Aman
 *
 *A controller class used to handle transaction and statistics apis results
 *Its calculate and process desired result in linear space and time complexity because we are just maintaining the circuler array and yes some variables.
 * 
 *
 */

@Controller
public class JSONController {

	private static final int SIZE = 60;

	long startTime;
	int count = 0;
	long lastTimeStampTransaction;
	long startTimestatistic = 0;

	HitCounter hitCountObj = new HitCounter();

	public JSONController() {

		this.startTime = (long) (System.currentTimeMillis() / 1E3);
	}

	/**
	 *  method produces random double values with time stamp and send values to hitCount
	 *  
	 * @return
	 */
	@RequestMapping(value = "/transactions", method = RequestMethod.GET)

	public @ResponseBody Transaction getTransactionAmount() {
		
		long curTime = (long) (System.currentTimeMillis() / 1E3);
		int timeElipse = (int) (curTime - startTime);
		lastTimeStampTransaction=curTime;
		//genrate random double nos. for amount
		double random = ThreadLocalRandom.current().nextDouble(100, 1000);

		DecimalFormat df = new DecimalFormat("#.00");
		double amount = Double.parseDouble(df.format(random));
		
		Transaction transaction = new Transaction();

		transaction.setAmount(amount);
		transaction.setTimestamp(curTime);

		// condition validates inputs lies in last 60 seconds 
		if ((timeElipse - SIZE) <= hitCountObj.getElapseTime())
			hitCountObj.hit(amount);
		else { // if not then ignore initial ones and recalculates the time from here for new validations
			this.startTime = (long) (System.currentTimeMillis() / 1E3);
			hitCountObj = new HitCounter();
			hitCountObj.hit(amount);
		}
		try {
			Thread.sleep(100);// thread sleep for 100 miliseconds
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return transaction;

	}

	/**
	 * Method return the desired json response if no request comes in last 60 seconds it returns all fields value zero
	 *  
	 * @return
	 */
	@RequestMapping(value = "/statistics", method = RequestMethod.GET)
	public @ResponseBody Statistics getStatisticDetailsOfLastMinute() {

		DecimalFormat df = new DecimalFormat("#.00");

		Statistics statistics = new Statistics();
		this.startTimestatistic = (long) (System.currentTimeMillis()/1E3);

		//validates that last b/w last T request and latest S request ith size buffer
		//basically handles if no transactions occurs last 60 seconds.
		  if((int)(this.startTimestatistic-this.lastTimeStampTransaction)<=60){
		/*
		 * lastTimeStampStatistic=this.startTimestatistic;
		 * this.startTimestatistic = (long) (System.currentTimeMillis()/1E3);
		 * int
		 * nowElapse=(int)(this.startTimestatistic-this.lastTimeStampStatistic);
		 */
		/*
		 * if no request comes in last SIZE seconds then do nothing
		 */

		//condition validates the last 2 time elapse during producing event if difference is < SIZE that means we are getting statistics of last 60 seconds window
		
		if ((hitCountObj.getElapseTime() - hitCountObj.getSecondLastElapseTime()) <= SIZE) {
		
			statistics.setCount(ValueHolderOfEachMinute.getInstanse().getCount());
			statistics.setSum(ValueHolderOfEachMinute.getInstanse().getSum());

			statistics.setAvg((int) (ValueHolderOfEachMinute.getInstanse().getSum()
					/ ValueHolderOfEachMinute.getInstanse().getCount()));
			double[] amountArr = ValueHolderOfEachMinute.getInstanse().getAmmountArray();
	
			Set<Double> set = new HashSet<Double>();

			for (int i = 0; i < amountArr.length; i++) {
				if (amountArr[i] != 0.0)
					set.add(amountArr[i]);
			}

			statistics.setMax(Collections.max(set).intValue());

			statistics.setMin(Collections.min(set).intValue());
		}
		  }
		return statistics;

	}

}