package pl.redny.sekura.securityRaport

import pl.redny.sekura.util.APIChecker
import pl.redny.sekura.util.SystemProperties

/**
 * Class responsible for generating information on security of android device.
 */
class SecurityReport {
    /**
     * Method responsible for finding Security patch level.
     * Uses official API for API23+, and reads props for older API version.
     *
     * @return [String] of format "rrrr-mm-dd".
     * If Android doesn't support Security patches, returns empty [String].
     */
    fun getSecurityPatchDate(): String {
        return if (APIChecker.isMarshmallow()) {
            android.os.Build.VERSION.SECURITY_PATCH
        } else {
            SystemProperties.getPropFromGetProp("ro.build.version.security_patch")
        }
    }
}