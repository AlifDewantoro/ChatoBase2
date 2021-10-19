package com.gamatechno.chato.sdk.data.DAO.Label

import com.gamatechno.chato.sdk.R
import com.gamatechno.chato.sdk.data.DAO.RoomChat.RoomChat
import java.io.Serializable

data class LabelModel(var label_id : Int = 0,
                      var label_title : String = "",
                      var is_checked : Boolean = false,
                      var name : String = "",
                      var label_user_id : String = "",
                      var delete_timestamp : String = "",
                      var label_color : String = "",
                      var is_checklist : Int = 0,
                      var count_room : Int = 0,
                      var room : MutableList<RoomChat> = ArrayList()
): Serializable {
    companion object {
        val RED = "RED"
        val BLUE = "BLUE"
        val PURPLE = "PURPLE"
        val GREEN = "GREEN"
        val YELLOW = "YELLOW"

        val TEAM = "TEAM"
        val TASK = "TASK"

        val RED_CODE = R.color.red_500
        val BLUE_CODE = R.color.blue_500
        val PURPLE_CODE = R.color.purple_500
        val GREEN_CODE = R.color.green_500
        val YELLOW_CODE = R.color.yellow_500
    }
}