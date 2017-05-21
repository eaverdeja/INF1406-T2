package com.company;

/**
 * Created by verdeja on 4/19/2017.
 */
class Multiplicador implements Runnable {
    final int size;
    double [][] a;
    double [][] b;
    double [][] result;
    final int i;
    final int j;

    public Multiplicador(int size, int i, int j, double[][] a, double[][] b, double[][] result) {
        this.size = size;
        this.i = i;
        this.j = j;
        this.a = a;
        this.b = b;
        this.result = result;
    }

    @Override
    public void run() {
        for(int k = 0; k < size; k++) {
            System.out.println("Thread[" + i + "," + j + "] Multiplicando " + a[i][k] + " por " + b[k][j]);
            result[i][j] += a[i][k] * b[k][j];
        }
    }
}
