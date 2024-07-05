package RecyclerViewHelper

import Model.DataClassPacientes
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class Adapter(private var Data: List<DataClassPacientes>) : RecyclerView.Adapter<ViewHolder>() {

    fun RecargarVista(newDataList: List<DataClassPacientes>){
        Data = newDataList
        notifyDataSetChanged()
    }

    fun actualizarListaDespuesDeActualizarDatos(uuid: String, nuevoNombre: String){
        val index = Datos.indexOfFirst { it.uuid == uuid }
        Datos[index].NombreProducto= nuevoNombre
        notifyItemChanged(index)
    }

    //Aun esta incompleto esto

}