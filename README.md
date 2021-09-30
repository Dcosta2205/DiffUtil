# DiffUtil
The powerfull Diff Util in Android Recycler View

## What is RecyclerView ?

```
RecyclerView is flexible and efficient version of ListView. It is an container for rendering larger data set of views that can be recycled and scrolled very efficiently.
```

Before discussing about DiffUtil lets have a look at the code using Recycler View

**Student.kt**

```

data class Student(
    val name: String,
    val id: Int,
    val age: Int,
    val address: String
)

```

**StudentAdapter.kt**

```
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_layout.view.*

class StudentAdapter :
    RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    private val studentList: ArrayList<Student> = ArrayList<Student>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(viewholder: StudentViewHolder, position: Int) {
        val student = studentList[position]
        viewholder.setData(student)
    }

    override fun getItemCount(): Int {
        return studentList.size
    }

    fun updateData(newStudentList: List<Student>) {
        studentList.clear() 
        studentList.addAll(newStudentList)
    }


    class StudentViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun setData(student: Student) {
            itemView.apply {
                tvName.text = student.name
                tvAge.text = "${student.age}"
                tvAddress.text = student.address
            }
        }
    }

}
```

**MainActivity.kt**

```
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var studentList = ArrayList<Student>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buildData()
        setAdapter()
    }

    private fun buildData() {
        for (i in 1..100) {
            val student = Student("Lloyd-$i", i, 26, "Jayanagar 4th Block-$i Bangalore")
            studentList.add(student)
        }
    }

    private fun setAdapter() {
        val linearLayoutManager = LinearLayoutManager(this)
        val studentAdapter = StudentAdapter()
        recyclerView.apply {
            adapter = studentAdapter
            layoutManager = linearLayoutManager
            studentAdapter.updateData(studentList)
        }

    }
}
```

Now imagine the data set has changed and you want to notify the Adapter about the changes then we call

```
studentAdapter.updateData(studentList)
```

and 

```
studentAdapter.notifyDataSetChanged();
```

This will update the recycler view with new set of Data.

But if notifyDataSetChanged was doing the work for you, why was DiffUtils was need?

Let's discuss that now,

- Using notifyDataSetChanged() , there is no way for the RecyclerView to know what the actual changes are there. So, all the visible views are recreated again. This is a very expensive operation.
In this process, the new instance is created of the adapter. Which makes the process very heavy.
- To over come this, Android Launched DiffUtils as part of support library.

DiffUtils is used to know the updats made in the RecyclerView Adapter.

### DiffUtil uses these methods to notify the RecyclerView for any changes in the data set:

- notifyItemMoved
- notifyItemRangeChanged
- notifyItemRangeInserted
- notifyItemRangeRemoved

Compared to `notifyDataSetChanged()` , these methods are far more efficient.

**DiffUtilCallBack.kt**

```
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
    
    /**
    * If areItemTheSame return true and areContentsTheSame returns false DiffUtil calls this method to get a payload about the change.
    */
    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}
```

- `getOldListSize()` : It returns the size of the old list.

- `getNewListSize()` : Returns the size of the new list

- `areItemsTheSame( oldPosition:Int , newPosition:Int)` : Called by the DiffUtil to decide whether two object represent the same Item in the old and new list.

- `areContentsTheSame( oldPosition:Int, newPosition:Int)` : Checks whether two items have the same data.You can change its behavior depending on your UI. This method is called by DiffUtil only if areItemsTheSame returns true. In our example, we are comparing the name and value for the specific item

- `getChangePayload(oldPosition:Int , newPosition:Int)` : If areItemTheSame return true and areContentsTheSame returns false DiffUtil calls this method to get a payload about the change.

- `getChangePayload(oldPosition:Int , newPosition:Int)` : If areItemTheSame return true and areContentsTheSame returns false DiffUtil calls this method to get a payload about the change.

Now make the below changes to the `StudentAdapter**

```
    fun updateData(newStudentList: List<Student>) {
        val diffUtilCallBack = DiffUtilCallBack(studentList, newStudentList)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallBack)
        studentList.clear() // clear the old student list
        studentList.addAll(newStudentList) // update the old list with new List
        diffResult.dispatchUpdatesTo(this)
    }
```

So here we are passing the oldStudentList and newStudentList to the DiffUtilCallback class and calculating the difference between the 2 lists.

After we know the difference, we update the studentList and notify the adapter via dispatchUpdatesTo .
