package com.jxzhang.yourgrades.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jxzhang.yourgrades.R;
import com.jxzhang.yourgrades.util.MyApplication;

import org.apache.http.util.EncodingUtils;

/**
 * Created by J.X.Zhang on 2015/9/22.
 * 课表Fragment
 */
public class TimeTableFragment extends Fragment {

    private static String classCode;
    private static String timeTableTypeCode;

    private TextView timeTableInfoText;

    private Button button_commit;
    private WebView web_view;
    private Spinner spinnerClassName;
    private Spinner spinnerTimeTableType;

    private String[] classNames = new String[]{"13级物联网工程班", "14级物联网班"};
    private String[] classCodes = new String[]{"2013031901", "2014031901"};
    private String[] timeTableTypes = new String[]{"格式1", "格式2"};
    private String[] timeTableCodes = new String[]{"1", "2"};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_time_table, container, false);

        timeTableInfoText = (TextView)view.findViewById(R.id.time_table_info_text);

        /**
         * 课表种类选择
         */
        spinnerTimeTableType = (Spinner) view.findViewById(R.id.spinner_time_table_type);
        ArrayAdapter<String> timeTableTypeAdapter = new ArrayAdapter<String>(MyApplication.getContext(), R.layout.spinner_item, timeTableTypes);
        spinnerTimeTableType.setAdapter(timeTableTypeAdapter);
        spinnerTimeTableType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                int index = parent.getSelectedItemPosition();
                timeTableTypeCode = timeTableCodes[index];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        /**
         * 班级选择
         */
        spinnerClassName = (Spinner) view.findViewById(R.id.spinner_class_name);
        ArrayAdapter<String> classNameAdapter = new ArrayAdapter<String>(MyApplication.getContext(), R.layout.spinner_item, classNames);
        spinnerClassName.setAdapter(classNameAdapter);
        spinnerClassName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                int index = parent.getSelectedItemPosition();
                classCode = classCodes[index];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        /**
         * 配置WebView
         */
        web_view = (WebView) view.findViewById(R.id.web_view_time_table);
        web_view.getSettings().setJavaScriptEnabled(true);
        web_view.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                Toast.makeText(MyApplication.getContext(), "Oh no! " + description,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        /**
         * 配置提交代码
         */
        button_commit = (Button) view.findViewById(R.id.button_commit);
        button_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeTableInfoText.setVisibility(View.GONE);
                web_view.setVisibility(View.VISIBLE);
                String url = "http://jwgl.hhhxy.cn/ZNPK/KBFB_ClassSel_rpt.aspx";
                String postDate = "Sel_XNXQ=20150&Sel_XZBJ=" + classCode + "&type=" + timeTableTypeCode;
                web_view.postUrl(url,
                        EncodingUtils.getBytes(postDate, "BASE64"));
            }
        });
        return view;
    }
}