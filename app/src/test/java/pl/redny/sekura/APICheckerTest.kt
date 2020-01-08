package pl.redny.sekura

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.runners.MockitoJUnitRunner
import pl.redny.sekura.util.APIChecker

@RunWith(MockitoJUnitRunner::class)
class APICheckerTest {

    @Test
    fun isMarshmallowTestFailure() {
        val buildVersion = mock(BuildVersionAccessor::class.java)
        `when`(buildVersion.SDK_INT).thenReturn(16)
        Assert.assertFalse(APIChecker.isMarshmallow())
    }

    interface BuildVersionAccessor {
        val SDK_INT: Int
    }
}

