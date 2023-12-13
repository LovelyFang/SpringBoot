package com.tsxy.methods;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Liu_df
 * @Date 2023/11/20 9:42
 */
public class CompletableFutureMethod {

    private void delay(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private int randomNumber() {
//        Random random = new Random();
//        return random.nextInt(2000) + 500;
        return 1009;
    }

    public List<String> GetInspectList1() throws Exception {
        int i = randomNumber();
        if (i%3 == 0) {
            throw new Exception("写死异常! " + i);
        }
        delay(i);
        List<String> list = new ArrayList<>();
        list.add("This is 1");
        list.add("This is 2");
        list.add("This is 3");
        return list;
    }

    public List<String> GetInspectList2(){
        int i = randomNumber();
        delay(i);
        List<String> list = new ArrayList<>();
        list.add("That is 4");
        list.add("That is 5");
        list.add("That is 6");
        return list;
    }

    public void obtainResult(List<String> result, List<String> beProcessed) {
        if (beProcessed == null) {
            return;
        }
        synchronized (result) {
            if (result == null) {
                result = beProcessed;
            }
            result.addAll(beProcessed);
        }
    }



}
