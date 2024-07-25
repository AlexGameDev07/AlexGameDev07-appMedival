package Model

import android.util.Log
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class Connection {
    fun Connect(): Connection? {
        return try {
            // Deshabilitar el registro de MBeans
            System.setProperty("oracle.jdbc.J2EE13Compliant", "true")

            // Cargar el controlador JDBC de Oracle
            Class.forName("oracle.jdbc.driver.OracleDriver")

            val url = "jdbc:oracle:thin:@192.168.1.23:1521:xe"
            val user = "AlexDev"
            val password = "lmq0MXbL"

            // Establecer la conexión
            DriverManager.getConnection(url, user, password)
        } catch (e: ClassNotFoundException) {
            Log.e("Connection", "Error al cargar el controlador JDBC: ${e.message}")
            null
        } catch (e: SQLException) {
            Log.e("Connection", "Error de SQL: ${e.message}")
            null
        } catch (e: Exception) {
            Log.e("Connection", "Otro error: ${e.message}")
            null
        }
    }
}
