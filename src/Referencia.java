
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Referencia {    

    public static List<Avaliacao> avaliar(List<Integer> rank, Map<Integer, Pergunta> colecao) {
        int maxScoreTotal, minScoreTotal, maxScorePerg, OwnerIdMaxScorePerg = -1;
        List<Avaliacao> avaliacoes = new ArrayList<>(); 
        for (int i = 0; i < rank.size(); i++) {
            avaliacoes.add(new Avaliacao());
            avaliacoes.get(i).setOwnerId(rank.get(i));
            maxScoreTotal = Integer.MIN_VALUE;
            minScoreTotal = Integer.MAX_VALUE;
            //percorre todas as perguntas
            for (Map.Entry<Integer, Pergunta> p : colecao.entrySet()) {
                int idAcceptedAnswer = p.getValue().getAcceptedAnswerId(); //id da melhor resposta
                maxScorePerg = Integer.MIN_VALUE;
                //percorre todas as respostas de uma pergunta
                for (Resposta resposta : p.getValue().getRespostas()) {
                    if (resposta.getOwnerUserId() == rank.get(i)) {
                        avaliacoes.get(i).setQuantResp(avaliacoes.get(i).getQuantResp() + 1);
                        if (resposta.getId() == idAcceptedAnswer) { //é a melhor resposta                       
                            avaliacoes.get(i).setQuantMelhorResp(avaliacoes.get(i).getQuantMelhorResp() + 1);
                        }

                        if (resposta.getScore() > maxScoreTotal) {
                            maxScoreTotal = resposta.getScore();
                        }
                        if (resposta.getScore() < minScoreTotal) {
                            minScoreTotal = resposta.getScore();
                        }
                    }
                    if (maxScorePerg < resposta.getScore()) { //obtendo resposta com maior score
                        maxScorePerg = resposta.getScore();
                        OwnerIdMaxScorePerg = resposta.getOwnerUserId();
                    }
                }
                //System.out.println("Id: "+OwnerIdMaxScorePerg);
                if (OwnerIdMaxScorePerg == rank.get(i)) { //usuario deu a resposta com maior score
                    //System.out.println("Entrou");
                    avaliacoes.get(i).setQuantMaiorScore(avaliacoes.get(i).getQuantMaiorScore() + 1);
                }
                avaliacoes.get(i).setMaiorScore(maxScoreTotal);
                avaliacoes.get(i).setMenorScore(minScoreTotal);
            }
        }
        return avaliacoes;
    }

    //rank considerando o score, ordena por %de aceitação (maior score), 
    //em seguida ordena os empates por quantidade de respostas aceitas 
    public static List<Avaliacao> gerarRankScore1(List<Avaliacao> avaliacoes) {
        List<Avaliacao> rankReferencia = new ArrayList<>(avaliacoes.size());
        for (Avaliacao a : avaliacoes) {
            rankReferencia.add(new Avaliacao(a));
        }
        ComparadorPercScore comparador = new ComparadorPercScore();
        Collections.sort(rankReferencia, comparador);

        return rankReferencia;
    }

    //rank considerando o score, ordena por número de respostas aceitas (maior score)
    public static List<Avaliacao> gerarRankScore2(List<Avaliacao> avaliacoes) {
        List<Avaliacao> rankReferencia = new ArrayList<>(avaliacoes.size());
        for (Avaliacao a : avaliacoes) {
            rankReferencia.add(new Avaliacao(a));
        }
        ComparadorMaiorScore comparador = new ComparadorMaiorScore();
        Collections.sort(rankReferencia, comparador);

        return rankReferencia;
    }

    //rank considerando a melhor resposta, ordena por %de aceitação (melhor resposta definida pelo autor da pergunta), 
    //em seguida ordena os empates por quantidade de respostas aceitas 
    public static List<Avaliacao> gerarRankMelhorResp1(List<Avaliacao> avaliacoes) {
        List<Avaliacao> rankReferencia = new ArrayList<>(avaliacoes.size());
        for (Avaliacao a : avaliacoes) {
            rankReferencia.add(new Avaliacao(a));
        }
        ComparadorPercMelhorResp comparador = new ComparadorPercMelhorResp();
        Collections.sort(rankReferencia, comparador);

        return rankReferencia;
    }

    //rank considerando a melhor resposta, ordena por número de respostas aceitas (melhor resposta dad pelo autor da pergunta)
    public static List<Avaliacao> gerarRankMelhorResp2(List<Avaliacao> avaliacoes) {
        List<Avaliacao> rankReferencia = new ArrayList<>(avaliacoes.size());
        for (Avaliacao a : avaliacoes) {
            rankReferencia.add(new Avaliacao(a));
        }
        ComparadorMelhorResp comparador = new ComparadorMelhorResp();
        Collections.sort(rankReferencia, comparador);

        return rankReferencia;
    }
    
    //Montando avaliação qualitativa dos top 10
    public static void obterAvaliacaoQualitativa(List<Avaliacao> avaliacoes){
        String dados = "";
        for(int i = 0; i < 10; i++){
            Avaliacao a = avaliacoes.get(i);
           dados+= a.getOwnerId()+" "+a.getQuantResp()+" "+a.getMenorScore()+" "+a.getMaiorScore()+" "+a.getQuantMaiorScore()+
                   " "+a.getPercScore()+" "+a.getQuantMelhorResp()+" "+a.getPercMelhorResp()+ "\n";
        }
        try {
            Files.write(Paths.get("D:/DisciplinaUFMG/ModelThinking/Trabalho/Oficial/avaliacao.txt"), dados.getBytes());
        } catch (IOException e) {
            System.out.println("Error na escrita: " + e.getMessage());
        }
        
    }

}
