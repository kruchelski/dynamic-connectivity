/******************************************************************************
 *  Compilation:  javac UF.java
 *  Execution:    java UF < input.txt
 *  Dependencies: StdIn.java StdOut.java
 *  Data files:   https://algs4.cs.princeton.edu/15uf/tinyUF.txt
 *                https://algs4.cs.princeton.edu/15uf/mediumUF.txt
 *                https://algs4.cs.princeton.edu/15uf/largeUF.txt
 *
 *  % java UF < tinyUF.txt
 *  4 3
 *  3 8
 *  6 5
 *  9 4
 *  2 1
 *  5 0
 *  7 2
 *  6 1
 *  2 components
 *
 ******************************************************************************/


/**
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 *
 *  Estrutura de dados II
 *  -----
 *  @author André Klingenfus Antunes  \\ implemented the
 *  @author Cassiano Kruchelski Vidal \\ missing funcitons
 *  @author Julio do Lago Muller      \\ of the alg
 */

public class UF2 {

    private int[] id;      // acessa o componente de um item
    private int count;     // number of components
    private int aux;

    // inicializa o array id de componentes
    public UF2(int n) {
        count = n;
        id = new int[n];
        for (int i = 0; i < n; i++) {
            id[i] = i;
        }
    }
    
    public int count() {
        return count;
    }
    
    public boolean connected(int p, int q) { 
        if (id[p] > id[q]) { // como os componentes tomam sempre o menor elemento como
            q = q ^ p;       // a referência de identificação do componente, deve sempre
            p = q ^ p;       // fazer verificações/modificações no menor elemento que está
            q = q ^ p;       // sendo enviado para a função, pois ele pode ser a referência
        }                    // e sofrer alterações que vão refletir no segundo elemento passado
        return find(p) == find(q);
    }

    public int find(int p){           // compara o valor que está no vetor na posição passada com
        if (id[p] != id[(id[p])]) {   // o valor do vetor na posição do menor elemento (que é a referência
            id[p] = this.find(id[p]); // do componente). Caso sejam diferentes, ele deve verificar a referência
        }                             // e esta se atualizar, para então, através de recursividade, atualizar
        return id[p];                 // os elementos que "apontavam" para a referência
    }

    public void union(int p, int q){
        if (id[p] < id[q]){           // compara o que está no vetor nas posições passadas para que
            id[(id[q])] = id[p];      // seja verificado qual é o menor, pois este tenderá a ser a 
            id[q] = id[p];            // referência do componente.

        } else {
            id[(id[p])] = id[q];
            id[p] = id[q];
        }
        count--;
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        int n = StdIn.readInt();
        UF2 uf = new UF2(n);
        while (!StdIn.isEmpty()) {
            int p = StdIn.readInt();
            int q = StdIn.readInt();
            if (uf.connected(p, q)) continue;
            uf.union(p, q);
            StdOut.println(p + " " + q);
        }
        StdOut.println(uf.count() + " components");
        long elapsed = System.currentTimeMillis() - start;
        StdOut.println("Tempo de execução: " + elapsed + "ms");
    }
}

