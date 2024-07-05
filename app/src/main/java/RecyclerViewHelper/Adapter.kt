package RecyclerViewHelper

import Model.DataClassPacientes
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class Adapter (private var Data: List<DataClassPacientes>) : RecyclerView.Adapter<ViewHolder>(){

    fun RecargarVista(newDataList: List<DataClassPacientes>){
        Data = newDataList
        notifyDataSetChanged()
    }

    fun actualizarListaDespuesDeActualizarDatos(id: Int, nuevoNombre: String){
        val index = Data.indexOfFirst { it.idPacientes == id }
        Datos[index].NombreProducto= nuevoNombre
        notifyItemChanged(index)
    }

    //Aun esta incompleto esto
}