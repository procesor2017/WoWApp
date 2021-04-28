package com.example.wowWeb.math;

import java.util.ArrayList;

public class MathModel {
    public long avgOfArrayList(ArrayList arrayList){
        long sum = 0;
        if(!arrayList.isEmpty()){
            for (Object n : arrayList){
                sum += (long) n;
            }
            return sum / arrayList.size();
        }
        return sum;
    }
}
