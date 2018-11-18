
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Principal {

    private static void calcularPesos(Map<Integer, Pergunta> colecao) {
        Pergunta pergunta;

        for (Map.Entry<Integer, Pergunta> p : colecao.entrySet()) {
            pergunta = p.getValue();
            int cont = 0;
            for (Resposta resposta : pergunta.getRespostas()) { //percorre todas as respostas
                //verifica se a resposta foi criada antes da melhor resposta
                if (resposta.getCreationDate().before(pergunta.getDataAcceptedAnswerId())) {
                    cont++;
                }
            }
            pergunta.setPeso(cont);
        }
    }

    public static void filtrarBase(Map<Integer, Pergunta> colecao) {
        //retirar perguntas que não tem todas as respostas
        //retirar as perguntas que ainda não tem uma resposta marcada como melhor
        //retirar as perguntas que não tem respostas
        //retirar as perguntas que tem todas as respostas, mas a resposta marcada como melhor não existe na base
        Iterator<Map.Entry<Integer, Pergunta>> it = colecao.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Pergunta> item = it.next();
            Pergunta p = item.getValue();
            if (p.getAnswerCount() != p.getRespostas().size() || p.getAcceptedAnswerId() == 0 || p.getRespostas().isEmpty() || p.getDataAcceptedAnswerId() == null || p.getOwnerUserId() == 0) {
                it.remove();
            }
        }
    }

    public static List<Item> gerarGrafo(Map<Integer, Pergunta> colecao) {
        List<Item> grafo = new ArrayList<>();
        for (Map.Entry<Integer, Pergunta> p : colecao.entrySet()) {
            for (Resposta r : p.getValue().getRespostas()) {
                Item item = new Item(p.getValue().getOwnerUserId(), r.getOwnerUserId(), p.getValue().getPeso());
                if (!grafo.contains(item)) {
                    grafo.add(item);
                } else {
                    int pos = grafo.indexOf(item);                   
                    grafo.get(pos).setPeso(grafo.get(pos).getPeso() + p.getValue().getPeso());//soma peso atual mais peso da nova aresta
                }               
            }
        }
        return grafo;
    }

    public static void escreverArquivo(List<Item> grafo) {
        String grafoString = "", pesos = "";
        for (Item item : grafo) {
            grafoString += item.toString() + "\n";
            pesos += item.getPeso() + "\n";
        }
        try {
            Files.write(Paths.get("D:/DisciplinaUFMG/ModelThinking/Trabalho/Oficial/grafo.txt"), grafoString.getBytes());
            Files.write(Paths.get("D:/DisciplinaUFMG/ModelThinking/Trabalho/Oficial/peso.txt"), pesos.getBytes());
        } catch (IOException e) {
            System.out.println("Error na escrita: " + e.getMessage());
        }
    }

    public static void imprimirAvaliacao(List<Avaliacao> avaliacoes) {
        avaliacoes.forEach((a) -> {
            System.out.println(a);
        });
    }

    //cria ordenação (vetor) com base nas posições dos usuários que estão no rank
    public static double[] converterListArray(List<Integer> rank, List<Avaliacao> referencia) {
        double[] arrayRef = new double[rank.size()];
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < rank.size(); i++) {
            map.put(rank.get(i), i);
        }
        int i = 0;
        for (Avaliacao a : referencia) {
            arrayRef[map.get(a.getOwnerId())] = i;
            i++;
        }
        return arrayRef;
    }

    public static double[] obterRank(int n) {
        double[] arrayRank = new double[n];
        for (int i = 0; i < n; i++) {
            arrayRank[i] = i;
        }
        return arrayRank;
    }

    public static void main(String[] args) {
        Map<Integer, Pergunta> colecao = BancoDados.lerBase();
        filtrarBase(colecao);
        calcularPesos(colecao);

        //BancoDados.escreverBD(colecao);
        List<Item> itens = gerarGrafo(colecao);
        int tam;

        //PageRank original
        Grafo grafoOriginal = PageRank.obtemVertices(itens);
        PageRank.criaMatrizAdjacencia(itens, grafoOriginal);
        PageRank.calculaPageRank(grafoOriginal, 0.85); //convergencia 0.001
        tam = grafoOriginal.getTamanho();
        List<Integer> rankOriginal = PageRank.obterTopKPageRank(grafoOriginal, tam);
        List<Avaliacao> avaliacoesOriginal = Referencia.avaliar(rankOriginal, colecao);
        //System.out.println("PageRank Original");
        //imprimirAvaliacao(avaliacoesOriginal);

        //PageRank com pesos
        Grafo grafoPesos = PageRank.obtemVertices(itens);
        PageRank.criaMatrizAdjacenciaPesos(itens, grafoPesos);
        PageRank.calculaPageRankPesos(grafoPesos, 0.85);
        List<Integer> rankPesos = PageRank.obterTopKPageRank(grafoPesos, tam);
        List<Avaliacao> avaliacoesPesos = Referencia.avaliar(rankPesos, colecao);      
        Referencia.obterAvaliacaoQualitativa(avaliacoesPesos);
        
        //Gerar Ranks de referência
        //Score Referencia duas ordenações
        List<Avaliacao> rankReferencia1 = Referencia.gerarRankScore1(avaliacoesPesos);
        //Score Referencia maior quantScore
        List<Avaliacao> rankReferencia2 = Referencia.gerarRankScore2(avaliacoesPesos);      
        //Melhor resposta - Referencia duas ordenações
        List<Avaliacao> rankReferencia3 = Referencia.gerarRankMelhorResp1(avaliacoesPesos);        
        //Melhor Resposta - Referencia quant quantMelhorResp
        List<Avaliacao> rankReferencia4 = Referencia.gerarRankMelhorResp2(avaliacoesPesos);

        //Comparação das referências com PageRank com Pesos
        System.out.println("Page Rank com pesos");
        double[] arrayRank = obterRank(rankPesos.size());
        double[] rankR1 = converterListArray(rankPesos, rankReferencia1);
        double[] rankR2 = converterListArray(rankPesos, rankReferencia2);
        double[] rankR3 = converterListArray(rankPesos, rankReferencia3);
        double[] rankR4 = converterListArray(rankPesos, rankReferencia4);
        KendallsCorrelation kt = new KendallsCorrelation();
        double valor = kt.correlation(arrayRank, rankR1);
        System.out.println("Valor: " + valor);
        valor = kt.correlation(arrayRank, rankR2);
        System.out.println("Valor: " + valor);
        valor = kt.correlation(arrayRank, rankR3);
        System.out.println("Valor: " + valor);
        valor = kt.correlation(arrayRank, rankR4);
        System.out.println("Valor: " + valor + "\n");

        //Comparação das referências com PageRank original
        System.out.println("Page Rank original");
        double[] arrayRankOriginal = obterRank(rankOriginal.size());
        rankR1 = converterListArray(rankOriginal, rankReferencia1);
        rankR2 = converterListArray(rankOriginal, rankReferencia2);
        rankR3 = converterListArray(rankOriginal, rankReferencia3);
        rankR4 = converterListArray(rankOriginal, rankReferencia4);
        valor = kt.correlation(arrayRankOriginal, rankR1);
        System.out.println("Valor: " + valor);
        valor = kt.correlation(arrayRankOriginal, rankR2);
        System.out.println("Valor: " + valor);
        valor = kt.correlation(arrayRankOriginal, rankR3);
        System.out.println("Valor: " + valor);
        valor = kt.correlation(arrayRankOriginal, rankR4);
        System.out.println("Valor: " + valor);

    }
}
