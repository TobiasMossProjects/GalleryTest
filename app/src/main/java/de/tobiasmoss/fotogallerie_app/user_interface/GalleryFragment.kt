package de.tobiasmoss.fotogallerie_app.user_interface

import android.Manifest
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.tobiasmoss.fotogallerie_app.R


/**
 * A fragment representing a list of Items.
 */
class GalleryFragment : Fragment() {

    private var columnCount = 3
    private lateinit var myAdapter: GalleryRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val myView = inflater.inflate(R.layout.fragment_gallery_list, container, false)

        // Request Permission
        activity?.let {
            ActivityCompat.requestPermissions(
                it,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                1
            )
        }

        // Set the adapter
        if (requireActivity().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            columnCount += 1
        }
        if (myView is RecyclerView)
            with(myView) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }

                myAdapter = GalleryRecyclerViewAdapter(requireActivity(), emptyList())
                adapter = myAdapter
            }

        return myView
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        Log.i("Menu", "Menu created")
        inflater.inflate(R.menu.select_directory, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.i("Menu", "Item clicked: " + item.itemId)
        return when (item.itemId) {
            R.id.action_select -> startFolderSelectionActivity()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun startFolderSelectionActivity(): Boolean {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        Log.i("External", "Activity For Result")
        startActivityForResult(intent, ActivityConstants.DOCUMENT_TREE)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            ActivityConstants.DOCUMENT_TREE -> if (data != null) {
                context?.let {
                    data.data?.let { it1 ->
                        DocumentFile.fromTreeUri(
                            it, it1
                        )
                    }
                }?.let { myAdapter.updateItems(it.listFiles().toList()) }
            }
            // Add more Options here
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            GalleryFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}

object ActivityConstants {
    const val DOCUMENT_TREE = 91
}