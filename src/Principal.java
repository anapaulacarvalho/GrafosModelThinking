
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
import java.nio.file.StandardOpenOption;

public class Principal {

    public static Map<Integer, Pergunta> lerBase() {
        int id;
        Connection con;
        Map<Integer, Pergunta> colecao = new HashMap<>();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

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
        } catch (Exception e) {
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
                    //System.out.println(pos);
                    grafo.get(pos).setPeso(grafo.get(pos).getPeso() + p.getValue().getPeso());//soma peso atual mais peso da nova aresta
                }
                //System.out.println(+p.getValue().getOwnerUserId() + " " + r.getOwnerUserId() + " " + p.getValue().getPeso());
            }
        }
        return grafo;

    }

    public static void escreverArquivo(List<Item> grafo) {
        String grafoString = "", pesos ="";
        for (Item item : grafo) {
            grafoString += item.toString()+"\n";
            pesos+=item.getPeso()+"\n";
        }
        try {
            Files.write(Paths.get("D:/DisciplinaUFMG/ModelThinking/Trabalho/Oficial/grafo.txt"),grafoString.getBytes());
            Files.write(Paths.get("D:/DisciplinaUFMG/ModelThinking/Trabalho/Oficial/peso.txt"), pesos.getBytes());
        } catch (IOException e) {
            System.out.println("Error na escrita: " + e.getMessage());
        }

    }

    public static void main(String[] args) {
        Map<Integer, Pergunta> colecao = lerBase();
        filtrarBase(colecao);
        calcularPesos(colecao);
        List<Item> itens = gerarGrafo(colecao);
     
        Grafo grafo = PageRank.obtemVertices(itens);
        PageRank.criaMatrizAdjacencia(itens, grafo);
        PageRank.calculaPageRank(grafo, 0.85);
        PageRank.imprimeTopKPageRank(grafo, 10);
    }
}
