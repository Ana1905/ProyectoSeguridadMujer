package com.example.proyectoseguridadmujer;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import javax.mail.Quota;

public class TestActivity extends AppCompatActivity
{

    Button TestButton, FamiliarTestButton, CoupleTestButton, FriendTestButton, WorkTestButton;
    String [] Questions, Values;
    Resources resources;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_test);

        //Wiring Up
        TestButton = findViewById(R.id.TestButton);
        FamiliarTestButton = findViewById(R.id.FamiliarTestButton);
        CoupleTestButton = findViewById(R.id.CoupleTestButton);
        FriendTestButton = findViewById(R.id.FriendTestButton);
        WorkTestButton = findViewById(R.id.WorkTestButton);
        resources = getResources();

        TestButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Questions = resources.getStringArray(R.array.ControlQuestions);
                Intent intent = new Intent(getApplicationContext(), QuestionsActivity.class);
                intent.putExtra("Preguntas", Questions);
                intent.putExtra("Quiz", 0);
                startActivity(intent);
                finish();
            }
        });

        FamiliarTestButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Questions = resources.getStringArray(R.array.FamiliarViolenceQuestions);
                Values = resources.getStringArray(R.array.FamiliarViolenceValues);
                Intent intent = new Intent(getApplicationContext(), QuestionsActivity.class);
                intent.putExtra("Preguntas", Questions);
                intent.putExtra("Valores", Values);
                intent.putExtra("Quiz", 1);
                startActivity(intent);
                finish();
            }
        });

        CoupleTestButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Questions = resources.getStringArray(R.array.CoupleViolenceQuestions);
                Values = resources.getStringArray(R.array.CoupleViolenceValues);
                Intent intent = new Intent(getApplicationContext(), QuestionsActivity.class);
                intent.putExtra("Preguntas", Questions);
                intent.putExtra("Valores", Values);
                intent.putExtra("Quiz", 2);
                startActivity(intent);
                finish();
            }
        });

        FriendTestButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Questions = resources.getStringArray(R.array.FriendViolenceQuestions);
                Values = resources.getStringArray(R.array.FriendViolenceValues);
                Intent intent = new Intent(getApplicationContext(), QuestionsActivity.class);
                intent.putExtra("Preguntas", Questions);
                intent.putExtra("Valores", Values);
                intent.putExtra("Quiz", 3);
                startActivity(intent);
                finish();
            }
        });

        WorkTestButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Questions = resources.getStringArray(R.array.WorkViolenceQuestions);
                Values = resources.getStringArray(R.array.WorkViolenceValues);
                Intent intent = new Intent(getApplicationContext(), QuestionsActivity.class);
                intent.putExtra("Preguntas", Questions);
                intent.putExtra("Valores", Values);
                intent.putExtra("Quiz", 4);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = NavUtils.getParentActivityIntent(TestActivity.this);
        startActivity(intent);
        finish();
    }
}
