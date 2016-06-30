package com.meizu.simplify.mvc;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;

import com.meizu.simplify.mvc.controller.BaseController;
import com.meizu.simplify.mvc.controller.IBaseController;
import com.meizu.simplify.mvc.dto.ControllerAnnotationInfo;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月26日 下午5:14:43</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月26日 下午5:14:43</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class BinarySearchTest {

	@Test
	public void testKey() {
		Map<String, ControllerAnnotationInfo<?>> controllerMap = new ConcurrentHashMap<>();
		controllerMap.put("astest", new ControllerAnnotationInfo<IBaseController<?>>(new BaseController<>(), "testName"));
		controllerMap.put("tesdfst", new ControllerAnnotationInfo<>(new BaseController<>(), "testName"));
		controllerMap.put("twsdfest", new ControllerAnnotationInfo<>(new BaseController<>(), "testName"));
		controllerMap.put("tesxdt", new ControllerAnnotationInfo<>(new BaseController<>(), "testName"));
		controllerMap.put("tsxcest", new ControllerAnnotationInfo<>(new BaseController<>(), "testName"));
		controllerMap.put("tsesest", new ControllerAnnotationInfo<>(new BaseController<>(), "testName"));
		controllerMap.put("tesdst", new ControllerAnnotationInfo<>(new BaseController<>(), "testName"));
		controllerMap.put("tsdcest", new ControllerAnnotationInfo<>(new BaseController<>(), "testName"));
		controllerMap.put("texxcst", new ControllerAnnotationInfo<>(new BaseController<>(), "testName"));
		controllerMap.put("texxst", new ControllerAnnotationInfo<>(new BaseController<>(), "testName"));
		Set<String> sets = controllerMap.keySet();
		String[] setarr = new String[sets.size()];
		sets.toArray(setarr);
		for (String string : setarr) {
			System.out.print(strToInt(string)+",");
		}
		System.out.println();
		System.out.println(strToInt("tesxdt"));
		binarySearch(setarr,"tesxdt");
		
		
	}
	
	@Test
	public void test() {
	       long[] arr = new long[] { 7404788,5644038,7402938,75477038,75277038,758280038,74028138,7409938,75303038,74097838 };
	       System.out.println(search(arr, 7404788));
	       System.out.println(search(arr, 75477038));
	       System.out.println(search(arr, 74028138));
	       System.out.println(search(arr, 74097838));
	       System.out.println(search(arr, 99));
	   }

	public static int strToInt( String str ){
	    int i = 0;
	    int num = 0;
	    boolean isNeg = false;

	    //Check for negative sign; if it's there, set the isNeg flag
	    if (str.charAt(0) == '-') {
	        isNeg = true;
	        i = 1;
	    }

	    //Process each character of the string;
	    while( i < str.length()) {
	        num *= 10;
	        num += str.charAt(i++) - '0'; //Minus the ASCII code of '0' to get the value of the charAt(i++).
	    }

	    if (isNeg)
	        num = -num;
	    return num;
	}
	
	private void binarySearch(String[] arr, String value) {
		int start = 0;
	       int end = arr.length - 1;
	       while (start <= end) {
	           int middle = (start + end) / 2;
	           if (strToInt(value) < strToInt(arr[middle])) {
	               end = middle - 1;
	           } else if (strToInt(value) > strToInt(arr[middle])) {
	               start = middle + 1;
	           } else {
	        	   System.out.println(arr[middle]+"|"+value);
	           }
	       }
	}
	
	public void binarySearch2(String[] arr, String value) {
		int low = 0;
		int high = arr.length-1;
		while(low<=high) {
			int mid = (low+high) /2;
			
            if (value.compareTo(arr[mid])>0) {  
                low = mid + 1;  
            } else if (value.compareTo(arr[mid])<0) {  
                high = mid - 1;  
            }  else {
            	System.out.println(arr[mid]+"|"+value);
            }
		}
	}
	
	public static int search(long[] arr, long key) {
	       int start = 0;
	       int end = arr.length - 1;
	       while (start <= end) {
	           int middle = (start + end) / 2;
	           if (key < arr[middle]) {
	               end = middle - 1;
	           } else if (key > arr[middle]) {
	               start = middle + 1;
	           } else {
	               return middle;
	           }
	       }
	       return -1;
	}
	
}
