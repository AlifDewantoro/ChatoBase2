package com.gamatechno.chato.sdk.data.DAO.File

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class FileModel(@SerializedName("file_id") val file_id : Int,
                      @SerializedName("file_title") val file_title : String,
                      @SerializedName("file_desc") val file_desc : String,
                      @SerializedName("file_name") val file_name : String,
                      @SerializedName("file_url") val file_url : String,
                      @SerializedName("file_thumbnail") val file_thumbnail : String,
                      @SerializedName("file_size") val file_size : Int,
                      @SerializedName("file_type") val file_type : String,
                      @SerializedName("insert_timestamp") val insert_timestamp : String,
                      @SerializedName("insert_user_id") val insert_user_id : Int,
                      @SerializedName("file_document_type") val file_document_type : String) : Serializable