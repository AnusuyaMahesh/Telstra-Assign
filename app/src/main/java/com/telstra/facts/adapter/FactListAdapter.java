package com.telstra.facts.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.telestra.facts.R;
import com.telstra.facts.model.FactDetail;
import com.telstra.facts.network.RequestManager;

import java.util.ArrayList;


/**
 * Created by Anusuya on 3/12/2015.
 */
public class FactListAdapter extends BaseAdapter {
    private static final String TAG = "FactListAdapter";
    Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<FactDetail> factList;


    public FactListAdapter(Context mContext, ArrayList<FactDetail> factList) {

        this.mContext = mContext;
        this.factList = factList;
        mInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return factList.size();
    }

    @Override
    public FactDetail getItem(int position) {
        return factList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        // reuse views
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.fact_list_item, null);

            holder = new ViewHolder();
            holder.imgFact = (ImageView) convertView.findViewById(R.id.fact_image);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.fact_title);
            holder.txtDescription = (TextView) convertView.findViewById(R.id.fact_description);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // fill data
        FactDetail factDetail = factList.get(position);
        String title = factDetail.getTitle();
        String description = factDetail.getDescription();
        String imageHref = factDetail.getImageHref();

        if (title != null) {
            holder.txtTitle.setText(title);
        } else {
            holder.txtTitle.setText(mContext.getString(R.string.title));
        }

        if (description != null) {
            holder.txtDescription.setText(description);
        } else {
            holder.txtDescription.setText(mContext.getString(R.string.description));
        }
        holder.imgFact.setTag(imageHref);

        if (imageHref != null) {
            RequestManager.getInstance().getImageLoader().get(imageHref, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                    String tag = (String) holder.imgFact.getTag();
                    if(tag!=null && tag.equals(imageContainer.getRequestUrl()))
                        holder.imgFact.setImageBitmap(imageContainer.getBitmap());
                }

                @Override
                public void onErrorResponse(VolleyError volleyError) {
                  //  holder.imgFact.setImageResource(R.drawable.img_not_available);

                }
            });
        } else {
            holder.imgFact.setImageResource(R.drawable.img_not_available);
        }


        return convertView;

    }

    /**
     * ViewHolder For the Adapter
     */
    static class ViewHolder {
        ImageView imgFact;
        TextView txtTitle;
        TextView txtDescription;
        boolean isImageSet;
    }


}

