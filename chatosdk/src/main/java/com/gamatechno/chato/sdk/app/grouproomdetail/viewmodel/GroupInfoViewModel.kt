package com.gamatechno.chato.sdk.app.grouproomdetail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gamatechno.chato.sdk.data.DAO.Group.Group

class GroupInfoViewModel: ViewModel() {
    internal var leavegroup: MutableLiveData<Boolean>? = null
    internal var groupdata: MutableLiveData<Group>? = null
    internal var updateGroupData: MutableLiveData<Group>? = null
    internal var refreshedListUser: MutableLiveData<Group>? = null
    internal var pagecontroller: MutableLiveData<Int>? = null

    fun initLeaveGroup(): LiveData<Boolean> {
        if (leavegroup == null) {
            leavegroup = MutableLiveData<Boolean>()
        }
        return leavegroup!!
    }

    fun updateLeaveGroup(back: Boolean) {
        if (leavegroup != null) {
            leavegroup!!.postValue(back)
        }
    }

    fun initPageController(): LiveData<Int> {
        if (pagecontroller == null) {
            pagecontroller = MutableLiveData<Int>()
        }
        return pagecontroller!!
    }

    fun updatePageController(data: Int) {
        if (pagecontroller != null) {
            pagecontroller!!.postValue(data)
        }
    }

    fun initUpdatedGroupData(): LiveData<Group> {
        if (updateGroupData == null) {
            updateGroupData = MutableLiveData<Group>()
        }
        return updateGroupData!!
    }

    fun updateUpdatedGroupData(group: Group) {
        if (updateGroupData != null) {
            updateGroupData!!.postValue(group)
        }
    }

    fun initRefreshedListUser(): LiveData<Group> {
        if (refreshedListUser == null) {
            refreshedListUser = MutableLiveData<Group>()
        }
        return refreshedListUser!!
    }

    fun updateRefreshedListUser(group: Group) {
        if (refreshedListUser != null) {
            refreshedListUser!!.postValue(group)
        }
    }

    fun initGroupData(): LiveData<Group> {
        if (groupdata == null) {
            groupdata = MutableLiveData<Group>()
        }
        return groupdata!!
    }

    fun updateGroupData(group: Group) {
        if (groupdata != null) {
            groupdata!!.postValue(group)
        }
    }
}