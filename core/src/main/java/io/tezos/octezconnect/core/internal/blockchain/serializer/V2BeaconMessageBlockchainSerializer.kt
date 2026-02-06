package io.tezos.octezconnect.core.internal.blockchain.serializer

import androidx.annotation.RestrictTo
import io.tezos.octezconnect.core.internal.message.v2.V2BeaconMessage
import kotlinx.serialization.KSerializer

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public interface V2BeaconMessageBlockchainSerializer {
    public val message: KSerializer<V2BeaconMessage>
}