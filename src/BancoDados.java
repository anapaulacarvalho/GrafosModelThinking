
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class BancoDados {
    
    
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
            String qTags = "select Id, CreationDate, Score, Body, Tags, PostTypeId, ParentId, "
                    + "AcceptedAnswerId, OwnerUserId, AnswerCount, Title "
                    + "from post1  where (PostTypeId = 1 and Tags like '%<.net>%') or PostTypeId = 2";
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
                        String data = rs.getString("creationDate");
                        r.setCreationDate(formatter.parse(data));
                        r.setScore(rs.getInt("Score"));
                        r.setBody(rs.getString("Body"));
                        r.setPostTypeId(rs.getInt("PostTypeId"));
                        r.setParentId(rs.getInt("ParentId"));
                        r.setOwnerUserId(rs.getInt("OwnerUserId"));

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

    public static void escreverBD(Map<Integer, Pergunta> colecao) {
        Connection con;
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Error : " + e.getMessage());
        }

        String url = "jdbc:mysql://localhost/Posts";
        String user = "root";
        String password = "";
        Pergunta pergunta;

        try {
            con = DriverManager.getConnection(url, user, password);
            PreparedStatement pstmt = null;

            for (Map.Entry<Integer, Pergunta> p : colecao.entrySet()) {
                pergunta = p.getValue();
                pstmt = con.prepareStatement(
                        "Insert into filtrada(Id, CreationDate, Score, Body, Tags, PostTypeId, ParentId, AcceptedAnswerId, OwnerUserId, AnswerCount, Title) values(?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?)");
                pstmt.setInt(1, pergunta.getId());
                java.util.Date a = pergunta.getCreationDate();
                java.sql.Timestamp b = new java.sql.Timestamp(a.getTime());
                pstmt.setTimestamp(2, b); //conferir
                pstmt.setDouble(3, pergunta.getScore());
                pstmt.setString(4, pergunta.getBody());
                pstmt.setString(5, "<c#>");
                pstmt.setInt(6, 1); //Tipo pergunta
                pstmt.setInt(7, -1); //sem valor real
                pstmt.setInt(8, pergunta.getAcceptedAnswerId());
                pstmt.setInt(9, pergunta.getOwnerUserId());
                pstmt.setInt(10, pergunta.getAnswerCount());
                pstmt.setString(11, pergunta.getTitle());
                pstmt.executeUpdate();

                for (Resposta resposta : pergunta.getRespostas()) { //percorre todas as respostas
                    pstmt = con.prepareStatement(
                            "Insert into filtrada(Id, CreationDate, Score, Body, Tags, PostTypeId, ParentId, AcceptedAnswerId, OwnerUserId, AnswerCount, Title) values(?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?)");
                    pstmt.setInt(1, resposta.getId());
                    a = resposta.getCreationDate();
                    b = new java.sql.Timestamp(a.getTime());
                    pstmt.setTimestamp(2, b);
                    pstmt.setDouble(3, resposta.getScore());
                    pstmt.setString(4, resposta.getBody());
                    pstmt.setString(5, "<c#>");
                    pstmt.setInt(6, 2); //Tipo pergunta
                    pstmt.setInt(7, resposta.getParentId()); 
                    pstmt.setInt(8, -1); //sem valor real
                    pstmt.setInt(9, resposta.getOwnerUserId());
                    pstmt.setInt(10, -1); //sem valor real
                    pstmt.setString(11, "");//sem valor real
                    pstmt.executeUpdate();
                }
            }

            pstmt.close();
            con.close();

        } catch (SQLException e) {
            System.out.println("Error 1: " + e.getMessage());
        }

    }

}
