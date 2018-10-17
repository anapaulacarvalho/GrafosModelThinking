
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class Principal {

    public static Map<Integer, Pergunta> lerBase() {
        int id;
        Connection con;
        Map<Integer, Pergunta> colecao = new HashMap<>();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }

        String url = "jdbc:mysql://localhost/Posts";
        String user = "root";
        String password = "";

        try {
            con = DriverManager.getConnection(url, user, password);
            String qTags = "select Id, CreationDate, Score, Body, Tags, PostTypeId, ParentId, AcceptedAnswerId, OwnerUserId, AnswerCount from novo";
            PreparedStatement stmt = con.prepareStatement(qTags);
        
            ResultSet rs = stmt.executeQuery();
            int cont = 0;
            while (rs.next()) {
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
                    colecao.put(id, p);
                    int i = 0;
                } else { //é uma resposta
                    System.out.println("Aqui");
                    Resposta r = new Resposta();
                    id = rs.getInt("Id");
                    r.setID(id);
                    String data = rs.getString("creationDate");
                    r.setCreationDate(formatter.parse(data));
                    r.setScore(rs.getInt("Score"));
                    r.setBody(rs.getString("Body"));
                    
                    r.setPostTypeId(rs.getInt("PostTypeId"));
                    r.setParentId(rs.getInt("ParentId"));
                    r.setOwnerUserId(rs.getInt("OwnerUserId"));
                    
                    //pega a pergunta vinculada a resposta corrente
                    Pergunta pergunta = colecao.get(rs.getInt("ParentId"));

                    if (pergunta.getAcceptedAnswerId() == r.getID()) { //se é a melhor resposta
                        //armazena a data da melhor resposta na pergunta
                        pergunta.setDataAcceptedAnswerId(r.getCreationDate());
                    }

                    pergunta.getRespostas().add(r);//adiciona a resposta na lista
                }

                cont++;

            }
            System.out.println("Total: " + cont);
            rs.close();
            stmt.close();
            con.close();

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
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
                System.out.println(pergunta.getId() + " " + resposta.getID());
            }
            pergunta.setPeso(cont);
            System.out.println("Peso: " +cont +"\n"); 
         }

    }

    public static void main(String[] args) {
        Map<Integer, Pergunta> colecao = lerBase();
        /*for (Map.Entry<Integer, Pergunta> p : colecao.entrySet()) {
            System.out.println("Tamanho"+ p.getValue().getRespostas().size());
            for (Resposta resposta : p.getValue().getRespostas()) { 
                System.out.println(p.getKey() + resposta.getID());
            }
        }*/
        calcularPesos(colecao);
    }
}
