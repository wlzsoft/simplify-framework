package vip.simplify.utils.sort;
import java.util.Random;


/**
 * <p><b>Title:</b><i>快速排序算法-随机数版</i></p>
 * <p>Desc: TODO待测试和整理</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2017年2月24日 下午1:44:06</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2017年2月24日 下午1:44:06</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class QuickSortK {
	
	public static int[] getRandom(int k){
		  Random rnd = new Random();
		int data[] = new int[k];
		for(int i = 0; i < k;i++){
			data[i] = rnd.nextInt(k);
		}
		return data;
	}
	
	public static void main(String[] args) {
		//int data[] = { 23, 65, 20, 345, 61, 76, 51, 42, 76,10, 33, 2, 4, 4, 6, 12, 34, 456, 66, 41,60,69 };
		int data[] = getRandom(100000000);
		long l1 = System.currentTimeMillis();
		QuickSortK.go(data, 100);
		System.out.println((System.currentTimeMillis() - l1));
		//QuickSort.go(data);
		/*Arrays.sort(data);
		for(int i = data.length - 1; i>=0;i--){
			System.out.print(data[i] + " ");
		} 
		System.out.println();*/
	}
	public static void go(int[] data,int k){
		quickSort(data,0,data.length - 1,k,0);
		boolean b = false;
		for(int i = data.length - 1; i>=0 ;i--){
			
			if(!b && data.length - i > k){
				b = true;
			    System.out.println();
			    break;
			}
			System.out.print(data[i] + ",");
		} 
		System.out.println();
	}
	public static void quickSort(int[] data,int start,int end,int k,int level){
		
		int p = 0;
		if(start < end){
			p = partition(data,start,end);
			/*System.out.println(level +  "  ==== " + (end - p) + " " + k);
			for(int d : data){
				System.out.print(d + " ");
			}
			 System.out.println();*/
			int len = end - p;
			if(k < len){ 
				quickSort(data,p + 1,end,k,level+1);
			}
			if(k >= len){ 
				quickSort(data,start,p - 1,k - len,level+1);//小
				quickSort(data,p + 1,end,len,level+1);
			} 
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
	
	
}
