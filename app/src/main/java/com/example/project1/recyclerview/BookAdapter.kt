package com.example.project1.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project1.data.Book
import com.example.project1.databinding.BookRowBinding

class BookAdapter(private var bookList: ArrayList<Book>) : RecyclerView.Adapter<BookAdapter.ViewHolder>() {

    interface OnClickListener {
        fun onClick(book: Book, position: Int)
    }
    var listener: OnClickListener? = null
    inner class ViewHolder(private val binding: BookRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(title: String, url: String, publisher: String, price: Int) {
            binding.run {
                Glide
                    .with(bThumbnail)
                    .load(url)
                    .into(bThumbnail)
                bTitle.text = title
                bPublisher.text = publisher
                bPrice.text = price.toString() + "원"
                //bTitle.visibility = visibility
                //bPublisher.visibility = visibility
                //bPrice.visibility = visibility
            }
        }
        init {
            binding.bThumbnail.setOnClickListener {
                listener?.onClick(bookList[adapterPosition],adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = BookRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(bookList[position].title, bookList[position].url, bookList[position].publisher, bookList[position].price)
        //holder.binding.engText.text = wordList[position].eng
        //holder.binding.korText.text = wordList[position].kor
    }

    override fun getItemCount(): Int = bookList.size
}