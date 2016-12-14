package spider;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by BFD_303 on 2016/12/13.
 */
public class Test {

    public static void main(String[] args) {
        int[] arr = {1,2,4,5,6,7,7,8,56,86,79,678,90,4,56,723,45,7,456,95,679,234,6,345,7,459,7};
        int[] result = new int[arr.length];
        mergeSort(arr,result,0,arr.length - 1);

        Arrays.stream(arr).forEach(i -> System.out.println(i));

    }


    /**
     * 归并排序
     * @param arr
     * @param result
     * @param start
     * @param end
     */
    public static void mergeSort(int[] arr,int[] result,int start,int end){
        if (start >= end) return;

        int len = end - start, mid = (len >> 1) + start;
        int start1 = start,end1 = mid;
        int start2 = mid + 1,end2 = end;
        mergeSort(arr,result,start1,end1);
        mergeSort(arr,result,start2,end2);

        int k = start;
        while(start1 <= end1 && start2 <= end2)
            result[k++] = arr[start1] < arr[start2] ? arr[start1++] : arr[start2++];
        while(start1 <= end1)
            result[k++] = arr[start1++];
        while(start2 <= end2)
            result[k++] = arr[start2++];
        for (k = start;k <= end;k++)
            arr[k] = result[k];
    }

    public int[] twoSum(int[] nums, int target) {
        HashMap<Integer,Integer> map = new HashMap<>();

        for (int i = 0; i < nums.length; i++) {
            if (map.get(nums[i]) != null) {
                int[] result = {map.get(nums[i]) + 1, i + 1};
                return result;
            }
            map.put(target - nums[i], i);
        }

        int[] result = {};
        return result;
    }

}
