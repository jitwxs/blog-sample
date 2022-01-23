package com.github.jitwxs.sample.grpc.pcbook;

public class Rating {
    private int count;
    private double sum;

    public Rating(int count, double sum) {
        this.count = count;
        this.sum = sum;
    }

    public int getCount() {
        return count;
    }

    public double getSum() {
        return sum;
    }

    public static Rating add(Rating r1, Rating r2) {
        return new Rating(r1.count + r2.count, r1.sum + r2.sum);
    }
}
