package com.example.proyectoseguridadmujer;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TestResultsActivity extends AppCompatActivity
{
    TextView TestTaken, ViolenceResult, ViolenceTypeLayout, ViolenceType, InstitutionLabel, TipsLabel;
    String [] TestLabel, ViolenceTypeLabel, RelationLabel;
    String ControlTestRelation = "", TestViolenceType = "";
    int Quiz, ViolenceCounter = 0, ViolenceHelper = 0;
    int [] Test, Violence, HViolence;
    boolean [] isTestTaken;
    boolean ControlTestSkip = false;
    Resources resources;

    RecyclerView mRecyclerInstituciones, mRecyclerTips;

    ArrayList<Institutions> mListaInstituciones = new ArrayList<Institutions>();
    ArrayList<Tip> mListaTips = new ArrayList<Tip>();

    RequestQueue requestQueue;
    //ArrayList<Tip> mListaTips = new ArrayList<Tip>();

    boolean isAbusive = false;
    ArrayList<String> mArrayVariablesObtenidas = new ArrayList<String>();

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
        TipsLabel = findViewById(R.id.tipsLabel);
        resources = getResources();

        mRecyclerInstituciones = findViewById(R.id.recyclerinstitucionesresultado);
        mRecyclerInstituciones.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerTips = findViewById(R.id.recyclertipsresultado);
        mRecyclerTips.setLayoutManager(new LinearLayoutManager(this));

        TestLabel = resources.getStringArray(R.array.TestType);
        ViolenceTypeLabel = resources.getStringArray(R.array.ViolenceType);
        RelationLabel = resources.getStringArray(R.array.RelationType);

        Test = new int[4];
        Violence = new int[5];
        isTestTaken = new boolean[4];

        GetInfo();
        ShowTestResults();
        definirVariables();
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
            isAbusive = true;
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

    public void definirVariables(){
        if(!mArrayVariablesObtenidas.isEmpty()){
            mArrayVariablesObtenidas.clear();
        }

        for(int i=0; i<HViolence.length; i++){

            if(isAbusive){
                //Relacionar variable con categoria del tip:
                switch (HViolence[i]){
                    case 0:
                        if(!mArrayVariablesObtenidas.contains("4")){
                            mArrayVariablesObtenidas.add("4");
                        }
                        if(!mArrayVariablesObtenidas.contains("5")) {
                            mArrayVariablesObtenidas.add("5");
                        }
                        break;
                    case 1:
                        if(!mArrayVariablesObtenidas.contains("1")){
                            mArrayVariablesObtenidas.add("1");
                        }
                        break;
                    case 2:
                        if(!mArrayVariablesObtenidas.contains("2")){
                            mArrayVariablesObtenidas.add("2");
                        }
                        break;
                    case 3:
                        if(!mArrayVariablesObtenidas.contains("1")){
                            mArrayVariablesObtenidas.add("1");
                        }
                        if(!mArrayVariablesObtenidas.contains("3")){
                            mArrayVariablesObtenidas.add("3");
                        }
                        break;
                    case 4:
                        if(!mArrayVariablesObtenidas.contains("2")){
                            mArrayVariablesObtenidas.add("2");
                        }
                        if(!mArrayVariablesObtenidas.contains("4")){
                            mArrayVariablesObtenidas.add("4");
                        }
                        break;
                }
            }
            else{
                //Relacionar variable con categoria del tip de prevencion:
                //Relacionar variable con categoria del tip:
                switch (HViolence[i]){
                    case 0:
                        if(!mArrayVariablesObtenidas.contains("5")){
                            mArrayVariablesObtenidas.add("5");
                        }
                        if(!mArrayVariablesObtenidas.contains("7")){
                            mArrayVariablesObtenidas.add("7");
                        }
                        break;
                    case 1:
                        if(!mArrayVariablesObtenidas.contains("4")){
                            mArrayVariablesObtenidas.add("4");
                        }
                        break;
                    case 2:
                        if(!mArrayVariablesObtenidas.contains("6")){
                            mArrayVariablesObtenidas.add("6");
                        }
                        break;
                    case 3:
                        if(!mArrayVariablesObtenidas.contains("1")){
                            mArrayVariablesObtenidas.add("1");
                        }
                        if(!mArrayVariablesObtenidas.contains("3")){
                            mArrayVariablesObtenidas.add("3");
                        }
                        break;
                    case 4:
                        if(!mArrayVariablesObtenidas.contains("2")){
                            mArrayVariablesObtenidas.add("2");
                        }
                        break;
                }
            }

            //Toast.makeText(getApplicationContext(), String.valueOf(HViolence[i]), Toast.LENGTH_SHORT).show();
        }

        if(!mArrayVariablesObtenidas.isEmpty()){
            if(isAbusive){
                String enlace = "https://seguridadmujer.com/app_movil/Institucion/obtenerInstituciones.php";
                for (int i=0; i<mArrayVariablesObtenidas.size(); i++){
                    if(i==0){
                        enlace = enlace + "?ID" + (i+1) + "=" + mArrayVariablesObtenidas.get(i);
                    }
                    else{
                        enlace = enlace + "&ID" + (i+1) + "=" + mArrayVariablesObtenidas.get(i);
                    }
                }
                //Toast.makeText(getApplicationContext(), enlace, Toast.LENGTH_LONG).show();
                obtenerInstituciones(enlace);
            }
            else{
                mRecyclerInstituciones.setVisibility(View.GONE);
                InstitutionLabel.setVisibility(View.GONE);
            }

            String enlace = "";
            if(isAbusive){
                enlace = "https://seguridadmujer.com/app_movil/Institucion/obtenerTips.php";

            }
            else{
                enlace = "https://seguridadmujer.com/app_movil/Institucion/obtenerTipsPrevencion.php";
            }
            for (int i=0; i<mArrayVariablesObtenidas.size(); i++){
                if(i==0){
                    enlace = enlace + "?ID" + (i+1) + "=" + mArrayVariablesObtenidas.get(i);
                }
                else{
                    enlace = enlace + "&ID" + (i+1) + "=" + mArrayVariablesObtenidas.get(i);
                }
            }
            //Toast.makeText(getApplicationContext(), enlace, Toast.LENGTH_LONG).show();
            obtenerTips(enlace);
        }


    }

    public void obtenerInstituciones(String URL){
        //Vacia la lista:
        if(!mListaInstituciones.isEmpty()){
            mListaInstituciones.clear();
        }

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                JSONObject jsonObject = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        jsonObject = jsonArray.getJSONObject(i);

                        Institutions institution = new Institutions();
                        institution.setID_Institucion(jsonObject.getInt("ID_Institucion"));
                        institution.setNombre(jsonObject.getString("Nombre"));
                        institution.setArea(jsonObject.getString("Area"));
                        institution.setRutaImagenPresentacion(jsonObject.getString("RutaImagenPresentacion"));
                        institution.setDescripcion(jsonObject.getString("Descripcion"));
                        institution.setTelefono(jsonObject.getString("Telefono"));
                        institution.setPaginaWeb(jsonObject.getString("PaginaWeb"));
                        institution.setLatitud(jsonObject.getDouble("Latitud"));
                        institution.setLongitud(jsonObject.getDouble("Longitud"));
                        institution.setNombreCategoria(jsonObject.getString("NombreCategoria"));

                        mListaInstituciones.add(institution);

                        //Toast.makeText(getApplicationContext(), jsonObject.getString("RutaImagen"), Toast.LENGTH_SHORT).show();
                    }
                    catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                mostrarRedesSociales();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }
        );
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    private void mostrarRedesSociales(){
        //Toast.makeText(getApplicationContext(), String.valueOf(mListaInstituciones.size()), Toast.LENGTH_SHORT).show();
        if(!mListaInstituciones.isEmpty()){
            mRecyclerInstituciones.setVisibility(View.VISIBLE);
            InstitutionLabel.setVisibility(View.VISIBLE);
            InstitutionAdapter2 institucionAdapter = new InstitutionAdapter2(this, mListaInstituciones);
            mRecyclerInstituciones.setAdapter(institucionAdapter);
        }
        else{
            mRecyclerInstituciones.setVisibility(View.GONE);
            InstitutionLabel.setVisibility(View.GONE);
        }
    }

    public void obtenerTips(String URL){
        //Vacia la lista:
        if(!mListaInstituciones.isEmpty()){
            mListaInstituciones.clear();
        }

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                JSONObject jsonObject = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        jsonObject = jsonArray.getJSONObject(i);

                        Tip tip = new Tip();
                        tip.setID_Tip(jsonObject.getInt("ID_Tip"));
                        tip.setTitulo(jsonObject.getString("Titulo"));
                        tip.setContenido(jsonObject.getString("Texto"));
                        if(isAbusive){
                            tip.setTipo(1);
                        }
                        else{
                            tip.setTipo(2);
                        }

                        mListaTips.add(tip);
                    }
                    catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                mostrarTips();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }
        );
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    private void mostrarTips(){
        //Toast.makeText(getApplicationContext(), String.valueOf(mListaTips.size()), Toast.LENGTH_SHORT).show();
        if(!mListaTips.isEmpty()){
            mRecyclerTips.setVisibility(View.VISIBLE);
            TipsLabel.setVisibility(View.VISIBLE);
            TipAdapter tipAdapter = new TipAdapter(this, mListaTips);
            mRecyclerTips.setAdapter(tipAdapter);
        }
        else{
            mRecyclerTips.setVisibility(View.GONE);
            TipsLabel.setVisibility(View.GONE);
        }
    }
}