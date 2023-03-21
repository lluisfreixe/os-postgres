package cat.openshift;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;

@WebServlet("/DonarAlta")
public class DonarAlta extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final String url = "jdbc:postgresql://localhost/postgres";
	private final String user = "postgres";
	private final String password = "password";
	private static final String query1 = "SELECT MAX(idpersona) idpersona FROM persona;";
    private static final String insert1 = "INSERT INTO persona (idpersona, nombre, cedula) VALUES (?, ?, ?);";
    private static final String delete1 = "DELETE FROM persona;";


	public DonarAlta() {
		super();
	}

	protected void request(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String missatge = "";
		
		try {
            try { 
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException ex) {
                missatge = "Error al registrar el driver de PostgreSQL: " + ex;
            }
            Connection conn = null;
            conn = DriverManager.getConnection(url, user, password);
            conn.setAutoCommit(false);
            
            PreparedStatement preparedStatement = conn.prepareStatement(query1);
            ResultSet rs = preparedStatement.executeQuery();
            int num = 0, comm = 0, cont_comm = 0;
            String nom, ced;

            if (rs.next()) {
            	num = rs.getInt("idpersona");
            	preparedStatement = conn.prepareStatement(delete1);
            	preparedStatement.executeUpdate();
                //missatge = "La ultima fila de la taula persona es la numero " + num;
            } else {
                num = 0;
            	//missatge = "No hi ha dades a la taula persona.";
            }
            
            for (num = 1; num <= 2000; num++) {
            	comm++;
                preparedStatement = conn.prepareStatement(insert1);
                preparedStatement.setInt(1, num);
                nom = "Lluis"+num;
                ced = "Cedula"+num;
                preparedStatement.setString(2, nom);
                preparedStatement.setString(3, ced);
                preparedStatement.executeUpdate();
                if (comm == 100) {
                	conn.commit();
                	//System.out.println("S'ha fet un comit.");
                	comm = 0;
                	cont_comm++;
                }
            }

            /*
            num++;
            preparedStatement = conn.prepareStatement(insert1);
            preparedStatement.setInt(1, num);
            nom = "Lluis";
            ced = "Cedula1";
            preparedStatement.setString(2, nom);
            preparedStatement.setString(3, ced);
            preparedStatement.executeUpdate();
            */
            
            //missatge = "Alta fila amb numero: " + num + " nom: " + nom + " cedula: " + ced + ".";
            missatge = "Alta correcta i s'han fet " + cont_comm + " commits.";
            
            conn.close();
        } catch (java.sql.SQLException sqle) {
        	missatge = "Error: " + sqle;
        }

		request.setAttribute("miss", missatge);
		request.getRequestDispatcher("tornada.jsp").forward(request, response);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request(request, response);
	}

}
