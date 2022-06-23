package com.example.project1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project1.data.Book
import com.example.project1.data.KakaoServiceImpl
import com.example.project1.data.ResponseWrapper
import com.example.project1.databinding.ActivityMainBinding
import com.example.project1.recyclerview.BookItemTouchHelperCallback
import com.example.project1.viewModel.BookViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.*
import java.lang.RuntimeException
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    val binding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val bookViewModel : BookViewModel by viewModels()
    private lateinit var  itemTouchHelper: ItemTouchHelper
    companion object {
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
                CoroutineScope(Dispatchers.Main).launch {
//                    val host = "https://dapi.kakao.com"
//                    val path = "/v2/search/blog"
//                    val query = "?query=맛집"
//                    val apiUrl = "$host$path$query"
//
//                    val requestHeaders = HashMap<String, String>()
//                    requestHeaders["Authorization"] = application.getString(R.string.kakao_api_key)
//
//                    var responseBody: String ?
//                    withContext(Dispatchers.IO) {
//                        responseBody = get(apiUrl, requestHeaders)
//                    }
//
//                    responseBodyTextView.text = responseBody
                    //Log.d(TAG, "onCreate": $"{responseBody}")
                    var response: Response<ResponseWrapper>
                    withContext(Dispatchers.IO) {
                        response = KakaoServiceImpl.kakaoService.getBlogs(
                            application.getString(R.string.kakao_api_key),
                            "한국",
                            "accuracy"
                        )
                    }
                    bookViewModel.deleteBook(0)
                    response.body()?.documents?.forEach {
                        bookViewModel.addBook(
                            Book(
                                title = "${it.title}",
                                url = "${it.thumbnail}",
                                publisher = "${it.publisher}",
                                price = it.price
                            )
                        )
                    }
                }

        setObserver()
        initLayout()

    }

    private fun initLayout(){
        binding.run{
            recyclerView.run {
                layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
                adapter = bookViewModel.bookAdapter
            }
        }
        itemTouchHelper = ItemTouchHelper(BookItemTouchHelperCallback(bookViewModel))
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    private fun setObserver(){
        bookViewModel.bookListLiveData.observe(this@MainActivity){
            bookViewModel.bookAdapter.notifyDataSetChanged()
        }
    }

    private fun get(apiUrl: String, requestHeaders: HashMap<String,String>) :String{

        val url = URL(apiUrl)
        val connection = url.openConnection() as HttpURLConnection
        return try {
            connection.requestMethod = "GET"
            requestHeaders.forEach{(key, value) ->
                connection.setRequestProperty(key,value)
            }
            // 2 way handshaking
            if (connection.responseCode == HttpURLConnection.HTTP_OK){
                readBody(connection.inputStream)
            }else {
                readBody(connection.errorStream)
            }
        } catch (e :IOException){
            throw RuntimeException("API response failed")
        } finally {
            connection.disconnect() //4 way handshaking
        }
    }

    private fun readBody(inputStream: InputStream) : String {
        val streamReader = InputStreamReader(inputStream)
        try {
            BufferedReader(streamReader).use { lineReader ->
                val responseBody = StringBuilder()
                var line : String?
                while(lineReader.readLine().also{ line = it } != null) {
                    responseBody.append(line)
                }
                return responseBody.toString()
            }
        }catch (e :IOException){
            throw RuntimeException("API response body read failed")
        }
    }

}