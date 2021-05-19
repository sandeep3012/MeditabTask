package com.meditab.task

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.meditab.task.adapter.ImageSearchAdapter
import com.meditab.task.databinding.ActivityMainBinding
import com.meditab.task.remote.Status
import com.meditab.task.utilities.CustomProgressDialog
import com.meditab.task.utilities.InjectorUtil
import com.meditab.task.viewmodel.ImageViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var customProgressDialog: Dialog

    private val imageSearchViewModel: ImageViewModel by viewModels(){
        InjectorUtil.provideImageViewModelFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this,R.layout.activity_main)
            .apply {
                lifecycleOwner = this@MainActivity
            }

        val adapter = ImageSearchAdapter{

            startActivity(Intent(this, ImageDetailActivity::class.java)
                .apply {
                    putExtra("title", it.title)
                    putExtra("index", it.index)
                    putExtra("imageUrl", it.thumbnail?.source)
                    putExtra("dimension", it.thumbnail?.width.toString().plus("*").plus(it.thumbnail?.width.toString()))
                })
        }

        binding.rv.adapter = adapter

        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query!!.isNotEmpty() && query.isNotBlank()) {
                    val q = query.trimEnd()
                    imageSearchViewModel.searchImage(200,q)
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }

        })
        subscribeUi(adapter)
    }

    fun subscribeUi(adapter: ImageSearchAdapter){
        imageSearchViewModel.liveSearchResponse.observe(this, Observer {
            adapter.submitList(it!!.pagesInfo)
        })

        imageSearchViewModel.imageSearchResponse.observe(this, Observer {
            when (it.status){
                Status.INACTIVE ->{}
                Status.LOADING ->{
                    showCustomProgressDialog("Loading")
                }
                Status.SUCCESS -> {
                    adapter.submitList(it!!.data!!.pagesInfo)
                    dismissDialog()
                }
                Status.ERROR -> {
                    dismissDialog()
                }
            }
        })
    }

    private fun showCustomProgressDialog(message : String){
        customProgressDialog = CustomProgressDialog.customProgressDialog(this, message)
        customProgressDialog.show()
    }

    private fun dismissDialog(){
        if (customProgressDialog.isShowing)
            customProgressDialog.dismiss()
    }
}