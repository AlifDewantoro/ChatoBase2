package com.gamatechno.chato.sdk.utils.ChatoEditText;

import android.content.Context;
import android.util.AttributeSet;

import com.chato.chato_emoticon.Helper.EmojiconEditText;


public class ChatEditText extends EmojiconEditText {

    public ChatEditText(Context context) {
        super(context);
    }

    public ChatEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChatEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (text.toString().length() > 0){
            if (text.toString().trim().isEmpty()){
                setText("");
            }
        }
    }
}
