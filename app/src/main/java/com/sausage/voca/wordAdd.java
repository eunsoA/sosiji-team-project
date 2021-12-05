package com.sausage.voca;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class wordAdd extends AppCompatActivity {

    private TextView textView;
    private EditText editText1, editText2;
    private ImageButton word_add_back;
    private TextView word_add_add;
    private TextView json_test_text;
    String wordbookID = "0";

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    //[은소]test - edit text에 입력된 내용을 realtime database에 업로드해봄.
    //데이터베이스에서 데이터를 읽고 쓰려면 DataReference의 인스턴스가 필요하다.
//    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    //child()는 데이터가 있을 위치의 이름을 정해준다.
//    DatabaseReference conditionRef = mRootRef.child("test");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_add);
        wordbookID = getIntent().getStringExtra("categoryID");

        editText1 = findViewById(R.id.english);
        editText2 = findViewById(R.id.korean);

        word_add_back = findViewById(R.id.word_add_back);
        word_add_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        word_add_add = findViewById(R.id.wordAddCompelete);
        word_add_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String english = editText1.getText().toString();
                String korean = editText2.getText().toString();
                Map<String, Object> wordcardData = new HashMap<>();
                wordcardData.put("word", english);
                wordcardData.put("mean1", korean);
                wordcardData.put("mean2", null);
                wordcardData.put("mean3", null);
                wordcardData.put("memorization", 0);


                DocumentReference wordBooksDoc = db.collection("users").document(user.getUid()).collection("wordbooks").document(wordbookID);
                wordBooksDoc.get().addOnCompleteListener((task) -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Map<String, Object> wordList = (Map<String, Object>) document.getData().get("wordlist");
                            int wordBookNum = wordList.size();
                            wordList.put(String.valueOf(wordBookNum),wordcardData);
                            wordBooksDoc.update("wordlist", wordList);
                        } else {
                            Log.i("mytag", "No such document");
                        }
                    } else {
                        Log.i("mytag", "get failed with " + task.getException());
                    }
                });
                //TODO 2안 해보고 안되면 3안으로 ㄱㄱ. 3안은 CategoryAdd.java 참고
                finish();
            }
        });

    }

}