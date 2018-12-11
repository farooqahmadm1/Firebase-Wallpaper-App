package com.example.farooq.wallpaperapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.farooq.wallpaperapp.Common.Common;
import com.example.farooq.wallpaperapp.Model.CategoryItem;
import com.example.farooq.wallpaperapp.Model.Wallpaper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class UploadWallpaper extends AppCompatActivity{

    private ImageView uImageView;
    private Button uBrowseButton,uUploadButton;
    private MaterialSpinner uSpinner;

    private Uri uri;
    private String CategorySelected ="";
    List<Object> valueList;
    List<Object> keyList;

    private FirebaseAuth auth;
    private StorageReference storageReference;
    Map<String,String> map =new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_wallpaper);
        valueList = new ArrayList<Object>();
        keyList = new ArrayList<Object>();

        storageReference = FirebaseStorage.getInstance().getReference();

        uImageView = (ImageView) findViewById(R.id.upload_image);
        uBrowseButton = (Button) findViewById(R.id.upload_browse_button);
        uUploadButton= (Button) findViewById(R.id.upload_upload_button);

        uSpinner = (MaterialSpinner) findViewById(R.id.upload_spinner);
        loadSpinnerItems();

    }

    private void loadSpinnerItems() {
        FirebaseDatabase.getInstance().getReference(Common.STR_CATEGORY_BACKGROUND)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                            CategoryItem item = snapshot.getValue(CategoryItem.class);
                            String key = snapshot.getKey();
                            map.put(key,item.getName());
                        }

                        Object[] array = map.values().toArray();
                        valueList.add(0,"Category");
                        valueList.addAll(Arrays.asList(array));
                        uSpinner.setItems(valueList);
                        uSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                                Object[] array = map.keySet().toArray();
                                keyList.add(0,"CategoryKey");
                                keyList.addAll(Arrays.asList(array));
                                CategorySelected = keyList.get(position).toString();
                            }
                        });
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });
    }//loadspinneritemsend function

    public void ChooseImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Pictures")
                ,Common.PCIK_IMAGE_REQUEST_CODE);
    }//function choose Image

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Common.PCIK_IMAGE_REQUEST_CODE && resultCode ==RESULT_OK){
            uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                uImageView.setImageBitmap(bitmap);
                uUploadButton.setEnabled(true);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void UploadImage(View view) {
        if (uSpinner.getSelectedIndex()==0){
            Toast.makeText(this, "Please choose Category", Toast.LENGTH_SHORT).show();
            return;
        }
        if(uri!=null){
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("Uploading...");
            dialog.show();

            // Create file metadata including the content type
            StorageMetadata metadata = new StorageMetadata.Builder()
                    .setContentType("image/jpg")
                    .build();
            final StorageReference ref = storageReference.child(new StringBuilder("Images/").append(UUID.randomUUID().toString()).toString());
            ref.putFile(uri,metadata).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(UploadWallpaper.this, "Uploaded", Toast.LENGTH_SHORT).show();
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            setImageUrlInDatabase(CategorySelected,uri.toString());
                            dialog.dismiss();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                    Toast.makeText(UploadWallpaper.this, "Failed Uplaoding", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = 100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount();
                    dialog.setMessage("Uploaded..."+(int)progress+"%");
                }
            });
        }
    }//function upload Image

    private void setImageUrlInDatabase(String categorySelected, String url) {
        FirebaseDatabase.getInstance().getReference(Common.STR_WALLPAPER)
                .push()
                .setValue(new Wallpaper(categorySelected,url))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        finish();
                    }
                });
    }


}
