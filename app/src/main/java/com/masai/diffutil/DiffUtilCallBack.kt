package com.masai.diffutil

import androidx.recyclerview.widget.DiffUtil

class DiffUtilCallBack(
    private val oldStudentsList: List<Student>,
    private val newStudentList: List<Student>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldStudentsList.size
    }

    override fun getNewListSize(): Int {
        return newStudentList.size
    }

    /**
     * Checks if the items are same based on a key value i.e here considering id is unique
     */
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldStudentsList[oldItemPosition].id == newStudentList[newItemPosition].id
    }

    /**
     * This is called if the areItemsTheSame returns true and checks if other contents are the same
     */
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldStudentsList[oldItemPosition] == newStudentList[newItemPosition]
    }


}