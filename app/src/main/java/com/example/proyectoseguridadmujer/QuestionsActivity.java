package com.example.proyectoseguridadmujer;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

public class QuestionsActivity extends AppCompatActivity
{
    int Type [], Variables [], Counter = 0, ControlCounter = 0, Response, Quiz, ControlQuiz;
    ImageView mImageTestComplete;
    TextView Question, QuestionNumber;
    Button TrueAnswer, SometimesAnswer, SeldomAnswer, NeverAnswer, ResultsButton;
    String [] Questions, Values, ControlQuestions;
    boolean isControlQuestion = false;
    boolean [] isTestTaken;
    Resources resources;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        //Action Bar Color:
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.Action_Bar_Color)));

        //WiringUp
        mImageTestComplete = findViewById(R.id.test_complete_image);
        Question = findViewById(R.id.Question);
        QuestionNumber = findViewById(R.id.QuestionNumber);
        TrueAnswer = findViewById(R.id.TrueAnswer);
        SometimesAnswer = findViewById(R.id.SometimesAnswer);
        SeldomAnswer = findViewById(R.id.SeldomAnswer);
        NeverAnswer = findViewById(R.id.NeverAnswer);
        ResultsButton = findViewById(R.id.ResultsButton);
        resources = getResources();

        mImageTestComplete.setVisibility(View.INVISIBLE);
        ResultsButton.setVisibility(View.INVISIBLE);

        Type = new int[4];
        Variables = new int[5];
        isTestTaken = new boolean[4];
        for (int i = 0; i < 4; i++)
        {
            isTestTaken [i] = false;
        }

        TrueAnswer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Response = 3;
                if (isControlQuestion)
                {
                    ControlAnswer();
                }
                else
                {
                    Answer();
                }
            }
        });

        SometimesAnswer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Response = 2;
                if (isControlQuestion)
                {
                    ControlAnswer();
                }
                else
                {
                    Answer();
                }
            }
        });

        SeldomAnswer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Response = 1;
                Answer();
            }
        });

        NeverAnswer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Response = 0;
                Answer();
            }
        });

        ResultsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), TestResultsActivity.class);
                intent.putExtra("Test", Type);
                intent.putExtra("TipoViolencia", Variables);
                intent.putExtra("Quiz", Quiz);
                intent.putExtra("TestContestado", isTestTaken);
                startActivity(intent);
                finish();

                /*
                if (Quiz == 0)
                {
                    Toast.makeText(getApplicationContext(),  Type [0] + " " + Type [1] + " " + Type [2] + " " + Type [3] + " " + Variables [0]
                            + " " + Variables [1] + " " + Variables [2] + " " + Variables [3] + " " + Variables [4], Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),  Type [Quiz - 1] + " " + Variables [0] + " " + Variables [1] + " " + Variables [2]
                            + " " + Variables [3] + " " + Variables [4], Toast.LENGTH_SHORT).show();
                }
                 */
            }
        });

        GetQuestions();
        QuizStart();
    }

    void GetQuestions ()
    {
        Bundle bundle = getIntent().getExtras();
        Questions = bundle.getStringArray("Preguntas");
        Values = bundle.getStringArray("Valores");
        if (Values == null)
        {
            Values = new String[12];
        }
        Quiz = bundle.getInt("Quiz");
    }

    void QuizStart ()
    {
        if (Quiz == 0)
        {
            if (ControlCounter == 0)
            {
                isControlQuestion = true;
                ControlQuestions = Questions;
                mImageTestComplete.setVisibility(View.INVISIBLE);
                ResultsButton.setVisibility(View.INVISIBLE);
                SeldomAnswer.setVisibility(View.INVISIBLE);
                NeverAnswer.setVisibility(View.INVISIBLE);
                Question.setText(ControlQuestions[ControlCounter]);
                QuestionNumber.setText("Pregunta " + (ControlCounter + 1) + " de 4 preguntas");
                SometimesAnswer.setText("No");
            }
            else
            {
                if (isControlQuestion)
                {
                    if (ControlCounter == 4)
                    {
                        TrueAnswer.setVisibility(View.INVISIBLE);
                        SometimesAnswer.setVisibility(View.INVISIBLE);
                        SeldomAnswer.setVisibility(View.INVISIBLE);
                        NeverAnswer.setVisibility(View.INVISIBLE);
                        mImageTestComplete.setVisibility(View.VISIBLE);
                        ResultsButton.setVisibility(View.VISIBLE);
                        Question.setText(" ");
                    }
                    else
                    {
                        mImageTestComplete.setVisibility(View.INVISIBLE);
                        ResultsButton.setVisibility(View.INVISIBLE);
                        SeldomAnswer.setVisibility(View.INVISIBLE);
                        NeverAnswer.setVisibility(View.INVISIBLE);
                        Question.setText(ControlQuestions[ControlCounter]);
                        QuestionNumber.setText("Pregunta " + (ControlCounter + 1) + " de 4 preguntas");
                        SometimesAnswer.setText("No");
                    }
                }
                else
                {
                    if (Counter == 12)
                    {
                        isControlQuestion = true;
                        if (ControlCounter == 4)
                        {
                            TrueAnswer.setVisibility(View.INVISIBLE);
                            SometimesAnswer.setVisibility(View.INVISIBLE);
                            SeldomAnswer.setVisibility(View.INVISIBLE);
                            NeverAnswer.setVisibility(View.INVISIBLE);
                            mImageTestComplete.setVisibility(View.VISIBLE);
                            ResultsButton.setVisibility(View.VISIBLE);
                            Question.setText(" ");
                        }
                        else
                        {
                            SeldomAnswer.setVisibility(View.INVISIBLE);
                            NeverAnswer.setVisibility(View.INVISIBLE);
                            SometimesAnswer.setText("No");
                            Question.setText(ControlQuestions[ControlCounter]);
                            QuestionNumber.setText("Pregunta " + (ControlCounter + 1) + " de 4 preguntas");
                        }
                    }
                    else
                    {
                        SeldomAnswer.setVisibility(View.VISIBLE);
                        NeverAnswer.setVisibility(View.VISIBLE);
                        SometimesAnswer.setText(R.string.SometimesAnswer);
                        Question.setText(Questions[Counter]);
                        QuestionNumber.setText("Pregunta " + (Counter + 1) + " de 12 preguntas");
                    }
                }
            }
        }
        else
        {
            if (Counter == 12)
            {
                TrueAnswer.setVisibility(View.INVISIBLE);
                SometimesAnswer.setVisibility(View.INVISIBLE);
                SeldomAnswer.setVisibility(View.INVISIBLE);
                NeverAnswer.setVisibility(View.INVISIBLE);
                mImageTestComplete.setVisibility(View.VISIBLE);
                ResultsButton.setVisibility(View.VISIBLE);
                Question.setText(" ");
            }
            else
            {
                Question.setText(Questions[Counter]);
                QuestionNumber.setText("Pregunta " + (Counter + 1) + " de 12 preguntas");
            }
        }
    }

    void Answer ()
    {
        int Valores = Integer.parseInt(Values [Counter]);

        if (Quiz == 0)
        {
            Type [ControlQuiz] += Response;
        }
        else
        {
            Type [Quiz - 1] += Response;
        }

        Variables [Valores] += Response;
        Counter++;
        QuizStart();
    }

    void ControlAnswer ()
    {
        if (Response == 3)
        {
            isTestTaken[ControlCounter] = true;
            isControlQuestion = false;
            Counter = 0;
            switch (ControlCounter)
            {
                case 0:
                {
                    Questions = resources.getStringArray(R.array.FamiliarViolenceQuestions);
                    Values = resources.getStringArray(R.array.FamiliarViolenceValues);
                    ControlQuiz = 0;
                    break;
                }
                case 1:
                {
                    Questions = resources.getStringArray(R.array.CoupleViolenceQuestions);
                    Values = resources.getStringArray(R.array.CoupleViolenceValues);
                    ControlQuiz = 1;
                    break;
                }
                case 2:
                {
                    Questions = resources.getStringArray(R.array.FriendViolenceQuestions);
                    Values = resources.getStringArray(R.array.FriendViolenceValues);
                    ControlQuiz = 2;
                    break;
                }
                case 3:
                {
                    Questions = resources.getStringArray(R.array.WorkViolenceQuestions);
                    Values = resources.getStringArray(R.array.WorkViolenceValues);
                    ControlQuiz = 3;
                    break;
                }
                default:
                {
                    break;
                }
            }
        }
        else
        {
            isControlQuestion = true;
        }
        ControlCounter++;
        QuizStart();
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = NavUtils.getParentActivityIntent(QuestionsActivity.this);
        startActivity(intent);
        finish();
    }
}