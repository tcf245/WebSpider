package crawl.test;


import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by tcf24 on 2016/11/26.
 */
public class TestMain {
    private static Gson gson = new Gson();

    public static void main(String[] args) {
        TestMain test = new TestMain();
        int[] num1 = {0,1,2,3,4,5,6};
        int[] num2 = {8,9};
        test.merge(num1,num1.length,num2,num2.length);
        System.out.println(Arrays.stream(num1)
                .distinct()
                .mapToObj(Integer::toString)
                .collect(Collectors.joining(",")));
    }

    public void merge(int[] nums1, int m, int[] nums2, int n) {
        int left = 0;
        int right = 0;
        while(right <= n){
            if (right >= n)
                return;
            while(left >= m && right < n){
                nums1[left++] = nums2[right++];
                System.out.println(left+","+right);
            }
            while(nums1[left] > nums2[right]){
                swap(nums1[left++],nums2[right]);
            }
        }
    }
    public void swap(int a, int b){
        int temp = a;
        a = b;
        b = temp;
    }

    public static void listAllChar(){
        for (int i = -128; i < 128; i++) {
            char c = (char) i;
            System.out.println(i + " -> "  + c);
        }
    }

    public static String byteToBinaryString(byte b) {
        String s = Integer.toBinaryString(b);
        if (b < 0) {
            s = s.substring(24);
        } else {
            if (s.length() < 8) {
                int len = s.length();
                for (int i = 0; i < 8 - len; i++) {
                    s = "0" + s;
                }
            }
        }
        return s;
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

    public static void getPaire(int[] nums,int target){
        Map<Integer,Integer> res = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (res.get(nums[i]) != null){
                System.out.println(nums[i] + "\t" + (target - nums[i]));
            }
            res.put(target - nums[i],nums[i]);
        }

    }
}
