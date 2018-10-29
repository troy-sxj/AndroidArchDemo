package com.mika.lib.image;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * @Author: mika
 * @Time: 2018-10-29 14:46
 * @Description: <p>
 *
 * </p>
 */
public class ArchImageView extends SimpleDraweeView {

    public ArchImageView(Context context) {
        super(context);
    }

    public ArchImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initDrawerHierarchy(attrs);
    }

    public ArchImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initDrawerHierarchy(attrs);
    }

    public ArchImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initDrawerHierarchy(attrs);
    }

    public ArchImageView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
    }

    /**
     * 处理xml属性
     * @param attrs
     */
    private void initDrawerHierarchy(AttributeSet attrs) {
        if (attrs == null) return;
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ArchImageView);
        if (typedArray != null) {
            int scaleType = typedArray.getInt(R.styleable.ArchImageView_scaleType, -1);
            int placeHolder = typedArray.getInt(R.styleable.ArchImageView_placeHolder, -1);
            int errorHolder = typedArray.getInt(R.styleable.ArchImageView_errorHolder, -1);
            boolean asRound = typedArray.getBoolean(R.styleable.ArchImageView_roundAsCircle, false);
            int overlayColor = typedArray.getColor(R.styleable.ArchImageView_roundWithOverlayColor, getResources().getColor(android.R.color.transparent));

            GenericDraweeHierarchyBuilder builder =
                    new GenericDraweeHierarchyBuilder(getResources());
            if (placeHolder != -1) {
                builder.setPlaceholderImage(placeHolder);
            }
            if (errorHolder != -1) {
                builder.setFailureImage(errorHolder);
            }
            if (scaleType != -1 && getScaleType(scaleType) != null) {
                builder.setPlaceholderImageScaleType(getScaleType(scaleType));
            }
            if (asRound) {
                //圆角图片
                RoundingParams rp = new RoundingParams();
                //设置边框颜色 宽度
                //rp.setBorder(Color.RED, 2);
                //设置圆角
                rp.setRoundAsCircle(true);
                //兼容GIF 圆角
                if (overlayColor != getResources().getColor(android.R.color.transparent)) {
                    rp.setOverlayColor(overlayColor);
                }
                builder.setRoundingParams(rp);
            }
            setHierarchy(builder.build());
            typedArray.recycle();
        }
    }

    private ScalingUtils.ScaleType getScaleType(int value) {
        switch (value) {
            case -1: // none
                return null;
            case 0: // fitXY
                return ScalingUtils.ScaleType.FIT_XY;
            case 1: // fitStart
                return ScalingUtils.ScaleType.FIT_START;
            case 2: // fitCenter
                return ScalingUtils.ScaleType.FIT_CENTER;
            case 3: // fitEnd
                return ScalingUtils.ScaleType.FIT_END;
            case 4: // center
                return ScalingUtils.ScaleType.CENTER;
            case 5: // centerInside
                return ScalingUtils.ScaleType.CENTER_INSIDE;
            case 6: // centerCrop
                return ScalingUtils.ScaleType.CENTER_CROP;
            case 7: // focusCrop
                return ScalingUtils.ScaleType.FOCUS_CROP;
            default:
                return null;
        }
    }

}
