package com.assessment.helper;

/**
 * A thread safe class used at the time of producing(Transactions) and consuming(Statistics) apis.
 * 
 * @author Aman
 *
 */
public class ValueHolderOfEachMinute {

	private ValueHolderOfEachMinute() {
		// TODO Auto-generated constructor stub
	}
	
	
	static ValueHolderOfEachMinute valueHolderOfEachMinute=null;
	
	public static ValueHolderOfEachMinute getInstanse(){
		
		if(valueHolderOfEachMinute==null){
			synchronized (ValueHolderOfEachMinute.class){
				if(valueHolderOfEachMinute==null) valueHolderOfEachMinute= new ValueHolderOfEachMinute();
			}
		}
		return valueHolderOfEachMinute;
	}
	
	volatile double[] ammountArray;
	volatile int count;
	volatile int sum;
	public int getSum() {
		return sum;
	}
	public void setSum(int sum) {
		this.sum = sum;
	}


	volatile int avg;

	
	public int getAvg() {
		return avg;
	}
	public void setAvg(int avg) {
		this.avg = avg;
	}
	public double[] getAmmountArray() {
		return ammountArray;
	}
	public void setAmmountArray(double[] ammountArray) {
		this.ammountArray = ammountArray;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	
}
