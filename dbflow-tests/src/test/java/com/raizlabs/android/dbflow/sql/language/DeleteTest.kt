package com.raizlabs.android.dbflow.sql.language

import com.raizlabs.android.dbflow.BaseUnitTest
import com.raizlabs.android.dbflow.config.databaseForTable
import com.raizlabs.android.dbflow.models.SimpleModel
import com.raizlabs.android.dbflow.models.SimpleModel_Table
import com.raizlabs.android.dbflow.query.delete
import com.raizlabs.android.dbflow.query.list
import com.raizlabs.android.dbflow.query.select
import com.raizlabs.android.dbflow.structure.save
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class DeleteTest : BaseUnitTest() {

    @Test
    fun validateQuery() {
        databaseForTable<SimpleModel> {
            assertEquals("DELETE ", delete().query)
        }
    }

    @Test
    fun validateDeletion() {
        databaseForTable<SimpleModel> {
            SimpleModel("name").save()
            delete<SimpleModel>().execute()
            assertFalse((select from SimpleModel::class).hasData())
        }
    }

    @Test
    fun validateDeletionWithQuery() {
        databaseForTable<SimpleModel> {
            SimpleModel("name").save()
            SimpleModel("another name").save()

            val where = delete<SimpleModel>().where(SimpleModel_Table.name.`is`("name"))
            assertEquals("DELETE FROM `SimpleModel` WHERE `name`='name'", where.query.trim())
            where.execute()

            assertEquals(1, (select from SimpleModel::class).list.size)
        }
    }
}