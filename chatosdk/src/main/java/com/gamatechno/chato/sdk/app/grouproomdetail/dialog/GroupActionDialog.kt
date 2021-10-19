package com.gamatechno.chato.sdk.app.grouproomdetail.dialog

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import androidx.recyclerview.widget.LinearLayoutManager
import com.gamatechno.chato.sdk.R
import com.gamatechno.chato.sdk.app.grouproomdetail.dialog.adapter.OptionAdapter
import com.gamatechno.chato.sdk.app.grouproomdetail.helper.GroupDetailHelper
import com.gamatechno.chato.sdk.app.kontakchat.KontakModel
import com.gamatechno.chato.sdk.data.DAO.Group.Group
import com.gamatechno.chato.sdk.data.model.OptionModel
import com.gamatechno.ggfw.utils.DialogBuilder
import kotlinx.android.synthetic.main.dialog_group_action.*

class GroupActionDialog(context: Context?, idUser: String, group: Group, kontakModel: KontakModel, onOnGroupAction: OnGroupActionListener ) : DialogBuilder(context, R.layout.dialog_group_action) {

    var optionAdapter: OptionAdapter? = null
    var list: MutableList<OptionModel> = ArrayList()

    init {
        setAnimation(R.style.DialogBottomAnimation)
        setFullWidth(dialog.lay_dialog)
        setGravity(Gravity.BOTTOM)
        initComponent(idUser, group, kontakModel, onOnGroupAction)
        show()
    }

    fun initComponent(idUser: String, group: Group, kontakModel: KontakModel, onOnGroupAction: OnGroupActionListener){
        with(kontakModel){
            if(group.is_admin == 1){
                if(is_admin == 1){
                    list.addAll(GroupDetailHelper.list_foradmin(kontakModel.user_name, idUser, kontakModel.user_id.toString()))
                } else {
                    list.addAll(GroupDetailHelper.list_forotheradmin(kontakModel.user_name, idUser, kontakModel.user_id.toString()))
                }
            } else {
                list.addAll(GroupDetailHelper.list_foruser(kontakModel.user_name, idUser, kontakModel.user_id.toString()))
            }
        }

        optionAdapter = OptionAdapter(context, list, object : OptionAdapter.OnOptionAdapter{
            override fun onOptionClick(optionModel: OptionModel) {
                when(optionModel.id){
                    GroupDetailHelper.id_chat -> {
                        onOnGroupAction.onChat(dialog, kontakModel)
                        dismiss()
                    }
                    GroupDetailHelper.id_addtoadmin -> {
                        onOnGroupAction.onAddToAdmin(dialog, kontakModel)
                        dismiss()
                    }
                    GroupDetailHelper.id_deletemember -> {
                        onOnGroupAction.onDelete(dialog, kontakModel)
                        dismiss()
                    }
                    GroupDetailHelper.id_removefromadmin -> {
                        onOnGroupAction.onRemoveFromAdmin(dialog, kontakModel)
                        dismiss()
                    }
                }
            }
        })
        with(dialog){
            rv.layoutManager = LinearLayoutManager(context)
            rv.adapter = optionAdapter
        }
    }

    interface OnGroupActionListener {
        fun onChat(dialog: Dialog, kontakModel: KontakModel)
        fun onDelete(dialog: Dialog, kontakModel: KontakModel)
        fun onRemoveFromAdmin(dialog: Dialog, kontakModel: KontakModel)
        fun onAddToAdmin(dialog: Dialog, kontakModel: KontakModel)
    }
}
