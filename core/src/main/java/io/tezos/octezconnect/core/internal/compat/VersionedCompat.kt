package io.tezos.octezconnect.core.internal.compat

import androidx.annotation.RestrictTo
import io.tezos.octezconnect.core.blockchain.Blockchain

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public interface VersionedCompat {
    public val withVersion: String
    public val blockchain: Blockchain
}