package com.example.proyectoseguridadmujer;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

public class TestResultsActivity extends AppCompatActivity
{
    TextView TestTaken, ViolenceResult, ViolenceTypeLayout, ViolenceType, InstitutionLabel;
    String [] TestLabel, ViolenceTypeLabel, RelationLabel;
    String ControlTestRelation = "", TestViolenceType = "";
    int Quiz, ViolenceCounter = 0, ViolenceHelper = 0;
    int [] Test, Violence, HViolence;
    boolean [] isTestTaken;
    boolean ControlTestSkip = false;
    Resources resources;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_results);

        //WiringUo
        TestTaken = findViewById(R.id.TestTaken);
        ViolenceResult = findViewById(R.id.ViolenceResult);
        ViolenceType = findViewById(R.id.ViolenceType);
        ViolenceTypeLayout = findViewById(R.id.ViolenceTypeLabel);
        InstitutionLabel = findViewById(R.id.RecommendedInstitution);
        resources = getResources();

        TestLabel = resources.getStringArray(R.array.TestType);
        ViolenceTypeLabel = resources.getStringArray(R.array.ViolenceType);
        RelationLabel = resources.getStringArray(R.array.RelationType);

        Test = new int[4];
        Violence = new int[5];
        isTestTaken = new boolean[4];

        GetInfo();
        ShowTestResults();
    }

    void GetInfo ()
    {
        Bundle bundle = getIntent().getExtras();
        Test = bundle.getIntArray("Test");
        Violence = bundle.getIntArray("TipoViolencia");
        Quiz = bundle.getInt("Quiz");
        isTestTaken = bundle.getBooleanArray("TestContestado");
    }

    void ShowTestResults ()
    {
        TestTaken.setText(TestLabel[Quiz]);

        if (Quiz != 0)
        {
            ViolenceResult.setText(RelationLabel [GetRelation(Quiz - 1)]);
        }
        else
        {
            for (int i = 1; i <= 4; i++)
            {
                if (isTestTaken [i -1] == false)
                {
                    if (i == 3)
                    {
                        ControlTestSkip = true;
                    }
                    continue;
                }
                ControlTestRelation += TestLabel[i] + ": " + RelationLabel [GetRelation(i - 1)];
                if (i != 4)
                {
                    ControlTestRelation += "\n \n";
                }
            }
            ViolenceResult.setText(ControlTestRelation);
            ViolenceType.setVisibility(View.VISIBLE);
            ViolenceTypeLayout.setVisibility(View.VISIBLE);
            InstitutionLabel.setVisibility(View.VISIBLE);
        }

        if (ControlTestSkip)
        {
            ViolenceResult.setText(resources.getString(R.string.DefaultRelation));
            ViolenceType.setVisibility(View.INVISIBLE);
            ViolenceTypeLayout.setVisibility(View.INVISIBLE);
            InstitutionLabel.setVisibility(View.INVISIBLE);
        }

        for (int i = 1; i < 5; i++)
        {
            if (Violence [i] > Violence [ViolenceCounter])
            {
                ViolenceCounter = i;
            }
        }
        for (int i = 0; i < 5; i++)
        {
            if (Violence [i] == Violence [ViolenceCounter])
            {
                ViolenceHelper++;
            }
        }
        HViolence = new int[ViolenceHelper];
        ViolenceHelper = 0;
        for (int i = 0; i < 5; i++)
        {
            if (Violence [i] == Violence [ViolenceCounter])
            {
                HViolence[ViolenceHelper] = i;
                ViolenceHelper++;
            }
        }
        if (HViolence.length == 1)
        {
            ViolenceType.setText(ViolenceTypeLabel [HViolence [0]]);
        }
        else
        {
            for (int i = 0; i < HViolence.length; i++)
            {
                TestViolenceType += ViolenceTypeLabel[HViolence[i]];
                if (i != HViolence.length - 1)
                {
                    TestViolenceType += "\n \n";
                }
            }
            ViolenceType.setText(TestViolenceType);
        }
    }

    int GetRelation (int TestCounter)
    {
        if (Test [TestCounter] >= 28 && Test [TestCounter] <= 36)
        {
            return 0;
        }
        else if (Test [TestCounter] >= 19 && Test [TestCounter] <= 27)
        {
            return 1;
        }
        else if (Test [TestCounter] >= 10 && Test [TestCounter] <= 18)
        {
            ViolenceType.setVisibility(View.INVISIBLE);
            ViolenceTypeLayout.setVisibility(View.INVISIBLE);
            InstitutionLabel.setVisibility(View.INVISIBLE);
            return 2;
        }
        else
        {
            ViolenceType.setVisibility(View.INVISIBLE);
            ViolenceTypeLayout.setVisibility(View.INVISIBLE);
            InstitutionLabel.setVisibility(View.INVISIBLE);
            return 3;
        }
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = NavUtils.getParentActivityIntent(TestResultsActivity.this);
        startActivity(intent);
        finish();
    }
}
