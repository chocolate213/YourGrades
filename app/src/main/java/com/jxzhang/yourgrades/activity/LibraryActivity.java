package com.jxzhang.yourgrades.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jxzhang.yourgrades.R;
import com.jxzhang.yourgrades.adapter.BookInfoAdapter;
import com.jxzhang.yourgrades.util.BookInfo;
import com.jxzhang.yourgrades.util.Constants;
import com.jxzhang.yourgrades.util.LibraryHTMLParse;
import com.jxzhang.yourgrades.util.LibraryHttpUtil;
import com.jxzhang.yourgrades.util.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J.X.Zhang on 2015/10/20.
 * 图书查询结果Activity
 */
public class LibraryActivity extends Activity {

    private SearchBookTask mAuthTask = null;
    private LinearLayout mBookProgressBar;
    private LinearLayout mBookListView;
    private LinearLayout mBookErrorPage;
    private String mBookTotalRecord;
    private TextView mBookTotalRecordInfo;

    private List<BookInfo> bookList = new ArrayList<BookInfo>();
    ListView mListView;
    private static int errorCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        //设置标题

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        //设置返回键
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("查询结果");
        //初始化控件
        mBookProgressBar = (LinearLayout) findViewById(R.id.book_progress_bar);
        mBookListView = (LinearLayout) findViewById(R.id.book_list_view);
        mBookErrorPage = (LinearLayout)findViewById(R.id.error_page_layout);
        mBookTotalRecordInfo = (TextView) findViewById(R.id.total_book_num);
        mListView = (ListView) findViewById(R.id.book_list);


        //获取书名
        String bookName = getSearchQuery(getIntent());

        //显示查询Progress
        showProgress(true);

        //启动异步线程
        mAuthTask = new SearchBookTask(bookName);
        mAuthTask.execute((Void) null);

    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getSearchQuery(intent);
    }

    private String getSearchQuery(Intent intent) {
        String bookName = "";
        if (intent == null)
            return null;
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            bookName = intent.getStringExtra(SearchManager.QUERY);
        }
        return bookName;
    }

    public void showProgress(final boolean show) {
        mBookProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        mBookListView.setVisibility(show ? View.GONE : View.VISIBLE);
    }
    public void showErrorPage(final boolean show){
        mBookProgressBar.setVisibility(show ? View.GONE : View.VISIBLE);
        mBookListView.setVisibility(show ? View.GONE : View.VISIBLE);
        mBookErrorPage.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    /**
     * 异步线程：查询书籍
     */
    public class SearchBookTask extends AsyncTask<Void, Void, Boolean> {

        private final String book_name;
        private LibraryHttpUtil libraryHttpUtil;
        public SearchBookTask(String _book_name) {
            book_name = _book_name;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            libraryHttpUtil = new LibraryHttpUtil(book_name);
            if(libraryHttpUtil.getBookInfo()){
                LibraryHTMLParse libraryHTMLParse = new LibraryHTMLParse();
                bookList = libraryHTMLParse.getList();
                if(bookList.size() == 0){
                    errorCode = Constants.LOGIN_NO_SEARCH_BOOKS;
                    return false;
                } else {
                    return true;
                }
            }
            errorCode = libraryHttpUtil.getErrorMessageCode();
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            mAuthTask = null;
            if (result) {
                BookInfoAdapter bookInfoAdapter = new BookInfoAdapter(LibraryActivity.this, R.layout.book_list_item, bookList);
                mListView.setAdapter(bookInfoAdapter);
                mListView.requestFocus();
                //共查找到（）本书；
                mBookTotalRecord = bookList.get(0).getB_10_TotalRecord();
                if(Integer.parseInt(mBookTotalRecord) > 500){
                    mBookTotalRecordInfo.setText("共为您找到" + mBookTotalRecord + "条搜索结果,以下为您展示前500条：");
                    Toast.makeText(LibraryActivity.this, "与关键词匹配的书目过多，请尝试重新键入合适的关键词以缩小检索范围", Toast.LENGTH_LONG).show();
                } else {
                    mBookTotalRecordInfo.setText("共为您找到" + mBookTotalRecord + "条搜索结果:");
                }

                //监听ListViewItem点击事件
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        BookInfo bookInfo = bookList.get(position);
                        String book_code = bookInfo.getB_11_Codes();
                        String book_name = bookInfo.getB_1_Name();
                        //打开图书结果Activity
                        Intent intent = new Intent(LibraryActivity.this, BookInfoActivity.class);
                        intent.putExtra("book_code", book_code);
                        intent.putExtra("book_name", book_name);
                        startActivity(intent);
                    }
                });

                showProgress(false);
            } else {
                showProgress(false);
                showErrorPage(true);
                switch (errorCode){
                    case Constants.LOGIN_NETWORK_WRONG:
                        Toast.makeText(MyApplication.getContext(), "啊哦...网络好像出了点问题...", Toast.LENGTH_SHORT).show();
                        break;
                    case Constants.LOGIN_SYSTEM_WRONG:
                        Toast.makeText(MyApplication.getContext(), "系统错误，请重试", Toast.LENGTH_SHORT).show();
                        break;
                    case Constants.LOGIN_NO_SEARCH_BOOKS:
                        Toast.makeText(MyApplication.getContext(), "没有找到相关书籍，请检查书名是否正确或尝试重新检索", Toast.LENGTH_SHORT).show();
                    default:
                        break;
                }
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
            showErrorPage(true);
        }

    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
