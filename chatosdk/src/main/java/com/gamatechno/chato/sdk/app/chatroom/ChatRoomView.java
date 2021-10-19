package com.gamatechno.chato.sdk.app.chatroom;

import com.gamatechno.chato.sdk.app.chatroom.model.ChatReactionResponse;
import com.gamatechno.chato.sdk.app.chatroom.model.ChatRoomUiModel;
import com.gamatechno.chato.sdk.app.kontakchat.KontakModel;
import com.gamatechno.chato.sdk.data.DAO.Chat.Chat;
import com.gamatechno.chato.sdk.data.DAO.Group.Group;
import com.gamatechno.chato.sdk.module.core.BaseView;

import java.util.List;

public interface ChatRoomView {
    interface Presenter{
        void requestHistoryChat(String id, String from_idchat, String room_id, boolean isRefresh);
        void requestHistoryGruopChat(String id, String from_idchat, boolean isRefresh);
        void sendMessage(Chat chat);
        void sendGroupMessage(Chat chat);
        void sendStatusMessage(List<Chat> chatList);
        void checkSelectedChat(List<Chat> chatList);
        void copyChat(List<Chat> chatList);
        void reactionChat(Chat chat, String chatroom_id, String emojiId);
        void starChat(Chat chat, String chatroom_id);
        void downloadFileFromChatToForward(Chat chat, KontakModel kontakModel);
        void requestDeleteMessage(List<Chat> messages, String own_message);
        void searchChat(String keyword, String room_id);
        void getMessageInfo(Chat chat);
        void getGroupMessageInfo(Chat chat, String roomId);
        void getUpdatedGroupInfo(ChatRoomUiModel chatRoomUiModel);
        void pinMessageGroup(Chat chat, Chat pinnedchat, String room_id);
    }

    interface View extends BaseView {
        void onRequestHistoryChat(List<Chat> chats, boolean isRefresh);
        void onSendMessage(Chat chat);
        void onFailedRequestHistoryChat(String message);
        void onFailedSendMessage(Chat chat);
        void onFailedSendMessage(Chat chat, String message);
        void onAppBarAction(int status_itemselected);
        void checkListMessageStatus(List<Chat> list_message_with_status);
        void setStatusOnList(String state);
        void setupStarIcon(boolean isStarred);
        void onStarChat(Chat chat, boolean isStarred);

        void hideContainerReply();
        void onSendStatusMessage();
        void onSuccessDownloadFileToForward(Chat chat, KontakModel kontakModel);

        void onSearchChat(List<Chat> chats);

        void onSuccessReaction(ChatReactionResponse response);
        void openFile(String uri);
        void onGetMessageInfo(Chat chat, String time_delivered, String time_read);
        void onGetGroupMessageInfo(Chat chat, List<KontakModel> terkirim, List<KontakModel> terbaca);
        void onCheckStatusRoom(String message);
        void onUpdateGroupInfo(Group group);
        void onPinMessageGroup(int is_pinned, Chat chat);
        void onLoadingChat();
        void onHideLoadingChat();
    }
}
