package com.meditab.task

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import coil.load
import coil.size.Scale
import com.meditab.task.databinding.ActivityImageDetailBinding
import com.meditab.task.databinding.ActivityMainBinding

class ImageDetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityImageDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityImageDetailBinding>(this,R.layout.activity_image_detail)
            .apply {
                lifecycleOwner = this@ImageDetailActivity
            }
        bindUi(intent)

        binding.btnGoBack.setOnClickListener {
            onBackPressed()
        }

    }

    fun bindUi(intent: Intent){
        binding.apply {
           // imageView.load(intent.getStringExtra("imageUrl"))
            val imgUrl = intent.getStringExtra("imageUrl")
            if (imgUrl.isNullOrBlank())
                imageView.load(R.drawable.ic_broken_image)
            else{
                imageView.load(imgUrl){
                    crossfade(true)
                    placeholder(R.drawable.loading_animation)
                    error(R.drawable.ic_broken_image)
                    scale(Scale.FILL)
                }
            }
            tvTitle.text = "Title - "+intent.getStringExtra("title")
            tvIndex.text = "Index - "+intent.getStringExtra("index")
            tvDimensions.text = "Dimensions - "+intent.getStringExtra("dimension")
        }
    }
}