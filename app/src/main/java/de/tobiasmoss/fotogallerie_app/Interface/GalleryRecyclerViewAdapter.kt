package de.tobiasmoss.fotogallerie_app.Interface

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.documentfile.provider.DocumentFile
import de.tobiasmoss.fotogallerie_app.R

/**
 * [RecyclerView.Adapter] that can display an external Folder.
 * TODO: Replace the implementation with code for your data type.
 */
class GalleryRecyclerViewAdapter(
    private var values: List<DocumentFile>
) : RecyclerView.Adapter<GalleryRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_gallery, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idView.text = position.toString()
        holder.contentView.text = item.name
    }

    override fun getItemCount(): Int = values.size

    fun updateItems(items: List<DocumentFile>){
        values = items
        this.notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val idView: TextView = view.findViewById(R.id.item_number)
        val contentView: TextView = view.findViewById(R.id.content)

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }
}