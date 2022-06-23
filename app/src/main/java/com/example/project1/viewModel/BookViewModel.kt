package com.example.project1.viewModel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project1.recyclerview.BookAdapter
import com.example.project1.R
import com.example.project1.data.Book
import com.example.project1.data.KakaoServiceImpl
import com.example.project1.data.ResponseWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.lang.StringBuilder

class BookViewModel: ViewModel() {
    lateinit var bookListLiveData: MutableLiveData<ArrayList<Book>>
    lateinit var bookList: ArrayList<Book>
    lateinit var bookAdapter: BookAdapter
    init {
        bookList = ArrayList()
        bookListLiveData = MutableLiveData()
        bookList.add(Book(
            title = "1",
            url = "@+id/b_publisher",
            publisher = "1",
            price = 1
        ))
        bookListLiveData.value = bookList
        bookAdapter = BookAdapter(bookListLiveData.value!!)
//        bookAdapter.listener = object : BookAdapter.OnClickListener{
//            override fun onClick(book: Book, position: Int) {
//                changeVisibility(position, book.visibility)
//            }
//        }
    }

    fun changePosition(oldPosition: Int, newPosition: Int) {
        bookList[oldPosition] = bookList[newPosition].also { bookList[newPosition] = bookList[oldPosition] }
        bookListLiveData.value = bookList
    }

    fun deleteBook(position: Int) {
        bookList.removeAt(position)
        bookListLiveData.value = bookList
    }
//    fun changeVisibility(position: Int, visibility : Int) {
//        when(visibility){
//            View.VISIBLE -> bookList[position].visibility = View.GONE
//            View.GONE -> bookList[position].visibility = View.VISIBLE
//        }
//        bookListLiveData.value = bookList
//    }
    fun addBook(book: Book) {
        val position = bookAdapter.itemCount
        bookList.add(book)
        bookListLiveData.value = bookList
    }
}