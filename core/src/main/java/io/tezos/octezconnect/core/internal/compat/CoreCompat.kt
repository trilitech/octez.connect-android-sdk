package io.tezos.octezconnect.core.internal.compat

import androidx.annotation.RestrictTo
import io.tezos.octezconnect.core.internal.compat.v2_0_0.CompatWithV2_0_0
import io.tezos.octezconnect.core.scope.BeaconScope

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public class CoreCompat(beaconScope: BeaconScope? = null) : Compat<VersionedCompat>() {
    init {
        register(CompatWithV2_0_0(beaconScope))
    }
}