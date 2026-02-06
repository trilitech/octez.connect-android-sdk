package io.tezos.octezconnect.core.internal.blockchain.serializer

import androidx.annotation.RestrictTo
import io.tezos.octezconnect.core.internal.message.v1.V1BeaconMessage
import kotlinx.serialization.KSerializer

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public interface V1BeaconMessageBlockchainSerializer {
    public val message: KSerializer<V1BeaconMessage>
}