package crawl.test;


<<<<<<< HEAD
import java.util.Arrays;
=======
import java.util.HashMap;
import java.util.Map;
>>>>>>> 9e555d1b123705f2ea52a2f22e550ad6c12a6c8a

/**
 * Created by tcf24 on 2016/11/26.
 */
public class TestMain {
    public static void main(String[] args) {
<<<<<<< HEAD
        int[] arr = {1,64,557,86797,78,12,4,5,46,45,7,474,568,6,78,567,83,45,6,45,75,68,3,456,3,4573};

        int[] result = new int[arr.length];
        mergeSort(arr,result,0,arr.length - 1);

        Arrays.stream(arr).forEach(i -> System.out.println(i));
    }

    public static int reverse(int x) {
        int reversed_n = 0;

        while (x != 0) {
            int temp = reversed_n * 10 + x % 10;
            x = x / 10;
            if (temp / 10 != reversed_n) {
                reversed_n = 0;
                break;
            }
            reversed_n = temp;
        }
        return reversed_n;
    }


    public static void mergeSort(int[] arr,int[] result,int start,int end){
        if (start >= end) return;

        int len = end - start;
        int mid = (len >> 1) + start;

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

        for (k = start;k <= end; k++)
            arr[k] = result[k];

    }


=======
        int[] nums = {1,2,3,4,5,6,7,8,9,0};
        getPaire(nums,1);

    }


    public static void getPaire(int[] nums,int target){
        Map<Integer,Integer> res = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (res.get(nums[i]) != null){
                System.out.println(nums[i] + "\t" + (target - nums[i]));
            }
            res.put(target - nums[i],nums[i]);
        }

    }

>>>>>>> 9e555d1b123705f2ea52a2f22e550ad6c12a6c8a
}
