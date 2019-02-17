package com.example.nfctest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SlideAdapter extends PagerAdapter {
    private Context context;
    LayoutInflater inflater;

    public int[] images;
    private String[] captions;

    public SlideAdapter(Context context, int[] images, String[] captions) {
        this.context = context;
        this.images = images;
        this.captions = captions;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return (view == (LinearLayout) o);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_slideshow, container, false);

        ImageView img = (ImageView) view.findViewById(R.id.imageView_id);
        img.setImageResource(images[position]);

        TextView text = (TextView) view.findViewById(R.id.textView_id);
        text.setText(captions[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }
}
