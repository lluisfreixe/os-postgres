package cat.openshift;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet("/")
public class AltaBBDD extends HttpServlet {

	// ================================================
	// Declarar variables - v3
	// ================================================

	private static final long serialVersionUID = 1L;
	private final String url1 = "jdbc:postgresql://localhost/postgres";
	private final String url2 = "jdbc:postgresql://172.30.179.78:5432/postgres";
	private final String user = "postgres";
	private final String password = "password";
	private static final String create1 = "CREATE TABLE persona (idpersona INT NOT NULL, nombre VARCHAR (30) NOT NULL, cedula VARCHAR (30) NOT NULL)";
	private static final String query1 = "SELECT MAX(idpersona) idpersona FROM persona;";
	private static final String insert1 = "INSERT INTO persona (idpersona, nombre, cedula) VALUES (?, ?, ?);";
	private static final String delete1 = "DELETE FROM persona;";

	public AltaBBDD() {
		super();
	}

	protected void request(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// ================================================
		// Declarar variables
		// ================================================

		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Statement stmt = null;

		String ipAddress = request.getRemoteAddr();
		String missatge = "";
		Boolean nova = false;
		Boolean error = false;
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		String hora = dateFormat.format(date);
		int num = 0, comm = 0, cont_comm = 0;
		String nom, ced;

		// ================================================
		// Drivers de Postgres
		// ================================================

		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException ex) {
			missatge = "Error al registrar el driver de PostgreSQL: " + ex;
			error = true;
		}

		// ================================================
		// Connectar a la BBDD PostgresSQL
		// ================================================

		if (error.equals(false)) {
			try {
				if (ipAddress.equals("127.0.0.1")) {
					conn = DriverManager.getConnection(url1, user, password);
				} else {
					conn = DriverManager.getConnection(url2, user, password);
				}
			} catch (java.sql.SQLException sqle) {
				missatge = "Error de connexio: " + sqle;
				error = true;
			}
		}

		// ================================================
		// Verificar si hi ha alguna fila a la taula.
		// ================================================

		if (error.equals(false)) {
			try {
				preparedStatement = conn.prepareStatement(query1);
				rs = preparedStatement.executeQuery();
			} catch (Exception e) {
				nova = true;
			}
		}

		// ================================================
		// Crear la taula quan no existeix.
		// I despres deixar el autocommit a false
		// ================================================

		if (error.equals(false) && nova.equals(true)) {
			try {
				conn.setAutoCommit(true);
				stmt = conn.createStatement();
				stmt.executeUpdate(create1);
				// conn.commit();
			} catch (java.sql.SQLException sqle) {
				missatge = "Error create SQLException: " + sqle;
				error = true;
			} catch (Exception e) {
				missatge = "Error create Exception: " + e;
				error = true;
			}
		}

		if (error.equals(false) && nova.equals(false)) {
			try {
				conn.setAutoCommit(false);
			} catch (java.sql.SQLException sqle) {
				missatge = "Error autocommit SQLException: " + sqle;
				error = true;
			} catch (Exception e) {
				missatge = "Error autocommit Exception: " + e;
				error = true;
			}
		}

		// ================================================
		// Esborrar les files quan existeixen.
		// ================================================

		if (nova.equals(false) && error.equals(false)) {
			try {
				if (rs.next()) {
					num = rs.getInt("idpersona");
					preparedStatement = conn.prepareStatement(delete1);
					preparedStatement.executeUpdate();
					conn.commit();
					// missatge = "La ultima fila de la taula persona es la numero " + num;
				} else {
					num = 0;
				}
			} catch (java.sql.SQLException sqle) {
				missatge = "Error delete SQLException: " + sqle;
			} catch (Exception e) {
				missatge = "Error delete Exception: " + e;
			}
		}

		// ================================================
		// Bucle per inserir les noves files
		// ================================================

		if (error.equals(false)) {
			try {
				for (num = 1; num <= 3000; num++) {
					comm++;
					preparedStatement = conn.prepareStatement(insert1);
					preparedStatement.setInt(1, num);
					nom = "Prova amb Postgres" + num;
					ced = num + " - " + hora;
					preparedStatement.setString(2, nom);
					preparedStatement.setString(3, ced);
					preparedStatement.executeUpdate();
					if (comm == 1000 && nova.equals(false)) {
						conn.commit();
						// System.out.println("S'ha fet un comit.");
						comm = 0;
						cont_comm++;
					}
				}
				missatge = "V1-Alta correcta i s'han fet " + cont_comm + " commits.";
				conn.close();
			} catch (java.sql.SQLException sqle) {
				missatge = "Error insert SQLException: " + sqle;
			} catch (Exception e) {
				missatge = "Error insert Exception: " + e;
			}
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
