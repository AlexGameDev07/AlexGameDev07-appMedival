package Model

import java.lang.Exception
import java.sql.Connection
import java.sql.DriverManager

class Connection {
    fun Connect(): Connection?{
        try {
            val url="jdbc:oracle:thin:@192.168.68.109:1521:xe"
            val user="MEDIVAL_DEVELOPER"
            val password="fakedrips"

            val connection= DriverManager.getConnection(url, user, password)

            return connection
        }catch (e: Exception){
            println("Clase Conexion: este es el error:$e")
            return null
        }
    }
}