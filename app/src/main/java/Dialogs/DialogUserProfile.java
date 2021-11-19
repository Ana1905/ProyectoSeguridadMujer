package Dialogs;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.proyectoseguridadmujer.ListAdapter;
import com.example.proyectoseguridadmujer.ListElement;
import com.example.proyectoseguridadmujer.R;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DialogUserProfile extends DialogFragment {

    ImageView ProfilePic;
    TextView Username, Description;
    Button Back, SavedPublications;
    RecyclerView UserPublications;
    RequestQueue requestQueue;

    ListAdapter publicationCommunityAdapter;

    int ID;
    String email, Mail, ImageResource, UsernameLabel, DescriptionLabel;
    boolean isSavedPublication = false;

    List<ListElement> mListaPublicaciones = new ArrayList<ListElement>();

    public DialogUserProfile () {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImageResource = getArguments().getString("Imagen");
        UsernameLabel = getArguments().getString("Usuario");
        ID = getArguments().getInt("ID_Usuaria");

    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.user_profile_dialog, container, false);

        //WiringUp
        ProfilePic = root.findViewById(R.id.ProfileUserPicture);
        Username = root.findViewById(R.id.ProfileUserName);
        Description = root.findViewById(R.id.ProfileUserDescription);
        Back = root.findViewById(R.id.ButtonProfileBack);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        SavedPublications = root.findViewById(R.id.ProfileUserSavedPublications);
        SavedPublications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSavedPublication)
                {
                    isSavedPublication = false;
                    getPublicationList("https://seguridadmujer.com/app_movil/Community/getUserPublicationList.php?ID_Usuaria="+ ID);
                    SavedPublications.setText("Ver publicaciones guardadas");

                    Drawable img = getContext().getResources().getDrawable(R.drawable.folder);
                    img.setBounds(0, 0, 60, 60);
                    SavedPublications.setCompoundDrawables(img, null, null, null);
                }
                else
                {
                    isSavedPublication = true;
                    getPublicationList("https://seguridadmujer.com/app_movil/Community/getSavedPublicationList.php?ID_Usuaria="+ ID);
                    SavedPublications.setText("Ver tus publicaciones");

                    Drawable img = getContext().getResources().getDrawable(R.drawable.socialmedia);
                    img.setBounds(0, 0, 60, 60);
                    SavedPublications.setCompoundDrawables(img, null, null, null);

                }
            }
        });

        UserPublications = root.findViewById(R.id.ProfileUserPublications);
        UserPublications.setLayoutManager(new LinearLayoutManager(getActivity()));

        Glide.with(getContext()).load(ImageResource).into(ProfilePic);
        Username.setText(UsernameLabel);

        getCredentialData();
        getUserDescription("https://seguridadmujer.com/app_movil/Community/ObtenerInfoUsuaria.php?ID_Usuaria=" + ID);

        getPublicationList("https://seguridadmujer.com/app_movil/Community/getUserPublicationList.php?ID_Usuaria="+ ID);

        return root;
    }

    public void getCredentialData()
    {
        SharedPreferences preferences = getContext().getSharedPreferences("Credencials",getContext().MODE_PRIVATE);
        email = preferences.getString("email", "");
    }

    public void getUserDescription (String URL)
    {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                JSONObject jsonObject = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        jsonObject = jsonArray.getJSONObject(i);
                        DescriptionLabel = jsonObject.getString("Descripcion");
                        Mail = jsonObject.getString("Correo");
                    }
                    catch (JSONException e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                if (Mail.equals(email))
                {
                    SavedPublications.setVisibility(View.VISIBLE);
                }
                Description.setText(DescriptionLabel);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }
        );
        requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);
    }

    public void getPublicationList (String Link)
    {
        mListaPublicaciones = new ArrayList<ListElement>();
        //Toast.makeText(getContext(), email, Toast.LENGTH_SHORT).show();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Link, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                String PublicationData = response.toString();
                Gson gson = new Gson();
                ListElement[] PublicationRegister = gson.fromJson(PublicationData, ListElement[].class);

                mListaPublicaciones = Arrays.asList(PublicationRegister);

                setAdapter();
            }
        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonArrayRequest);
    }

    public void setAdapter ()
    {
        ArrayList<ListElement> arrayList = new ArrayList<ListElement>(mListaPublicaciones);
        publicationCommunityAdapter = new ListAdapter(getActivity(), arrayList);
        UserPublications.setAdapter(publicationCommunityAdapter);
    }
}