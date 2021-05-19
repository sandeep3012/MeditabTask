package com.meditab.task.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.meditab.task.R
import com.meditab.task.databinding.ListItemImageBinding
import com.meditab.task.remote.entity.PagesInfo

class ImageSearchAdapter(private var imageSelectionlistener: (PagesInfo) -> Unit)
    : ListAdapter<PagesInfo,RecyclerView.ViewHolder>(PageDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ImageSearchViewHolder(ListItemImageBinding.inflate(
            LayoutInflater.from(parent.context), parent, false), imageSelectionlistener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val page = getItem(position)
        (holder as ImageSearchViewHolder).bind(page)
    }

    inner class ImageSearchViewHolder(
        private val binding: ListItemImageBinding,
        private val imageSelectionlistener : (PagesInfo) -> Unit
    ) :RecyclerView.ViewHolder(binding.root){
        init {
            binding.setSelectImageListener {
                binding.pageImages.let { pages ->
                    imageSelectionlistener.invoke(getItem(position))
                }
            }
        }

        fun bind(item: PagesInfo){
            binding.apply {
                val imageUrl = item.thumbnail?.source
                if (imageUrl.isNullOrBlank())
                    ivSearchImage.load(R.drawable.ic_broken_image)
                else{
                    ivSearchImage.load(imageUrl){
                        crossfade(true)
                        placeholder(R.drawable.loading_animation)
                        error(R.drawable.ic_broken_image)
                        scale(Scale.FILL)
                    }
                }
            }
        }
    }

    private class PageDiffCallback : DiffUtil.ItemCallback<PagesInfo>(){
        override fun areItemsTheSame(oldItem: PagesInfo, newItem: PagesInfo): Boolean {
            return oldItem.pageid == newItem.pageid
        }
        override fun areContentsTheSame(oldItem: PagesInfo, newItem: PagesInfo): Boolean {
            return oldItem.pageid == newItem.pageid
        }
    }
}