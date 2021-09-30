package com.masai.diffutil

data class Student(
    val name: String,
    val id: Int,
    val age: Int,
    val address: String
) {
    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) return false
        val student = other as Student
        if (id != student.id) return false
        if (name != student.name) return false
        if (age != student.age) return false
        if (address != student.address) return false
        return true
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}