package jbox.skillz.medic.Adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import jbox.skillz.medic.Model.Presc;
import jbox.skillz.medic.R;
import jbox.skillz.medic.UploadPrescription;

/**
 * Created by joni on 9/1/2019.
 */

public class PrescAdapter extends ArrayAdapter<Presc> {

      private static final String TAG = "PersonListAdapter";
      private Context mContext;
      int mResource;
      ArrayList<String> prescItemNm;
      ArrayList<Presc> prescList;

      public PrescAdapter(UploadPrescription uploadPrescription, int custom_layout, ArrayList<Presc> prescs, ArrayList<String> prescItemsNames) {
            super(uploadPrescription, custom_layout, prescs);
            mContext=uploadPrescription;
            mResource=custom_layout;
            prescItemNm = prescItemsNames;
            this.prescList = prescs;
      }

      @NonNull
      @Override
      public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            String itemName=getItem(position).getPrescText();

            Presc person=new Presc();

            LayoutInflater inflater=LayoutInflater.from(mContext);
            convertView=inflater.inflate(mResource,parent,false);

            AutoCompleteTextView prescItemText=convertView.findViewById(R.id.presc_list_text);
            prescItemText.setText(itemName);

            ArrayAdapter<String> adp = new ArrayAdapter<String>(mContext,
                    android.R.layout.simple_list_item_1, prescItemNm);
            prescItemText.setAdapter(adp);
            adp.notifyDataSetChanged();

            prescItemText.getText();

            prescItemText.addTextChangedListener(new TextWatcher() {
                  @Override
                  public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                  }

                  @Override
                  public void onTextChanged(CharSequence s, int start, int before, int count) {
                        prescList.remove(position);
                  }

                  @Override
                  public void afterTextChanged(Editable s) {
                        if (!s.toString().isEmpty())
                        {
                              prescList.add(position, new Presc(prescItemText.getText().toString()));
                        }
                        else
                        {
                              prescList.add(position, new Presc(" "));
                        }
                  }
            });


            return convertView;
      }
}
