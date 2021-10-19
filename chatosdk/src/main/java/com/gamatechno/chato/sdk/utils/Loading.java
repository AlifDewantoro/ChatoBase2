package com.gamatechno.chato.sdk.utils;

import android.app.Dialog;
import android.content.Context;
import android.widget.RelativeLayout;

import com.gamatechno.chato.sdk.R;
import com.gamatechno.ggfw.utils.DialogBuilder;

public class Loading extends DialogBuilder {
    RelativeLayout lay;

    public Loading(Context context) {
        super(context, R.layout.layout_loading);
        initComponent(new OnInitComponent() {
            @Override
            public void initComponent(Dialog dialog) {
                lay = dialog.findViewById(R.id.lay);

                setCancellable(false);
                setFullScreen(lay);
            }
        });
    }
}
