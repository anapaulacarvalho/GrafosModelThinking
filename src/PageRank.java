
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PageRank {

    //Método que cria lista com os vertices do grafo
    public static Grafo obtemVertices(List<Item> itens) {
        Vertice temp;
        List<Vertice> vertices = new ArrayList<>();
        for (Item item : itens) {
            temp = new Vertice(item.getOrigem());
            if (vertices.indexOf(temp) == -1) {
                vertices.add(temp);
            }
            temp = new Vertice(item.getDestino());
            if (vertices.indexOf(temp) == -1) {
               vertices.add(temp);
            }
        }

        ComparadorNome comparador = new ComparadorNome();
        Collections.sort(vertices, comparador);
        Grafo grafo = new Grafo(vertices.size());
        grafo.setVertices(vertices);
        return grafo;
        /*for(Vertice v: grafo.getVertices()){
            System.out.println(v.getNome() + " "+ v.getScore());
        }*/
    }

    public static void criaMatrizAdjacencia(List<Item> itens, Grafo grafo) {
        int posO, posD;
        List<Vertice> vertices = grafo.getVertices();

        Vertice temp;
        for (Item item : itens) {
            temp = new Vertice(item.getOrigem());
            posO = vertices.indexOf(temp);
            temp = new Vertice(item.getDestino());
            posD = vertices.indexOf(temp);
            if (posO == -1 || posD == -1) {
                System.out.println("Erro: " + item.getOrigem() + " " + item.getDestino());
            }
            grafo.getMatrizAdj()[posO][posD] = item.getPeso();
        }
    }

    public static int[] getVetorOutdegree(Grafo grafo) {
        /* Gera, para cada vertice, o seu grau de saida
    (ou seja, o numero de arestas adjacentes).
    Armazena todos no vetor out_degree.
    Em que out_degree[v] e o grau de saida do vertice v. */
        int[] outDegree = new int[grafo.getTamanho()];
        int saida = 0;
        for (int i = 0; i < grafo.getTamanho(); i++) {
            for (int j = 0; j < grafo.getTamanho(); j++) {
                if (grafo.getMatrizAdj()[i][j] > 0) {
                    saida++; //conta quantas ocorrencias saidas do vertices
                }
            }
            outDegree[i] = saida;
            saida = 0;
        }
        return outDegree;
    }

    /*  Calcula a partir do grafo o page rank do vertice passado como parametro.
    Tambem sÃo parametros:
    page_rank[u]: o PageRank (anterior) do vertice u.
    out_degree[u]: o grau de saida do vertice u.
    dumping_factor: dumping factor a ser usado. */
    public static double calculaPageRankVertice(Grafo grafo, List<Double> pr, int[] out_degree, int vertice, double dumpingFactor) {
        int i, j;
        double pageRank = 0;

        for (i = 0; i < grafo.getTamanho(); i++) {
            if (grafo.getMatrizAdj()[i][vertice] > 0) { //existe aresta i --> vertice       
                int wij = 0;
                for (j = 0; j < grafo.getTamanho(); j++) { //somatorio dos pesos (pega todos as saidas do vertice i)
                    if (grafo.getMatrizAdj()[i][j] > 0) {
                        wij += grafo.getMatrizAdj()[i][j];
                    }
                }
                /*Soma o pagerank de cada vertice e divide pelo somatorio dos pesos */
                pageRank += (grafo.getMatrizAdj()[i][vertice] * pr.get(i)) / wij;

            }
        }

        pageRank = pageRank * dumpingFactor + (1 - dumpingFactor);
        /*Multiplica a soma encontrada pelo dumping factor*/
        return pageRank;
    }

    public static List<Double> normalizaVetor(List<Double> vetor, int numeroVertices) {
        /* Funcao que ira normalizar o vetor passado */
        double soma = 0;
        int i;
        //somatorio do conteudo do vetor
        for (i = 0; i < numeroVertices; i++) {
            soma += vetor.get(i);
        }
        //divide o conteudo de cada posicao pelo somatorio
        for (i = 0; i < numeroVertices; i++) {
            vetor.set(i, vetor.get(i) / soma);
        }
        return vetor;
    }

    public static void calculaPageRank(Grafo grafo, double dumpingFactor) {
        /* Funcao que ira calcular o Page Rank de todos os vertices
    com a partir do CalculaPageRankVertice e ira normalizar os
    resultados ate atingirem a condicao de parada somaDifPR <=0.1 */
        List<Double> vetorPR = new ArrayList<>();
        List<Double> vetorPR_atual = new ArrayList<>();

        int[] outDegree = getVetorOutdegree(grafo);

        for (int i = 0; i < grafo.getTamanho(); i++) {
            vetorPR.add(1 - dumpingFactor); //iniciliza
            vetorPR_atual.add(0.0);
            
        }

        double somaDifPR = 0;

        do {

            for (int vertice = 0; vertice < grafo.getTamanho(); vertice++) { //para cada vertice associado a linha analisada..
                vetorPR_atual.set(vertice, calculaPageRankVertice(grafo, vetorPR, outDegree, vertice, dumpingFactor));
                

            }

            vetorPR_atual = normalizaVetor(vetorPR_atual, grafo.getTamanho());

            somaDifPR = 0;
            for (int vertice = 0; vertice < grafo.getTamanho(); vertice++) {
                somaDifPR += Math.abs(vetorPR_atual.get(vertice) - vetorPR.get(vertice));      
                vetorPR.set(vertice, vetorPR_atual.get(vertice));
            }
        } while (somaDifPR >= 0.0001);

        System.out.println("Tamanho"+grafo.getTamanho() + " "+vetorPR.size() + " "+grafo.getVertices().size());
        for (int i = 0; i < grafo.getTamanho(); i++) {
            grafo.getVertices().get(i).setScore(vetorPR.get(i));
        }

    }

    public static void imprimeTopKPageRank(Grafo grafo, int k) {

        int i;
        ComparadorScore comparador = new ComparadorScore();
        Collections.sort(grafo.getVertices(), comparador);
        for (i = 0; i < k && i < grafo.getTamanho(); i++) {
            System.out.println("Posicao " + (i + 1) + " Vertice " + grafo.getVertices().get(i).getNome() + " - Score: " + grafo.getVertices().get(i).getScore());
        }
    }

}
