package crawl.test;


import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by tcf24 on 2016/11/26.
 */
public class TestMain {
    private static Gson gson = new Gson();

    public static void main(String[] args) {
        Set<String> resultSet = new HashSet<String>();
        List<String> resultList = new ArrayList<>();
        try {
            List<String> lines = FileUtils.readLines(new File("etc/chanpin.txt"),"utf-8");
            lines.forEach(l -> {
                Map<String,Object> item = gson.fromJson(l,Map.class);
                List<String> items = (List<String>) item.get("items");
                items.forEach(i -> {
                    resultList.add(i);
                    resultSet.add(i);
                });
            });

            System.out.println(resultList.size());
            System.out.println(resultSet.size());
            FileUtils.writeLines(new File("etc/result.txt"),resultSet,"\n",true);

        } catch (IOException e) {
            e.printStackTrace();
        }

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
