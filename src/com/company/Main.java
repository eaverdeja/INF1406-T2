package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        //Recuperando os parametros de entrada
        File file = new File(args[0]);
        int size = Integer.parseInt(args[1]);
        int maxThreads = Integer.parseInt(args[2]);
        int quantMatrizes = Integer.parseInt(args[3]);

        try {
            Scanner in = new Scanner(file);

            //Armazenamos o tempo de inicio do cálculo
            long startTime = System.currentTimeMillis();
            long endTime;

            //Recuperamos as duas primeiras matriz do arquivo
            double[][] a = construirMatriz(in, size);
            double[][] b = construirMatriz(in, size);
            //Inicializamos a matriz resultante
            double[][] result = new double[size][size];

            //Instanciamos a pool de threads
            ExecutorService pool = Executors.newFixedThreadPool(maxThreads);
            int k; int i; int j;

            Collection<Future<?>> futures = new LinkedList<>();

            //Enquanto houver matrizes no arquivo
            for(k = 2; k <= quantMatrizes; k++) {

                //Calculamos a matriz resultante
                System.out.println("Multiplicando A");
                dumpMatriz(a);
                System.out.println("Por B");
                dumpMatriz(b);
                System.out.println();

                //Para cada uma das celulas da matriz
                for (i = 0; i < size; i++) {
                    for (j = 0; j < size; j++) {
                        //Criamos uma nova thread na pull e adicionamos a lista de valores futuros
                        futures.add(pool.submit(new Multiplicador(size, i, j, a, b, result)));
                    }
                }

                //Esperamos pelas threads
                for (Future<?> future:futures) {
                    future.get();
                }

                //Atribuimos a matriz resultante à a
                for (i = 0; i < size; i++) {
                    for (j = 0; j < size; j++) {
                        a[i][j] = result[i][j];
                        result[i][j] = 0;
                    }
                }

                if(k == quantMatrizes) {
                    continue;
                }

                //Atribuimos uma nova matriz do arquivo para b
                b = construirMatriz(in, size);
                System.out.println();
            }

            pool.shutdown();
            pool.awaitTermination(1, TimeUnit.DAYS);

            endTime   = System.currentTimeMillis();
            long totalTime = endTime - startTime;

            System.out.println("Terminando o calculo da matriz resultante!");
            dumpMatriz(a);
            System.out.println("Tempo gasto: " + totalTime + "ms");

            //Imprimos o resultado no arquivo
            try{
                PrintWriter writer = new PrintWriter("resultado.txt", "UTF-8");
                for (i = 0; i < size; i++) {
                    for (j = 0; j < size; j++) {
                        writer.print((float)a[i][j]);
                        writer.print(" ");
                    }
                    writer.println();
                }
                writer.close();
            } catch (IOException e) {
                // do something
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static double[][] construirMatriz(Scanner in, int size) throws FileNotFoundException {

        double[][] matriz = new double[size][size];

        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                matriz[i][j] = in.nextDouble();
            }
        }

        return matriz;
    }

    public static void dumpMatriz(double[][] matriz) {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                System.out.print(matriz[i][j] + " ");
            }
            System.out.println();
        }
    }
}
