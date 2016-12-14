package crawl.test;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by tcf24 on 2016/11/26.
 */
public class TestMain {
    public static void main(String[] args) {
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

}
