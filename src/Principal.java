
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;

public class Principal {

    public static Map<Integer, Pergunta> lerBase() {
        int id;
        Connection con;
        Map<Integer, Pergunta> colecao;
        colecao = new HashMap<>();
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Error : " + e.getMessage());
        }

        String url = "jdbc:mysql://localhost/Posts";
        String user = "root";
        String password = "";

        try {
            con = DriverManager.getConnection(url, user, password);
            String qTags = "select Id, CreationDate, Score, Body, Tags, PostTypeId, ParentId, AcceptedAnswerId, OwnerUserId, AnswerCount, Title from menor2  where  (PostTypeId = 1 and Tags like '%<c#>%') or PostTypeId = 2";
            PreparedStatement stmt = con.prepareStatement(qTags);

            ResultSet rs = stmt.executeQuery();
            int cont = 0;
            while (rs.next()) {
                if (rs.getInt("OwnerUserId") != 0) { //só utiliza o post se tiver a identificação do owerUserId
                    if (rs.getInt("PostTypeId") == 1) { //é pergunta
                        id = rs.getInt("Id");
                        Pergunta p = new Pergunta();
                        p.setId(id);
                        String data;
                        data = rs.getString("creationDate");
                        p.setCreationDate(formatter.parse(data));
                        p.setScore(rs.getInt("Score"));
                        p.setBody(rs.getString("Body"));
                        p.setTags(rs.getString("Tags"));
                        p.setAcceptedAnswerId(rs.getInt("AcceptedAnswerId"));
                        p.setOwnerUserId(rs.getInt("OwnerUserId"));
                        p.setAnswerCount(rs.getInt("AnswerCount"));
                        p.setTitle(rs.getString("Title"));
                        colecao.put(id, p);
                    } else { //é uma resposta
                        Resposta r = new Resposta();
                        id = rs.getInt("Id");
                        r.setId(id);
                        String data = rs.getString("creationDate");//
                        r.setCreationDate(formatter.parse(data));
                        r.setScore(rs.getInt("Score"));//
                        r.setBody(rs.getString("Body"));//
                        r.setPostTypeId(rs.getInt("PostTypeId"));//
                        r.setParentId(rs.getInt("ParentId"));//
                        r.setOwnerUserId(rs.getInt("OwnerUserId"));//

                        //pega a pergunta vinculada a resposta corrente
                        Pergunta pergunta = colecao.get(rs.getInt("ParentId"));
                        if (pergunta != null) { //validacao caso nao tenha encontrado a pergunta na base
                            if (pergunta.getAcceptedAnswerId() == r.getId()) { //se é a melhor resposta
                                //armazena a data da melhor resposta na pergunta
                                pergunta.setDataAcceptedAnswerId(r.getCreationDate());

                            }
                            pergunta.getRespostas().add(r);//adiciona a resposta na lista
                        }

                    }
                }

                cont++;

            }
            //System.out.println("Total: " + cont);
            rs.close();
            stmt.close();
            con.close();

        } catch (SQLException e) {
            System.out.println("Error 1: " + e.getMessage());
        } catch (ParseException e) {
            System.out.println("Error 2: " + e.getMessage());
        }
        return colecao;

    }

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
        int quant = 0;
        Iterator<Map.Entry<Integer, Pergunta>> it = colecao.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Pergunta> item = it.next();
            Pergunta p = item.getValue();
            if (p.getAnswerCount() != p.getRespostas().size() || p.getAcceptedAnswerId() == 0 || p.getRespostas().isEmpty() || p.getDataAcceptedAnswerId() == null || p.getOwnerUserId() == 0) {
                quant++;
                it.remove();
            }
        }
        System.out.println("REMOVIDOS"+ quant);
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
                    //System.out.println(pos);
                    grafo.get(pos).setPeso(grafo.get(pos).getPeso() + p.getValue().getPeso());//soma peso atual mais peso da nova aresta
                }
                //System.out.println(+p.getValue().getOwnerUserId() + " " + r.getOwnerUserId() + " " + p.getValue().getPeso());
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
                System.out.println("Id: "+OwnerIdMaxScorePerg);
                if (OwnerIdMaxScorePerg == rank.get(i)) { //usuario deu a resposta com maior score
                     System.out.println("Entrou");
                    avaliacoes.get(i).setQuantMaiorScore(avaliacoes.get(i).getQuantMaiorScore() + 1);
                }
                 avaliacoes.get(i).setMaiorScore(maxScoreTotal);
                 avaliacoes.get(i).setMenorScore(minScoreTotal);
            }
        }
        return avaliacoes;
    }

    /*public static void conferirResultado(List<Integer> result, Map<Integer, Pergunta> colecao) {
        int maxScore;
        int OwnerIdMaiorScore = 0;
        List<Integer> acertosMelhorResposta = new ArrayList<>();
        List<Integer> acertosScore = new ArrayList<>();
        List<Integer> quantRespondidas = new ArrayList<>(); //Quantas perguntas foram respondidas
        for (int i = 0; i < result.size(); i++) {
            acertosMelhorResposta.add(0);
            acertosScore.add(0);
            quantRespondidas.add(0);
        }
        for (int i = 0; i < result.size(); i++) {
            for (Map.Entry<Integer, Pergunta> p : colecao.entrySet()) {
                int idAcceptedAnswer = p.getValue().getAcceptedAnswerId(); //id da melhor resposta
                maxScore = -1;
                for (Resposta resposta : p.getValue().getRespostas()) {

                    if (resposta.getId() == idAcceptedAnswer) { //é a melhor resposta

                        if (result.get(i) == resposta.getOwnerUserId()) { //a melhor resposta foi dada por um dos top rank
                            //System.out.println(resposta.getId() + " "+ resposta.getOwnerUserId() );
                            acertosMelhorResposta.set(i, acertosMelhorResposta.get(i) + 1);
                        }
                    }
                    if (maxScore < resposta.getScore()) { //obtendo resposta com maior score
                        maxScore = resposta.getScore();
                        OwnerIdMaiorScore = resposta.getOwnerUserId();
                    }
                    if (result.get(i) == resposta.getOwnerUserId()) {//
                        quantRespondidas.set(i, quantRespondidas.get(i) + 1);
                    }

                }
                if (OwnerIdMaiorScore == result.get(i)) {
                    acertosScore.set(i, acertosScore.get(i) + 1);
                }
            }
        }

        System.out.println("Score" + "   " + "Melhor resposta");
        for (int i = 0; i < acertosScore.size(); i++) {
            System.out.println(acertosScore.get(i) + "   " + acertosMelhorResposta.get(i) + "  " + quantRespondidas.get(i));
        }
    }*/

    public static void imprimirAvaliacao(List<Avaliacao> avaliacoes){
        avaliacoes.forEach((a) -> {
            System.out.println(a);
        });
    }
    public static void main(String[] args) {
        Map<Integer, Pergunta> colecao = lerBase();
        filtrarBase(colecao);
        calcularPesos(colecao);
        List<Item> itens = gerarGrafo(colecao);
        Grafo grafo = PageRank.obtemVertices(itens);
        PageRank.criaMatrizAdjacencia(itens, grafo);
        PageRank.calculaPageRank(grafo, 0.85);
        List<Integer> rank = PageRank.imprimeTopKPageRank(grafo, 10);
        List<Avaliacao> avaliacoes = avaliar(rank, colecao);
        imprimirAvaliacao(avaliacoes);
        
        /* List<Item> itens = new ArrayList<>();
        itens.add(new Item(1,3,1));
        itens.add(new Item(1,2,1));
        itens.add(new Item(2,3,1));
        itens.add(new Item(3,1,1));
        itens.add(new Item(3,2,1));
        itens.add(new Item(4,2,1));
        Grafo grafo = PageRank.obtemVertices(itens);
        PageRank.criaMatrizAdjacencia(itens, grafo);
        PageRank.calculaPageRank(grafo, 0.85); //MUDAR
        PageRank.imprimeTopKPageRank(grafo, 20);*/
    }
}
