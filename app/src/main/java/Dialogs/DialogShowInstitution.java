package Dialogs;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.proyectoseguridadmujer.ImageAdapter;
import com.example.proyectoseguridadmujer.Institutions;
import com.example.proyectoseguridadmujer.R;
import com.example.proyectoseguridadmujer.SocialMedia;
import com.example.proyectoseguridadmujer.SocialMediaAdapter;
import com.example.proyectoseguridadmujer.Tip;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DialogShowInstitution extends DialogFragment implements OnMapReadyCallback{

    Button mBotonVolver;
    TextView InstitutionName, InstitutionArea, InstitutionDescription, InstitutionPhone, InstitutionPage, redesSocialesLabel;
    ImageView InstitutionImage;
    private GoogleMap mMap;
    Institutions institutions = new Institutions();
    Bundle bundle;

    RecyclerView mRecyclerView;
    RecyclerView mRecyclerViewRedesSociales;

    String email = "";
    String MAP_FRAGMENT = "map_fragment";

    ArrayList<String> mListaImagenes = new ArrayList<String>();
    ArrayList<SocialMedia> mListaRedesSociales = new ArrayList<>();

    RequestQueue requestQueue;

    public DialogShowInstitution() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        institutions.setID_Institucion(getArguments().getInt("ID_Institucion"));
        institutions.setNombre(getArguments().getString("Nombre"));
        institutions.setDescripcion(getArguments().getString("Descripcion"));
        institutions.setTelefono(getArguments().getString("Telefono"));
        institutions.setPaginaWeb(getArguments().getString("PaginaWeb"));
        institutions.setLatitud(getArguments().getDouble("Latitud"));
        institutions.setLongitud(getArguments().getDouble("Longitud"));
        institutions.setRutaImagenPresentacion(getArguments().getString("RutaImagen"));
        institutions.setArea(getArguments().getString("Area"));
        institutions.setNombreCategoria(getArguments().getString("NombreCategoria"));

    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_list_content, container, false);

        //WiringUp
        mBotonVolver = root.findViewById(R.id.button_dialog_institution_back);
        InstitutionName = root.findViewById(R.id.InstitutionNameLabel);
        InstitutionImage = root.findViewById(R.id.InstitutionImageHolder);
        InstitutionArea = root.findViewById(R.id.InstitutionAreaContent);
        InstitutionDescription = root.findViewById(R.id.InstitutionDescription);
        InstitutionPhone = root.findViewById(R.id.InstitutionPhone);
        InstitutionPage = root.findViewById(R.id.InstitutionPage);
        redesSocialesLabel = root.findViewById(R.id.redes_sociales_instituciones);

        mRecyclerView = root.findViewById(R.id.recyclerImagenesInstituciones);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false));

        mRecyclerViewRedesSociales = root.findViewById(R.id.recyclerRedesSociales);
        mRecyclerViewRedesSociales.setLayoutManager(new LinearLayoutManager(getContext()));
        /*
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.content)

        */

        //Map Fragment:
        FragmentManager fragmentManager = getChildFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment) fragmentManager
                .findFragmentByTag(MAP_FRAGMENT); // Check if map already exists

        if(mapFragment == null){
            // Create new Map instance if it doesn't exist
            mapFragment = SupportMapFragment.newInstance();
            fragmentManager.beginTransaction()
                    .replace(R.id.map, mapFragment, MAP_FRAGMENT)
                    .commit();
        }
        mapFragment.getMapAsync(this);


        mBotonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        ChangeInstitutionData();

        obtenerImagenes("https://seguridadmujer.com/app_movil/Institucion/obtenerImagenesAdicionales.php?ID_Publicacion="+institutions.getID_Institucion());

        obtenerRedesSociales("https://seguridadmujer.com/app_movil/Institucion/obtenerRedesSociales.php?ID_Publicacion="+institutions.getID_Institucion());


        return root;
    }

    public void ChangeInstitutionData ()
    {
        InstitutionName.setText(institutions.getNombre());
        Glide.with(this).load(institutions.getRutaImagenPresentacion()).into(InstitutionImage);
        InstitutionArea.setText(institutions.getNombreCategoria());
        //InstitutionArea.setText("áéíóú");
        InstitutionDescription.setText(institutions.getDescripcion());
        InstitutionPhone.setText(institutions.getTelefono());
        InstitutionPage.setText(institutions.getPaginaWeb());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(15.0f);
        mMap.setMaxZoomPreference(20.0f);
        LatLng ubication = new LatLng(institutions.getLatitud(), institutions.getLongitud());
        mMap.addMarker(new MarkerOptions().position(ubication).title("Institucion ubicacion"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ubication));
    }

    //Metodo para obtener los reportes de acontecimiento de la base de datos aun en su tiempo de vida:
    public void obtenerImagenes(String URL){
        //Vacia la lista:
        if(!mListaImagenes.isEmpty()){
            mListaImagenes.clear();
        }

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                JSONObject jsonObject = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        jsonObject = jsonArray.getJSONObject(i);

                        mListaImagenes.add(jsonObject.getString("RutaImagen"));
                        //Toast.makeText(getApplicationContext(), jsonObject.getString("RutaImagen"), Toast.LENGTH_SHORT).show();
                    }
                    catch (JSONException e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                mostrarImagenesAdicionales();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }
        );
        requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonArrayRequest);
    }

    private void mostrarImagenesAdicionales(){
        if(!mListaImagenes.isEmpty()){
            mRecyclerView.setVisibility(View.VISIBLE);
            ImageAdapter imageAdapter = new ImageAdapter(getActivity(), mListaImagenes);
            mRecyclerView.setAdapter(imageAdapter);
        }
        else{
            mRecyclerView.setVisibility(View.INVISIBLE);
        }
    }

    public void obtenerRedesSociales(String URL){
        //Vacia la lista:
        if(!mListaImagenes.isEmpty()){
            mListaImagenes.clear();
        }

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                JSONObject jsonObject = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        jsonObject = jsonArray.getJSONObject(i);

                        SocialMedia socialMedia = new SocialMedia();
                        socialMedia.setTipoRedSocial(jsonObject.getString("NombreRedSocial"));
                        socialMedia.setNombreRedSocial(jsonObject.getString("Enlace"));
                        mListaRedesSociales.add(socialMedia);

                        //Toast.makeText(getApplicationContext(), jsonObject.getString("RutaImagen"), Toast.LENGTH_SHORT).show();
                    }
                    catch (JSONException e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
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
        requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonArrayRequest);
    }

    private void mostrarRedesSociales(){
        if(!mListaRedesSociales.isEmpty()){
            redesSocialesLabel.setVisibility(View.VISIBLE);
            mRecyclerViewRedesSociales.setVisibility(View.VISIBLE);
            ImageAdapter imageAdapter = new ImageAdapter(getActivity(), mListaImagenes);
            SocialMediaAdapter socialMediaAdapter = new SocialMediaAdapter(getActivity(), mListaRedesSociales);
            mRecyclerViewRedesSociales.setAdapter(socialMediaAdapter);
        }
        else{
            redesSocialesLabel.setVisibility(View.GONE);
            mRecyclerViewRedesSociales.setVisibility(View.GONE);
        }
    }

}