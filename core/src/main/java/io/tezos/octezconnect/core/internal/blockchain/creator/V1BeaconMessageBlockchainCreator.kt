package io.tezos.octezconnect.core.internal.blockchain.creator

import androidx.annotation.RestrictTo
import io.tezos.octezconnect.core.internal.message.v1.V1BeaconMessage
import io.tezos.octezconnect.core.message.BeaconMessage

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public interface V1BeaconMessageBlockchainCreator {
    public fun from(senderId: String, message: BeaconMessage): Result<V1BeaconMessage>
}