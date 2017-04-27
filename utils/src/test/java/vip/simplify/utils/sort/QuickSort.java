package vip.simplify.utils.sort;

/**
 * <p><b>Title:</b><i>快速排序算法</i></p>
 * <p>Desc: TODO待测试和整理</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2017年2月24日 下午1:43:54</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2017年2月24日 下午1:43:54</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class QuickSort {
	
	public static void main(String[] args) {
		int data[] = { 10, 33, 2, 4, 55, 6, 12, 34, 456, 66, 43, 23, 65, 1, 345, 61, 76, 31, 43, 76 }; 
		quickSort(data,0,data.length - 1);
		for(int d : data){
			System.out.print(d + " ");
		}
		System.out.println();
	}
	
	public static void go(int[] data){
		quickSort(data,0,data.length - 1); 
		for(int i = data.length - 1; i>=0 && data.length - i <= 100;i--){
			System.out.print(data[i] + ",");
		} 
		System.out.println();
	}
	public static void quickSort(int[] data,int start,int end){
		
		int p = 0;
		if(start < end){
			p = partition(data,start,end);  
			//System.out.println(p);
			quickSort(data,start,p -1);
			quickSort(data,p+1,end);
		}
	}
	public static int partition(int[] data,int left,int right){
		int p = data[left];
		while(left < right){
			while(left < right && data[right] >= p){
				right--;
			}
			if(left < right) data[left++] = data[right];
			while(left < right && data[left] <= p){
				left++;
			}
			if(left < right)data[right--] = data[left]; 
			 
		}
		data[left] = p;
		return left;
	}
	public static void swap(int []data,int i,int j){
		int temp = data[i];
		data[i] = data[j];
		data[j] = temp;
	}
	
	
}
