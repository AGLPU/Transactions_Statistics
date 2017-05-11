package com.assessment.controller;

/*
Count hits in last minute 
Implemented by circular array
*/
import java.util.Arrays;
import java.util.Random;

import com.assessment.helper.ValueHolderOfEachMinute;


/**
 *  A Class having circuler array of buffer size 60 to contains amount values of last 60 seconds
 * @author Aman
 *
 */
public class HitCounter {

	private static int SIZE = 60;// 60 for last minute
	
	// An array for count request
	private int[] A = null;
	
	// An array to process amount data
	private double[] A1 = null;

	private int index = 0;
	
	private long startTime = 0;
	
	private int lastMinuteCount = 0;

	public HitCounter() {
	
		this.A = new int[SIZE];
		
		this.A1 = new double[SIZE];

		this.index = 0;
		// Counter start time, index start from 0
		this.startTime = (long) (System.currentTimeMillis() / 1E3);
		this.lastMinuteCount = 0;
	}

	public void hit(Double amt) {
		int lastIndex = index;// record last index
		this.index = getIndex();// get current index
		if (lastIndex == index) {// still in this slot
			A[index] += 1;// increase current second count by 1
			A1[index] += amt;// increase current second count by 1

			this.lastMinuteCount += 1;// increase last minute count
		}

		else {
			for (int i = (lastIndex + 1) % SIZE; i <= this.index; i++) {
				this.lastMinuteCount -= A[i];// for index, i is 60 seconds ago
				A[i] = 0;
				A1[i] = 0;// first hit in this second

			}
			A[this.index] = 1;// first hit in this second
			A1[this.index] = amt;// first hit in this second

			this.lastMinuteCount += 1;// update lastMinute
		}
		int sum = 0;
		for (int i = 0; i < A1.length; i++)
			sum += A1[i];

		// setting all values to Statistic api
		ValueHolderOfEachMinute.getInstanse().setAmmountArray(A1);
		ValueHolderOfEachMinute.getInstanse().setCount(lastMinuteCount);
		ValueHolderOfEachMinute.getInstanse().setSum(sum);
	}

	public int lastMinuteCount() {
		return lastMinuteCount;
	}

	// Calculate the index in circular array based on the current time
	public int getIndex() {
		long curTime = (long) (System.currentTimeMillis() / 1E3);
		secondLasttimeElipse = timeElipse;

		timeElipse = (int) (curTime - startTime);

		System.out.println("timeElipse = " + timeElipse + ", ");
		return (timeElipse % SIZE);
	}

	int timeElipse = 0;
	int secondLasttimeElipse = 0;

	public int getElapseTime() {
		return this.timeElipse;
	}

	public int getSecondLastElapseTime() {
		return this.secondLasttimeElipse;
	}

	public void resetAllValuesifNoLast60SecRequest() {
		this.A = new int[SIZE];
		this.A1 = new double[SIZE];

		this.index = 0;
		// Counter start time, index start from 0
		this.startTime = (long) (System.currentTimeMillis() / 1E3);
		this.lastMinuteCount = 0;
	}

}
