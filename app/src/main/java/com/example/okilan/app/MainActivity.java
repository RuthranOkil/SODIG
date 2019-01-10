package com.example.okilan.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    Button b;
    EditText txtview;
    ImageView i;
    String s,s1;
    TextView t1;
    char ch1[] = new char[3];
    char ch[] = new char[500];
    int c,c1 = 0;
    private int REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b = findViewById(R.id.button);
        i = findViewById(R.id.imageView);
        txtview = findViewById(R.id.editText);
        t1 = findViewById(R.id.textView);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,"Select picture"),REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData()!= null){
            Uri uri = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                i.setImageBitmap(bitmap);
                TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
                if(!textRecognizer.isOperational()){

                }else{
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray items = textRecognizer.detect(frame);
                    StringBuilder strBuilder = new StringBuilder();
                    for (int i = 0; i < items.size(); i++) {
                        TextBlock item = (TextBlock) items.valueAt(i);
                        strBuilder.append(item.getValue());
                        strBuilder.append("/");
                        for (Text line : item.getComponents()) {
                            //extract scanned text lines here
                            Log.v("lines", line.getValue());
                            for (Text element : line.getComponents()) {
                                //extract scanned text words here
                                Log.v("element", element.getValue());
                            }
                        }
                    }
                    txtview.setText(strBuilder.toString().substring(0, strBuilder.toString().length() - 1));
                    s = strBuilder.toString();
                    ch = s.toCharArray();
                    c = s.length()-1;
                    for(int i=2;i>=0;i--){
                        if(ch[c]!='/' || ch[c]!='o' || ch[c]!=' ') {
                            ch1[i] = ch[c];
                            Log.v("ch1:", String.valueOf(ch1[i]));
                            c--;
                            c1++;
                        }else{
                            break;
                        }
                    }if(c1==3){
                        t1.setText("Count :" + String.valueOf(ch1[0]) + String.valueOf(ch1[1]) + String.valueOf(ch1[2]));
                    }else if(c1 == 2){
                        t1.setText("count : "+String.valueOf(ch1[1])+String.valueOf(ch1[2]));
                    }else{
                        t1.setText(" ");
                        t1.setText("Count : "+String.valueOf(ch1[2]));
                    }
                }
            }  catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
