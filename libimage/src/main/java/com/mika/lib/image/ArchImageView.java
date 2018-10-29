package com.mika.lib.image;

import android.content.Context;
import android.util.AttributeSet;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * @Author: mika
 * @Time: 2018-10-29 14:46
 * @Description:
 * <p>
 *
 * </p>
 */
public class ArchImageView extends SimpleDraweeView {

    public ArchImageView(Context context) {
        super(context);
    }

    public ArchImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ArchImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ArchImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public ArchImageView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
    }


}
