package pl.redny.sekura

import android.content.Context
import android.os.Build
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.runners.MockitoJUnitRunner
import pl.redny.sekura.util.APIChecker
import java.lang.reflect.Array.setInt
import java.lang.reflect.AccessibleObject.setAccessible
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.sql.Ref
import pl.redny.sekura.APICheckerTest.BuildVersionAccessor




@RunWith(MockitoJUnitRunner::class)
class APICheckerTest {

//`    @Test
//    fun isMarshmallowTest_success() {
//        val buildVersion = mock(BuildVersionAccessor::class.java)
//        `when`(buildVersion.SDK_INT).thenReturn(25)
//        Assert.assertTrue(APIChecker.isMarshmallow())
//    }`

    @Test
    fun isMarshmallowTest_failure() {
        val buildVersion = mock(BuildVersionAccessor::class.java)
        `when`(buildVersion.SDK_INT).thenReturn(16)
        Assert.assertFalse(APIChecker.isMarshmallow())
    }

    interface BuildVersionAccessor {
        val SDK_INT: Int
    }
}

