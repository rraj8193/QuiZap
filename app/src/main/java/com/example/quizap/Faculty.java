package com.example.quizap;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizap.Adapter.QuizListAdapter;
import com.example.quizap.model.QuizListModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.jetbrains.annotations.NotNull;

public class Faculty extends AppCompatActivity {

    Toolbar toolbar;
    private FirebaseAuth fauth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference quizesref = db.collection("Quizes");
    private QuizListAdapter quizListAdapter;
    FirebaseUser user = fauth.getCurrentUser();
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty);
        toolbar = findViewById(R.id.facultytoolbar);
        setSupportActionBar(toolbar);
        email = user.getEmail();
        setUpRecyclerView();
    }
    private void setUpRecyclerView(){
        Query query = quizesref.whereEqualTo("email",email);
        FirestoreRecyclerOptions<QuizListModel> options = new FirestoreRecyclerOptions.Builder<QuizListModel>().setQuery(query,QuizListModel.class).build();
        quizListAdapter = new QuizListAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.facultypagerecyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(quizListAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
                quizListAdapter.deleteQuestion(viewHolder.getLayoutPosition());
            }
        }).attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        quizListAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        quizListAdapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.addquiz){
            startActivity(new Intent(getApplicationContext(),QuizDetail.class));
        }
        else if (id == R.id.logout){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(),LogIn.class));
            finish();
        }
        else if(id == R.id.clearall){
            Toast.makeText(this, "You Clicked Clear", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

}