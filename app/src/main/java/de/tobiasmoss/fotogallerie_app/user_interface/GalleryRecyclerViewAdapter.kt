package de.tobiasmoss.fotogallerie_app.user_interface

import android.app.Activity
import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.documentfile.provider.DocumentFile
import de.tobiasmoss.fotogallerie_app.R
import java.io.FileDescriptor
import java.io.InputStream

/**
 * [RecyclerView.Adapter] that can display an external Folder.
 */
class GalleryRecyclerViewAdapter(
    private var myActivity : Activity,
    private var values: List<DocumentFile>
) : RecyclerView.Adapter<GalleryRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_gallery, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.imageView.setImageBitmap(
            Bitmap.createScaledBitmap(decodeSampledBitmapFromFile(item.uri), 500, 500, false)
        )

    }

    private fun decodeSampledBitmapFromFile(uri: Uri): Bitmap {
        Log.i("Images", "Decoding: $uri")
        // First decode with inJustDecodeBounds=true to check dimensions

        return BitmapFactory.Options().run {
            inJustDecodeBounds = true
            val parcelFileDescriptor: ParcelFileDescriptor =
                myActivity.applicationContext.contentResolver.openFileDescriptor(uri, "r")!!
            val fileDescriptor: FileDescriptor = parcelFileDescriptor.fileDescriptor

            inSampleSize = 8
            inJustDecodeBounds = false

            BitmapFactory.decodeFileDescriptor(fileDescriptor, null, this)
        }

    }

    override fun getItemCount(): Int = values.size

    fun updateItems(items: List<DocumentFile>){
        values = items
        this.notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageView)

        override fun toString(): String {
            return super.toString() + " '" + imageView.toString() + "'"
        }
    }
}